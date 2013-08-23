package com.erep.dist.servlets.install;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.ServerConstants;
import com.erep.dist.data.InstallLog;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

public class InstallServlet extends HttpServlet {

	private static final long		serialVersionUID	= 1L;

	private static final DateFormat	dateFormat			= new SimpleDateFormat(
																"yyyy.MM.dd HH:mm:ss zzzz 'GMT' Z");

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			try {
				pm.getObjectById(ServerConfig.class, ServerConstants.DEFAULT);
				resp.getWriter().write("Already installed!");
			} catch (JDOObjectNotFoundException e) {
				resp.getWriter().write("Installing, please wait... <br />");
				long init = new Date().getTime();
				install(pm);
				long time = new Date().getTime() - init;
				resp.getWriter().write(
						"Installed successfully in " + time + "ms (:");
			}
		}

		writeStats(resp);

	}

	private void writeStats(HttpServletResponse resp) throws IOException {
		resp.getWriter().write("\n<hr />\n");
		resp.getWriter().write("<b>Your current setup:</b> <br />");
		resp.getWriter().write(
				"App ID: " + SystemProperty.applicationId.get() + "<br />");
		resp.getWriter().write(
				"App Version: " + SystemProperty.applicationVersion.get()
						+ "<br />");
		resp.getWriter().write(
				"Database Version: " + ServerConstants.DB_VERSION + "<br />");
		resp.getWriter().write(
				"AppEngine SDK version: " + SystemProperty.version.get()
						+ "<br />");

		User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		resp.getWriter().write(
				"You are: "
						+ (currentUser != null ? currentUser.getEmail()
								: "not logged in") + "<br />");

		resp.getWriter().write(
				"Server time: " + dateFormat.format(new Date()) + "<br />");
		resp.getWriter().write(
				"eRepublik day: " + eRepTimeCalculator.geteRepDay() + " "
						+ eRepTimeCalculator.geteRepTime());

	}

	private void install(PersistenceManager pm) {
		pm.makePersistent(InstallHelper.newDefaultGroup());
		pm.makePersistent(InstallHelper.newDefaultConfig());
		pm.makePersistent(InstallHelper.newDefaultLicence());

		InstallLog log = new InstallLog();
		log.setDate(new Date());
		log.setDbVersion(ServerConstants.DB_VERSION);
		log.setAppVersion(SystemProperty.applicationVersion.get());
		log.setAppEngineVersion(SystemProperty.version.get());
		User currentUser = UserServiceFactory.getUserService().getCurrentUser();
		log.setUserEmail(currentUser != null ? currentUser.getEmail()
				: "not logged in");

		pm.makePersistent(log);
	}

}
