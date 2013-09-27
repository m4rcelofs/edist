package com.erep.dist.instances.frontend.tasks;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.simple.JSONObject;

import com.erep.dist.Mailer;
import com.erep.dist.ServerConstants;
import com.erep.dist.auth.AssymetricCipherUtils;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

public class NotifyEmailChangeTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(NotifyEmailChangeTask.class
															.getName());

	private final String		oldEmail;
	private final String		newEmail;

	public NotifyEmailChangeTask(String oldEmail, String newEmail) {
		this.oldEmail = oldEmail;
		this.newEmail = newEmail;
	}

	@Override
	public void run() {
		try {
			notifyLicenceServer(newEmail);
			notifyOldEmail(oldEmail, newEmail);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void notifyLicenceServer(String newEmail) throws IOException {

		if (newEmail == null || newEmail.isEmpty())
			return;

		URL server = new URL(ServerConstants.LICENCE_SERVER_EMAIL_UPDATE);

		HTTPRequest request = new HTTPRequest(server, HTTPMethod.POST);
		request.getFetchOptions().setDeadline(300d); // 5min

		JSONObject jsObj = new JSONObject();
		jsObj.put("appId", SystemProperty.applicationId.get());
		jsObj.put("email", newEmail);

		String encoded = "encrypted=";
		try {
			encoded += AssymetricCipherUtils
					.encryptPublic(jsObj.toJSONString());
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Could not encrypt email update", e);
			throw new IOException(e);
		}
		request.setPayload(encoded.getBytes("UTF-8"));

		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();

		HTTPResponse response = fetchService.fetch(request);
		if (response.getResponseCode() != 200)
			throw new IOException("Could not update email on licence server");
	}

	private void notifyOldEmail(String oldEmail, String newEmail)
			throws IOException {

		if (oldEmail == null || oldEmail.isEmpty())
			return;

		StringBuilder builder = new StringBuilder(
				"<p><b>eDist - Email Change Notification</b></p>");

		builder.append("<p>App: ").append(SystemProperty.applicationId.get())
				.append("</p>");
		builder.append("<p>Old e-mail: ").append(oldEmail).append("</p>");
		builder.append("<p>New e-mail: ").append(newEmail).append("</p>");

		Mailer mailer = new Mailer();
		mailer.mail("eDist - Email Change Notification", builder.toString(),
				mailer.new MailAddress(oldEmail, oldEmail), mailer.new MailAddress("admins"));

	}

}
