package com.erep.dist;

import com.erep.dist.data.Player;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ConfigGranularity;
import com.erep.dist.data.enums.RequestType;

public class ConfigHelper {

	private final DataAccessObject	dao;

	public ConfigHelper(DataAccessObject dao) {
		this.dao = dao;
	}

	public boolean getConfigAutoSend(Player player) {
		Boolean autoSend = null;

		ServerConfig server = dao.getServerConfig();

		if (server.getGranularity() == ConfigGranularity.PLAYER)
			autoSend = player.getAutoSend();
		else
			if (server.getGranularity() == ConfigGranularity.GROUP)
				autoSend = player.getGroup().getAutoSend();

		// default fallback
		if (autoSend == null)
			autoSend = server.getMuConfig().getAutoSend();

		return autoSend;

	}

	public boolean getConfigRocketAllowed(Player player) {
		Boolean rocketAllowed = null;
		ServerConfig server = dao.getServerConfig();

		if (server.getGranularity() == ConfigGranularity.PLAYER)
			rocketAllowed = player.getRocketAllowed();
		else
			if (server.getGranularity() == ConfigGranularity.GROUP)
				rocketAllowed = player.getGroup().getRocketAllowed();

		// default fallback
		if (rocketAllowed == null)
			rocketAllowed = server.getMuConfig().getRocketAllowed();

		return rocketAllowed;
	}

	public int getConfigMaxFF(Player player) {
		Integer maxFF = null;
		ServerConfig server = dao.getServerConfig();

		if (server.getGranularity() == ConfigGranularity.PLAYER)
			maxFF = player.getCurrentMaxDailyFF();
		else
			if (server.getGranularity() == ConfigGranularity.GROUP)
				maxFF = player.getGroup().getCurrentMaxDailyFF();

		// default fallback
		if (maxFF == null)
			maxFF = server.getMuConfig().getCurrentMaxDailyFF();

		return maxFF;
	}

	public RequestType getConfigDistType(Player player) {
		RequestType type = null;
		ServerConfig server = dao.getServerConfig();

		if (server.getGranularity() == ConfigGranularity.PLAYER)
			type = player.getAllowedType();
		else
			if (server.getGranularity() == ConfigGranularity.GROUP)
				type = player.getGroup().getAllowedType();

		// default fallback
		if (type == null)
			type = server.getMuConfig().getAllowedType();

		return type;
	}
}
