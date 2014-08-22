package com.erep.dist.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;

import org.joda.time.DateTime;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.erep.dist.ServerConstants;
import com.erep.dist.data.Licence;
import com.erep.dist.data.dao.Server;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.taskqueue.TaskOptions.Method;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

public class LicensingHelper {

	/**
	 * Caching in the vm to avoid shared memcache
	 */
	private static class LicenceCache {
		DateTime	time;
		Integer		validUntilDay;
		Integer		userLimit;
	}

	private static Logger				logger	= Logger.getLogger(LicensingHelper.class
														.getName());

	private static final LicenceCache	cache	= new LicenceCache();

	private final Server				server;

	public LicensingHelper(Server server) {
		this.server = server;
	}

	public LicensingHelper(PersistenceManager pm) {
		this.server = new Server(pm);
	}

	public boolean isValidLicence() {
		return getLicenceExpiration() >= eRepTimeCalculator.geteRepDay();
	}

	public int getLicenceExpiration() {
		if (cache.time == null || cache.time.plusHours(12).isBeforeNow()) {
			try {
				refreshCache();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Could not validate server licence", e);
				return 0;
			}
		}
		return cache.validUntilDay;
	}

	public int getUserLimit() {
		if (cache.time == null || cache.time.plusHours(12).isBeforeNow()) {
			try {
				refreshCache();
			} catch (Exception e) {
				logger.log(Level.SEVERE, "Could not validate server licence", e);
				return 0;
			}
		}
		return cache.userLimit;
	}

	private void refreshCache() throws Exception {
		Licence licence = server.getLicence();

		if (licence.getSigned() == null || licence.getSigned().isEmpty())
			forceLicenceRefresh();

		String decodedLicence = AssymetricCipherUtils.validateSigned(licence
				.getSigned());
		JSONObject jsObj = (JSONObject) JSONValue.parse(decodedLicence);

		cache.validUntilDay = Integer.valueOf(jsObj.get("validUntilDay")
				.toString());
		cache.userLimit = Integer.valueOf(jsObj.get("userLimit").toString());
		cache.time = new DateTime();

	}

	public boolean forceLicenceRefresh() {
		logger.log(Level.WARNING, "Starting licensing job");
		boolean result = false;
		try {
			URL licenceServer = getLicenceServer();

			Licence licence = server.getLicence();
			licence.setSigned(fetchLicence(licenceServer));

			cache.time = null;
			result = true;
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not download server licence", e);
		}
		logger.log(Level.WARNING, "Licensing refresh finished");
		return result;
	}

	private String fetchLicence(URL licenceServer) throws IOException {
		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();
		HTTPRequest request = new HTTPRequest(licenceServer);
		request.getFetchOptions().setDeadline(300d);
		return new String(fetchService.fetch(licenceServer).getContent(),
				"UTF-8");
	}

	private URL getLicenceServer() throws MalformedURLException {
		String appId = SystemProperty.applicationId.get();
		String licenceServer = ServerConstants.LICENCE_SERVER.replace(
				"{appId}", appId);
		return new URL(licenceServer);
	}

	public static void scheduleLicenceUpdate() {
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions taskOptions = TaskOptions.Builder
				.withUrl("/admin/cron/licence")
				.header("Host",
						BackendServiceFactory.getBackendService()
								.getBackendAddress("edist-default-backend"))
				.method(Method.GET);
		queue.add(taskOptions);
	}

}
