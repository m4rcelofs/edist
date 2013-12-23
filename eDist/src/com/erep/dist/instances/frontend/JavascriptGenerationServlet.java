package com.erep.dist.instances.frontend;

import static org.apache.commons.lang3.StringUtils.replace;

import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.ServerConstants;
import com.erep.dist.auth.AuthHelper;
import com.erep.dist.cache.Cacheable;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.api.utils.SystemProperty;

public class JavascriptGenerationServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/javascript");

		switch (req.getPathInfo()) {
			case "/user.js":
			case "/user.nocache.js":
				writeUserJS(req, resp);
				break;
			case "/header.js":
			case "/header.nocache.js":
				writeHeaderJS(req, resp);
				break;
			case "/footer.js":
			case "/footer.nocache.js":
				writeFooterJS(req, resp);
				break;
			case "/employer.user.js":
				writeEmployerGreasemonkeyJS(req, resp);
				break;
			default:
				resp.getWriter().write(
						"// no content available for " + req.getPathInfo());
		}
	}

	private void writeEmployerGreasemonkeyJS(HttpServletRequest req,
			HttpServletResponse resp) throws IOException {
		String template = loadTemplate("employer_template.user.js");
		template = replace(template, "{host}", req.getServerName());
		resp.getWriter().write(template);
	}

	private void writeUserJS(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		
		UserService users = UserServiceFactory.getUserService();
		boolean loggedIn = users.isUserLoggedIn();
		
		String template = loadTemplate("user_template.js");
		
		template = replace(template, "{loggedIn}", ""+loggedIn);
		String referer = req.getHeader("referer") != null ? req.getHeader("referer") : req.getRequestURI();
		template = replace(template, "{loginURL}", loggedIn ? "null" : "'" + users.createLoginURL(referer) + "'");		
		template = replace(template, "{logoutURL}", loggedIn ? "'" + users.createLogoutURL("/") + "'": "null");		
	
		
		try (CloseablePM pm = new CloseablePM(
			PMF.getNewPersistenceManager())) {
			DataAccessObject dao = new DataAccessObject(pm);
			AuthHelper auth = new AuthHelper(dao, users);
			RegisteredUser user = dao.find(users.getCurrentUser());

			template = replace(template, "{name}", user != null ? "'" + user.getName() + "'" : "null");
			template = replace(template, "{email}", loggedIn ? "'" + users.getCurrentUser().getEmail() + "'" : "null");
			template = replace(template, "{hasReportsAccess}", "" + auth.hasReportsAccess());
			template = replace(template, "{hasPlayerReportsAccess}", "" + auth.hasPlayerReportsAccess());
			template = replace(template, "{hasManagementAccess}", "" + auth.hasManagementAccess());
			template = replace(template, "{hasAdminAccess}", "" + auth.hasAdminAccess());
		}

		resp.getWriter().write(template);
	}
	
	private void writeHeaderJS(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String template = loadTemplate("header_template.js");
		
		try (CloseablePM pm = new CloseablePM(
				PMF.getNewPersistenceManager())) {
			DataAccessObject dao = new DataAccessObject(pm);
			template = replace(template, "{muName}", dao.getServerConfig().getMuName());	
		}
		
		resp.getWriter().write(template);
	}
	
	private void writeFooterJS(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String template = loadTemplate("footer_template.js");
		template = replace(template, "{appId}", SystemProperty.applicationId.get());
		template = replace(template, "{version}", SystemProperty.applicationVersion.get());
		template = replace(template, "{eRepDay}", "" + eRepTimeCalculator.geteRepDay());
		template = replace(template, "{pageSize}", "" + ServerConstants.QUERY_PAGE_LIMIT);
		resp.getWriter().write(template);
	}

	@Cacheable
	private String loadTemplate(String filename) throws IOException {
		try (FileInputStream input = new FileInputStream("WEB-INF/templates/"
				+ filename)) {
			return IOUtils.toString(input, "UTF-8");
		}
	}

}
