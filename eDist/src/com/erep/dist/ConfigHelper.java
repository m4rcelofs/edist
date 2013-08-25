package com.erep.dist;

import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ConfigGranularity;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;

public class ConfigHelper {

	private final DataAccessObject	dao;

	public ConfigHelper(DataAccessObject dao) {
		this.dao = dao;
	}
	
	public boolean getAutoSend(Player player) {
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

	public boolean getRocketAllowed(Player player) {
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

	public int getMaxFF(Player player) {
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

	public RequestType getDistType(Player player) {
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

	public Distributor getDistributor(Player player) {
		Distributor dist = null;
		ServerConfig server = dao.getServerConfig();

		if (server.getGranularity() == ConfigGranularity.PLAYER)
			dist = player.getDistributor();
		else
			if (server.getGranularity() == ConfigGranularity.GROUP)
				dist = player.getGroup().getDistributor();

		// default fallback
		if (dist == null)
			dist = server.getMuConfig().getDistributor();

		return dist;
	}
	
	public ResourceQuality getAllowedWeaponQuality(Player player) {
		ResourceQuality q = null;
		ServerConfig server = dao.getServerConfig();

		if (server.getGranularity() == ConfigGranularity.PLAYER)
			q = player.getAllowedWeaponQuality();
		else
			if (server.getGranularity() == ConfigGranularity.GROUP)
				q = player.getGroup().getAllowedWeaponQuality();

		// default fallback
		if (q == null)
			q = server.getMuConfig().getAllowedWeaponQuality();

		return q;
	}
}
