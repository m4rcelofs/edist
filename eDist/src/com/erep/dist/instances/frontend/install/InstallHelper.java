package com.erep.dist.instances.frontend.install;

import com.erep.dist.ServerConstants;
import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.DistributorRoot;
import com.erep.dist.data.EmployerRoot;
import com.erep.dist.data.Licence;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.PlayerGroupRoot;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ConfigGranularity;
import com.erep.dist.data.enums.ReportsAccessLevel;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class InstallHelper {
	public static ServerConfig newDefaultConfig() {
		ServerConfig config = new ServerConfig();

		config.setKey(KeyFactory.createKey(ServerConfig.class.getSimpleName(),
				ServerConstants.DEFAULT_ID));
		config.setGranularity(ConfigGranularity.GROUP);
		config.setMuName("  ");
		config.setReportsAccess(ReportsAccessLevel.MODS);
		config.setPlayerReportsAccess(ReportsAccessLevel.MODS);

		DistConfig distConfig = new DistConfig();
		distConfig.setKey(KeyFactory.createKey(
				DistConfig.class.getSimpleName(), ServerConstants.DEFAULT_ID));
		distConfig.setAutoSend(false);
		distConfig.setCurrentMaxDailyFF(160);
		distConfig.setRocketAllowed(false);
		distConfig.setAllowedType(RequestType.WEAPONS_FOOD);
		distConfig.setAllowedWeaponQuality(ResourceQuality.Q7);
		distConfig.setDistributor(defaultDistributor());
		config.setMuConfig(distConfig);

		return config;
	}

	private static Distributor defaultDistributor() {
		Distributor distributor = new Distributor();
		distributor.setEmail("exemplo@exemplo.com");
		distributor.setPassword("");
		distributor.setKey(KeyFactory.createKey(distributorRootKey(),
				Distributor.class.getSimpleName(), ServerConstants.DEFAULT_ID));
		return distributor;
	}

	public static DistributorRoot newDistributorRoot() {
		DistributorRoot root = new DistributorRoot();
		root.setKey(distributorRootKey());
		return root;
	}

	private static Key distributorRootKey() {
		return KeyFactory.createKey(DistributorRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	public static EmployerRoot newEmployerRoot() {
		EmployerRoot root = new EmployerRoot();
		root.setKey(employerRootKey());
		return root;
	}

	private static Key employerRootKey() {
		return KeyFactory.createKey(EmployerRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	public static PlayerGroupRoot newPlayerGroupRoot() {
		PlayerGroupRoot root = new PlayerGroupRoot();
		root.setKey(groupRootKey());
		return root;
	}

	private static Key groupRootKey() {
		return KeyFactory.createKey(PlayerGroupRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	public static PlayerGroup newDefaultGroup() {
		PlayerGroup group = new PlayerGroup();

		group.setGroupName("Default");
		group.setKey(KeyFactory.createKey(groupRootKey(),
				PlayerGroup.class.getSimpleName(), ServerConstants.DEFAULT_ID));

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
