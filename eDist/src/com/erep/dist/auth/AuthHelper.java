package com.erep.dist.auth;

import com.erep.dist.DataAccessObject;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.RegisteredUser.Role;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthHelper {

	private final DataAccessObject	dao;
	private final UserService		userService;

	public AuthHelper(DataAccessObject dao) {
		this(dao, UserServiceFactory.getUserService());
	}

	public AuthHelper(DataAccessObject dao, UserService userService) {
		this.dao = dao;
		this.userService = userService;
	}

	public boolean hasReportsAccess() {
		ServerConfig config = dao.getServerConfig();

		switch (config.getReportsAccess()) {
			case ADMINS:
				return hasAdminAccess();
			case MODS:
				return isModerator();
			case VIEWER:
				return isViewer();
			case PUBLIC:
				return true;
			default:
				return false;
		}
	}

	public boolean hasPlayerReportsAccess() {
		ServerConfig config = dao.getServerConfig();

		switch (config.getPlayerReportsAccess()) {
			case ADMINS:
				return hasAdminAccess();
			case MODS:
				return isModerator();
			case VIEWER:
				return isViewer();
			case PUBLIC:
				return true;
			default:
				return false;
		}
	}

	public boolean hasManagementAccess() {
		return isModerator();
	}

	public boolean hasAdminAccess() {
		return userService.isUserLoggedIn() && userService.isUserAdmin();
	}

	private boolean isViewer() {
		if (userService.isUserLoggedIn()) {
			RegisteredUser regUser = dao.find(userService.getCurrentUser());
			return regUser.getRole() == Role.VIEWER;
		}
		return false;
	}

	private boolean isModerator() {
		if (userService.isUserLoggedIn()) {
			RegisteredUser regUser = dao.find(userService.getCurrentUser());
			return regUser.getRole() == Role.MODERATOR;
		}
		return false;
	}

}
