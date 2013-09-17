package com.erep.dist.instances.frontend;

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
import com.erep.dist.auth.AuthHelper;
import com.erep.dist.data.RegisteredUser;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

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
		try (FileInputStream input = new FileInputStream(
						"WEB-INF/templates/employer_template.user.js")) {
			String template = IOUtils.toString(input, "UTF-8");
			template = template.replace("{host}", req.getServerName());
			resp.getWriter().write(template);
		}
	}

	private void writeUserJS(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		UserService users = UserServiceFactory.getUserService();

		boolean loggedIn = users.isUserLoggedIn();

		StringBuilder builder = new StringBuilder(
				"window.edist = { user : { loggedIn: ")
				.append(loggedIn)
				.append(", loginURL : ")
				.append(loggedIn ? null : "'" + users.createLoginURL(req.getHeader("referer")) + "'")
				.append(", logoutURL : ")
				.append(loggedIn ? "'" + users.createLogoutURL("/") + "'" : null);

		if (loggedIn) {
			try (CloseablePM pm = new CloseablePM(
					PMF.getNewPersistenceManager())) {
				DataAccessObject dao = new DataAccessObject(pm);
				AuthHelper auth = new AuthHelper(dao, users);
				RegisteredUser user = dao.find(users.getCurrentUser());
				
				builder.append(", name : '").append(user.getName()).append("', email : '")
					.append(users.getCurrentUser().getEmail())
					.append("', hasReportsAccess : ").append(auth.hasReportsAccess())
					.append(", hasPlayerReportsAccess : ").append(auth.hasPlayerReportsAccess())
					.append(", hasManagementAccess: ").append(auth.hasManagementAccess())
					.append(", hasAdminAccess : ").append(auth.hasAdminAccess());
			}
		}

		builder.append(" } };");

		resp.getWriter().write(builder.toString());
	}

}
