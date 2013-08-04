package com.erep.dist.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.ServerConstants;
import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ConfigGranularity;
import com.google.appengine.api.datastore.KeyFactory;

public class InstallServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			try {
				pm.getObjectById(ServerConfig.class, ServerConstants.DEFAULT);
				resp.getWriter().write("Already installed!");
				return;
			} catch (JDOObjectNotFoundException e) {
				resp.getWriter().write("Installing, please wait... \n");
				long init = new Date().getTime();
				install(pm);
				long time = new Date().getTime() - init;
				resp.getWriter().write(
						"Installed successfully in " + time + "ms (:");
			}
		}

	}

	private void install(PersistenceManager pm) {
		pm.makePersistent(defaultGroup());
		pm.makePersistent(defaultConfig());
	}

	private PlayerGroup defaultGroup() {
		PlayerGroup group = new PlayerGroup();

		group.setGroupName("Default");
		group.setKey(KeyFactory.createKey(PlayerGroup.class.getSimpleName(),
				ServerConstants.DEFAULT));
		group.setPlayers(new HashSet<Player>());

		return group;
	}

	private ServerConfig defaultConfig() {
		ServerConfig config = new ServerConfig();

		config.setKey(KeyFactory.createKey(ServerConfig.class.getSimpleName(),
				ServerConstants.DEFAULT));
		config.setGranularity(ConfigGranularity.MU);
		config.setMuName("  ");

		DistConfig distConfig = new DistConfig();
		distConfig.setKey(KeyFactory.createKey(DistConfig.class.getSimpleName(),
				ServerConstants.DEFAULT));
		distConfig.setAutoSend(false);
		distConfig.setCurrentMaxDailyFF(160);
		distConfig.setRocketAllowed(false);

		Distributor distributor = new Distributor();
		distributor.setEmail("exemplo@exemplo.com");
		distributor.setPassword("");
		distributor.setKey(KeyFactory.createKey(
				Distributor.class.getSimpleName(), ServerConstants.DEFAULT));

		distConfig.setDistributor(distributor);
		config.setMuConfig(distConfig);

		return config;
	}
}
