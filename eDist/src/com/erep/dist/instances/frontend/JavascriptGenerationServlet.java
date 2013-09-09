package com.erep.dist.instances.frontend;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.RegisteredUser.Role;
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
				writeUserJS(resp);
				break;
			default:
				resp.getWriter().write("// no content available for " + req.getPathInfo());
		}
	}

	private void writeUserJS(HttpServletResponse resp) throws IOException {
		UserService users = UserServiceFactory.getUserService();
		
		boolean loggedIn = users.isUserLoggedIn();
		
		StringBuilder builder = new StringBuilder(
				"window.edist = { user : { loggedIn: ")
				.append(loggedIn)
				.append(", loginURL : ")
				.append(loggedIn ? null : "'" + users.createLoginURL("") + "'")
				.append(", logoutURL : ")
				.append(loggedIn ? "'" + users.createLogoutURL("") + "'" : null);
		
		if(loggedIn) {
			boolean isModerator = false;
			String name = "";
			try(CloseablePM pm = new CloseablePM(PMF.getNewPersistenceManager())) {
				DataAccessObject dao = new DataAccessObject(pm);
				RegisteredUser user = dao.find(users.getCurrentUser());
				isModerator = user.getRole() == Role.MODERATOR;
				name = user.getName();
			}
			
			builder.append(", name : '")
					.append(name)
					.append("', email : '")
					.append(users.getCurrentUser().getEmail())
					.append("', isAdmin : ")
					.append(users.isUserAdmin())
					.append(", isModerator : ")
					.append(isModerator);
		}
		
		builder.append(" } };");
		
		resp.getWriter().write(builder.toString());
	}

}
