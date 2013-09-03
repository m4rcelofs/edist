package com.erep.dist.servlets;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import com.erep.dist.Mailer;
import com.erep.dist.ServerConstants;
import com.erep.dist.auth.AssymetricCipherUtils;
import com.google.appengine.api.urlfetch.HTTPMethod;
import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

public class NotifyEmailChangeTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(NotifyEmailChangeTask.class
															.getName());

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String oldEmail = req.getParameter("oldEmail");
		String newEmail = req.getParameter("newEmail");

		notifyLicenceServer(newEmail);
		notifyOldEmail(oldEmail, newEmail);

	}

	@SuppressWarnings("unchecked")
	private void notifyLicenceServer(String newEmail) throws IOException {
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
		request.setPayload(encoded.getBytes());

		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();

		HTTPResponse response = fetchService.fetch(request);
		if (response.getResponseCode() != 200)
			throw new IOException("Could not update email on licence server");
	}

	private void notifyOldEmail(String oldEmail, String newEmail)
			throws IOException {
		StringBuilder builder = new StringBuilder(
				"<p><b>eDist - Email Change Notification</b></p>");

		builder.append("<p>App: ").append(SystemProperty.applicationId.get())
				.append("</p>");
		builder.append("<p>Old e-mail: ").append(oldEmail).append("</p>");
		builder.append("<p>New e-mail: ").append(newEmail).append("</p>");

		Mailer mailer = new Mailer();
		mailer.mail("eDist - Email Change Notification", builder.toString(),
				mailer.new MailAddress(oldEmail, oldEmail));

	}

}
