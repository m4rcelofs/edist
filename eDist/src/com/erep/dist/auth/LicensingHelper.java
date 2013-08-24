package com.erep.dist.auth;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.joda.time.DateTime;

import com.erep.dist.DataAccessObject;
import com.erep.dist.ServerConstants;
import com.erep.dist.data.Licence;
import com.erep.dist.erep.eRepTimeCalculator;
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
		String		decodedLicence;
	}

	private static Logger				logger	= Logger.getLogger(LicensingHelper.class
														.getName());

	private static final LicenceCache	cache	= new LicenceCache();

	private final DataAccessObject		dao;

	public LicensingHelper(DataAccessObject dao) {
		this.dao = dao;
	}

	public boolean validate() {
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
		return Integer.valueOf(cache.decodedLicence);
	}

	private void refreshCache() throws Exception {
		Licence licence = dao.getLicence();

		cache.decodedLicence = AssymetricCipherUtils.validateLicence(licence
				.getSigned());
		cache.time = new DateTime();

	}

	public void forceLicenceRefresh() {
		logger.log(Level.WARNING, "Starting licensing job");
		try {
			URL licenceServer = getLicenceServer();

			Licence licence = dao.getLicence();
			licence.setSigned(fetchLicence(licenceServer));

			cache.time = null;

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not download server licence", e);
		}
		logger.log(Level.WARNING, "Licensing refresh finished");
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

}
