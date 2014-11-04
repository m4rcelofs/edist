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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.dao.Server;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class PlayerReportsFilter implements Filter {

	private static Logger	log	= Logger.getLogger(PlayerReportsFilter.class
										.getName());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain filterChain) throws IOException, ServletException {

		UserService userService = UserServiceFactory.getUserService();

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			AuthHelper auth = new AuthHelper(new Server(pm),
					userService);

			if (auth.hasPlayerReportsAccess())
				filterChain.doFilter(req, resp);
			else {
				if (!userService.isUserLoggedIn()) {
					String destination = "/login?destination="
							+ ((HttpServletRequest) req).getRequestURL();
					((HttpServletResponse) resp).sendRedirect(destination);

				} else {
					log.log(Level.SEVERE, "Unauthorized access attempt!");
					((HttpServletResponse) resp).sendError(403);
				}
			}
		}
	}
}
