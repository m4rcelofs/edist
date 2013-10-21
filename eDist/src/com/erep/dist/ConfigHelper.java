package com.erep.dist;

import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Player;
import com.erep.dist.data.ServerConfig;

public class ConfigHelper {

	private final DataAccessObject	dao;

	public ConfigHelper(DataAccessObject dao) {
		this.dao = dao;
	}

	public DistConfig getConfig(Player player) {
		ServerConfig server = dao.getServerConfig();

		DistConfig config = null;

		switch (server.getGranularity()) {
			case PLAYER:
				config = player.getConfig();
				break;
			case GROUP:
				config = player.getGroup().getConfig();
				break;
			default:
				return server.getMuConfig();
		}

		// default fallback
		if (config == null)
			config = server.getMuConfig();

		return config;
	}

}
