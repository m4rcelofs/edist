package com.erep.dist.instances.backend;

import static com.erep.dist.data.enums.PlayerStatus.ENABLED;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.auth.LicensingHelper;
import com.erep.dist.data.Player;
import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.instances.backend.tasks.PlayerStatsTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class PlayerStatsStarterJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(PlayerStatsStarterJob.class
															.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			
			if (!new LicensingHelper(dao).isValidLicence())
				return;
			
			Integer day = eRepTimeCalculator.geteRepDay();
			Extent<Player> players = dao.listAllPlayers();
			
			int count = 0;
			for (Player dbPlayer : players)
				if (dbPlayer.getDbStatus() == ENABLED) {
					startUpdateTask(day, dbPlayer);
					count++;
				}
			
			players.closeAll();
			logger.log(Level.WARNING, "Starting stats refresh for {0} players",
					count);
		}
	}

	private void startUpdateTask(Integer day, Player dbPlayer) {
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");
		DeferredTask task = new PlayerStatsTask(day, dbPlayer);

		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.add(taskOptions);
	}

}
