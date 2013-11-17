package com.erep.dist;

import com.erep.dist.data.DistConfig;
import com.erep.dist.data.FightVerification;
import com.erep.dist.data.Player;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.WorkVerification;

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

	public FightVerification getFightVerification(Player player) {
		ServerConfig server = dao.getServerConfig();

		FightVerification fightVerif = null;

		switch (server.getGranularity()) {
			case PLAYER:
				fightVerif = player.getFightVerif();
				break;
			case GROUP:
				fightVerif = player.getGroup().getFightVerif();
				break;
			default:
				return server.getFightVerif();
		}

		// default fallback
		if (fightVerif == null)
			fightVerif = server.getFightVerif();

		return fightVerif;
	}

	public WorkVerification getWorkVerification(Player player) {
		ServerConfig server = dao.getServerConfig();

		WorkVerification fightVerif = null;

		switch (server.getGranularity()) {
			case PLAYER:
				fightVerif = player.getWorkVerif();
				break;
			case GROUP:
				fightVerif = player.getGroup().getWorkVerif();
				break;
			default:
				return server.getWorkVerif();
		}

		// default fallback
		if (fightVerif == null)
			fightVerif = server.getWorkVerif();

		return fightVerif;
	}

}
