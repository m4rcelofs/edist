package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.blobstore.UploadOptions.Builder.withMaxUploadSizeBytes;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.esxx.js.protocol.GAEConnectionManager;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.Avatar;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.dao.Avatars;
import com.erep.dist.data.dao.Stats;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class AvatarFetchTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(AvatarFetchTask.class
															.getName());

	private Key					statsKey;
	private String				avatarLink;

	public AvatarFetchTask(DailyStats stats, String avatarLink) {
		this.statsKey = stats.getKey();
		this.avatarLink = avatarLink.startsWith("http") ? avatarLink : "http:" + avatarLink;
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			URLFetchService fetchService = URLFetchServiceFactory
					.getURLFetchService();

			HTTPResponse response = fetchService.fetch(new URL(avatarLink));

			if (response.getResponseCode() == 404) {
				logger.log(Level.WARNING, "Could not find {0}", avatarLink);
				return;
			}

			String digest = getDigest(response);
			Stats stats = new Stats(pm);
			Avatars avatars = new Avatars(pm);

			try {
				Avatar exists = avatars.find(new Avatar(digest));
				DailyStats dbStats = stats.find(new DailyStats(statsKey));
				dbStats.setAvatar(exists.getServingUrl());
				dbStats.setAvatarKey(exists);
			} catch (JDOObjectNotFoundException e) {
				uploadToBlobstore(statsKey, digest, response);
			}

		} catch (Exception e) {
			logger.log(Level.WARNING, e.getMessage(), e);
			throw new RuntimeException(e);
		}

	}

	private String getDigest(HTTPResponse response) throws Exception {
		MessageDigest digest = MessageDigest.getInstance("MD5");
		return getHexString(digest.digest(response.getContent()));
	}

	private static String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	private void uploadToBlobstore(Key statsKey, String digest,
			HTTPResponse response) throws IOException {

		BlobstoreService blobService = BlobstoreServiceFactory
				.getBlobstoreService();
		String uploadUrl = blobService.createUploadUrl(
				"/admin/avatarUploadCallback", withMaxUploadSizeBytes(524288)); // 512kB

		DefaultHttpClient client = new DefaultHttpClient(
				new GAEConnectionManager());
		HttpPost httpPost = new HttpPost(uploadUrl);

		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new ByteArrayBody(response.getContent(),
				"image/jpeg", digest + ".jpg");
		mpEntity.addPart("avatar", cbFile);

		httpPost.addHeader("edist-md5", digest);
		httpPost.addHeader("edist-stats-encodedKey", KeyFactory.keyToString(statsKey));

		httpPost.setEntity(mpEntity);

		HttpResponse uploadResponse = client.execute(httpPost);
		if (uploadResponse.getStatusLine().getStatusCode() != 200)
			throw new IOException("Could not upload avatar: "
					+ uploadResponse.getStatusLine().getStatusCode());
	}

}
