package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.images.ServingUrlOptions.Builder.withBlobKey;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Avatar;
import com.erep.dist.data.DailyStats;
import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.images.ImagesService;
import com.google.appengine.api.images.ImagesServiceFactory;

public class AvatarUploadCallbackServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	@SuppressWarnings("deprecation")
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		BlobstoreService blobService = BlobstoreServiceFactory
				.getBlobstoreService();
		BlobKey blobKey = blobService.getUploadedBlobs(req).get("avatar");

		try {
			String digest = req.getHeader("edist-md5");
			Key statsKey = KeyFactory.stringToKey(req
					.getHeader("edist-stats-encodedKey"));

			try (CloseablePM pm = PMF.getNewPersistenceManager()) {
				DataAccessObject dao = new DataAccessObject(pm);

				Avatar avatar = new Avatar(digest);
				avatar.setBlob(blobKey);

				ImagesService imgService = ImagesServiceFactory
						.getImagesService();
				avatar.setServingUrl(imgService.getServingUrl(withBlobKey(
						blobKey).secureUrl(true)));

				dao.save(avatar);
				
				DailyStats dbStats = dao.find(new DailyStats(statsKey));
				dbStats.setAvatar(avatar.getServingUrl());
				dbStats.setAvatarKey(avatar);
				
			}
		} catch (Exception e) {
			blobService.delete(blobKey);
		}
	}
}
