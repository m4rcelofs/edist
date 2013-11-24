package com.erep.dist;

import com.erep.dist.data.DistConfig;
import com.erep.dist.data.FightVerification;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.WorkVerification;

public class ConfigHelper {

	private final DataAccessObject	dao;

	public ConfigHelper(DataAccessObject dao) {
		this.dao = dao;
	}

	public DistConfig getConfig(Player player, PlayerGroup group) {
		ServerConfig server = dao.getServerConfig();

		DistConfig config = null;

		switch (server.getGranularity()) {
			case PLAYER:
				config = player.getConfig();
				// fallback to group
				if (config != null)
					break;
			case GROUP:
				config = group.getConfig();
				// fallback to server
				if (config != null)
					break;
			default:
				return server.getMuConfig();
		}
		return config;
	}

	public FightVerification getFightVerification(Player player,
			PlayerGroup group) {
		ServerConfig server = dao.getServerConfig();

		FightVerification fightVerif = null;

		switch (server.getGranularity()) {
			case PLAYER:
				fightVerif = player.getFightVerif();
				// fallback to group
				if (fightVerif != null)
					break;
			case GROUP:
				fightVerif = group.getFightVerif();
				// fallback to MU
				if (fightVerif != null)
					break;
			default:
				return server.getFightVerif();
		}

		return fightVerif;
	}

	public WorkVerification getWorkVerification(Player player, PlayerGroup group) {
		ServerConfig server = dao.getServerConfig();

		WorkVerification workVerif = null;

		switch (server.getGranularity()) {
			case PLAYER:
				workVerif = player.getWorkVerif();
				// fallback to group
				if (workVerif != null)
					break;
			case GROUP:
				workVerif = group.getWorkVerif();
				// fallback to MU
				if (workVerif != null)
					break;
			default:
				return server.getWorkVerif();
		}
		return workVerif;
	}

}
