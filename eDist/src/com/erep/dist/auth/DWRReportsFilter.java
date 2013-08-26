package com.erep.dist.auth;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.directwebremoting.AjaxFilter;
import org.directwebremoting.AjaxFilterChain;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.ServerConstants;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.RegisteredUser.Role;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class DWRReportsFilter implements AjaxFilter {

	private static Logger	log	= Logger.getLogger(DWRReportsFilter.class
										.getName());

	@Override
	public Object doFilter(Object obj, Method method, Object[] params,
			AjaxFilterChain chain) throws Exception {

		if (!ServerConstants.PRIVATE_REPORTS)
			return chain.doFilter(obj, method, params);

		UserService userService = UserServiceFactory.getUserService();

		if (!userService.isUserLoggedIn()) {
			log.log(Level.SEVERE, "User is not logged-in in filter");
			return null;
		}

		User gUser = userService.getCurrentUser();

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			RegisteredUser regUser = dao.find(gUser);

			if (regUser.getRole() != Role.MODERATOR) {
				log.log(Level.SEVERE,
						"Unauthorized access attempt: " + gUser.getEmail());
				return null;
			}
		}

		return chain.doFilter(obj, method, params);
	}

}
