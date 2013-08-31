package com.erep.dist.auth;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.ServerConstants;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.RegisteredUser.Role;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class ReportsFilter implements Filter {

	private static Logger	log	= Logger.getLogger(ReportsFilter.class
										.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {

		if (!ServerConstants.PRIVATE_REPORTS) {
			filterChain.doFilter(req, resp);
			return;
		}

		UserService userService = UserServiceFactory.getUserService();

		if (!userService.isUserLoggedIn()) {
			((HttpServletResponse) resp).sendError(401);
			log.log(Level.SEVERE, "User is not logged-in in filter");
			return;
		}

		User gUser = userService.getCurrentUser();

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			RegisteredUser regUser = dao.find(gUser);

			if (regUser.getRole() != Role.MODERATOR) {
				((HttpServletResponse) resp).sendError(403);
				log.log(Level.SEVERE,
						"Unauthorized access attempt: " + gUser.getEmail());
				return;
			}
		}

		filterChain.doFilter(req, resp);

	}

}
