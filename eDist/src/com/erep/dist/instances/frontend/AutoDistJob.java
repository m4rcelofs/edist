package com.erep.dist.instances.frontend;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.RequestsHelper;
import com.erep.dist.auth.LicensingHelper;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;

public class AutoDistJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(AutoDistJob.class
															.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.log(Level.WARNING, "Starting autoDist job");

		int autoDist = 0;

//		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
//			DataAccessObject dao = new DataAccessObject(pm);
//
//			if (!new LicensingHelper(dao).isValidLicence())
//				return;
//
//			ConfigHelper config = new ConfigHelper(dao);
//			RequestsHelper requests = new RequestsHelper(dao);
//
//			List<Player> players = dao.listAllPlayers();
//
//			for (Player player : players) {
//				for (PlayerGroup group : player.getGroups())
//					if (config.getConfig(player, group).getAutoSend()) {
//						Transaction tx = pm.currentTransaction();
//						try {
//							tx.begin();
//							Request request = requests.createRequest(player,
//									group);
//							player.addRequest(request);
//							dao.save(request);
//							requests.addToQueue(request);
//							tx.commit();
//							autoDist++;
//						} finally {
//							if (tx.isActive()) {
//								tx.rollback();
//								logger.log(Level.SEVERE,
//										"Request for player {0} failed",
//										player.getName());
//							}
//						}
//					}
//			}
//
//		}

		logger.log(Level.WARNING, "AutoDist requested for {0} players",
				autoDist);
	}
}
