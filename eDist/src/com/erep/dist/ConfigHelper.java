package com.erep.dist;

import javax.jdo.PersistenceManager;

import com.erep.dist.data.DistConfig;
import com.erep.dist.data.FightVerification;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.WorkVerification;
import com.erep.dist.data.dao.Server;

public class ConfigHelper {

	private final Server	server;

	public ConfigHelper(PersistenceManager pm) {
		this.server = new Server(pm);
	}

	public DistConfig getConfig(Player player, PlayerGroup group) {
		ServerConfig sConfig = server.getConfig();

		switch (sConfig.getGranularity()) {
			case PLAYER:
				if (player.getConfig() != null)
					return player.getConfig();
				// fallback to group
			case GROUP:
				if (group.getConfig() != null)
					return group.getConfig();
				// fallback to server
			default:
				return sConfig.getConfig();
		}
	}

	public FightVerification getFightVerification(Player player,
			PlayerGroup group) {
		ServerConfig sConfig = server.getConfig();
		switch (sConfig.getGranularity()) {
			case PLAYER:
				if (player.getFightVerif() != null)
					return player.getFightVerif();
				// fallback to group
			case GROUP:
				if (group.getFightVerif() != null)
					return group.getFightVerif();
				// fallback to MU
			default:
				return sConfig.getFightVerif();
		}
	}

	public WorkVerification getWorkVerification(Player player, PlayerGroup group) {
		ServerConfig sConfig = server.getConfig();

		switch (sConfig.getGranularity()) {
			case PLAYER:
				if (player.getWorkVerif() != null)
					return player.getWorkVerif();
				// fallback to group
			case GROUP:
				if (group.getWorkVerif() != null)
					return group.getWorkVerif();
				// fallback to MU
			default:
				return sConfig.getWorkVerif();
		}
	}

}
