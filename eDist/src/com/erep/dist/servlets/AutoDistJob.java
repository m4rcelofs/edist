package com.erep.dist.servlets;

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
import com.erep.dist.data.Player;

public class AutoDistJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(AutoDistJob.class
															.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.log(Level.WARNING, "Starting autoDist job");

		int autoDist = 0;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			ConfigHelper config = new ConfigHelper(dao);
			RequestsHelper requests = new RequestsHelper(dao);

			List<Player> players = dao.listAllPlayers();

			for (Player player : players) {
				if (config.getConfigAutoSend(player)) {
					Transaction tx = pm.currentTransaction();
					try {
						tx.begin();
						requests.createRequest(player);
						tx.commit();
					} finally {
						if (tx.isActive())
							tx.rollback();
					}
					autoDist++;
				}
			}

		}

		logger.log(Level.WARNING, "AutoDist requested for {0} players",
				autoDist);
	}
}
