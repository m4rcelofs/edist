package com.erep.dist.auth;

import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.dao.Server;
import com.erep.dist.data.enums.RegisteredUserRole;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

public class AuthHelper {

	private final Server	server;
	private final UserService		userService;

	public AuthHelper(Server server) {
		this(server, UserServiceFactory.getUserService());
	}

	public AuthHelper(Server server, UserService userService) {
		this.server = server;
		this.userService = userService;
	}

	public boolean hasReportsAccess() {
		ServerConfig config = server.getConfig();

		switch (config.getReportsAccess()) {
			case ADMINS:
				return hasAdminAccess();
			case MODS:
				return isModerator() || hasAdminAccess();
			case VIEWER:
				return isViewer() || isModerator() || hasAdminAccess();
			case PUBLIC:
				return true;
			default:
				return false;
		}
	}

	public boolean hasPlayerReportsAccess() {
		ServerConfig config = server.getConfig();

		switch (config.getPlayerReportsAccess()) {
			case ADMINS:
				return hasAdminAccess();
			case MODS:
				return isModerator() || hasAdminAccess();
			case VIEWER:
				return isViewer() || isModerator() || hasAdminAccess();
			case PUBLIC:
				return true;
			default:
				return false;
		}
	}

	public boolean hasManagementAccess() {
		return isModerator() || isAdmin();
	}

	public boolean hasAdminAccess() {
		return isAdmin();
	}

	private boolean isViewer() {
		if (userService.isUserLoggedIn()) {
			RegisteredUser regUser = server.find(userService.getCurrentUser());
			return regUser.getRole() == RegisteredUserRole.VIEWER;
		}
		return false;
	}

	private boolean isModerator() {
		if (userService.isUserLoggedIn()) {
			RegisteredUser regUser = server.find(userService.getCurrentUser());
			return regUser.getRole() == RegisteredUserRole.MODERATOR;
		}
		return false;
	}
	
	private boolean isAdmin() {
		return userService.isUserLoggedIn() && userService.isUserAdmin();
	}

}
