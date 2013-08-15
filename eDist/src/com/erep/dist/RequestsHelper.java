package com.erep.dist;

import com.erep.dist.data.Player;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ConfigGranularity;

public class RequestsHelper {

	private final DataAccessObject	dao;

	public RequestsHelper(DataAccessObject dao) {
		this.dao = dao;
	}

	public boolean verifyRocketAllowed(Player player) {
		boolean rocketAllowed = getConfigRocketAllowed(player);
		// TODO check dists of the day
		return rocketAllowed;
	}

	public int verifyMaxFF(Player player) {
		int maxFF = getConfigMaxFF(player);
		// TODO check dists of the day
		return maxFF;
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

}
