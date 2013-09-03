package com.erep.dist.servlets.frontend.install;

import com.erep.dist.ServerConstants;
import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Licence;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ConfigGranularity;
import com.erep.dist.data.enums.ReportsAccessLevel;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.google.appengine.api.datastore.KeyFactory;

public class InstallHelper {
	public static ServerConfig newDefaultConfig() {
		ServerConfig config = new ServerConfig();

		config.setKey(KeyFactory.createKey(ServerConfig.class.getSimpleName(),
				ServerConstants.DEFAULT_ID));
		config.setGranularity(ConfigGranularity.PLAYER);
		config.setMuName("  ");
		config.setReportsAccess(ReportsAccessLevel.MODS);

		DistConfig distConfig = new DistConfig();
		distConfig.setKey(KeyFactory.createKey(
				DistConfig.class.getSimpleName(), ServerConstants.DEFAULT_ID));
		distConfig.setAutoSend(false);
		distConfig.setCurrentMaxDailyFF(160);
		distConfig.setRocketAllowed(false);
		distConfig.setAllowedType(RequestType.WEAPONS_FOOD);
		distConfig.setAllowedWeaponQuality(ResourceQuality.Q7);

		Distributor distributor = new Distributor();
		distributor.setEmail("exemplo@exemplo.com");
		distributor.setPassword("");
		distributor.setKey(KeyFactory.createKey(
				Distributor.class.getSimpleName(), ServerConstants.DEFAULT_ID));

		distConfig.setDistributor(distributor);
		config.setMuConfig(distConfig);

		return config;
	}

	public static PlayerGroup newDefaultGroup() {
		PlayerGroup group = new PlayerGroup();

		group.setGroupName("Default");
		group.setKey(KeyFactory.createKey(PlayerGroup.class.getSimpleName(),
				ServerConstants.DEFAULT_ID));

		return group;
	}

	public static Licence newDefaultLicence() {
		Licence licence = new Licence();
		licence.setId(KeyFactory.createKey(Licence.class.getSimpleName(),
				ServerConstants.DEFAULT_ID));
		licence.setSigned("");
		return licence;
	}
}
