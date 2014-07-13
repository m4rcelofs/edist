package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerStatus.ENABLED;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Extent;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.auth.LicensingHelper;
import com.erep.dist.data.Player;
import com.erep.dist.data.dao.Server;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class PlayerStatsStarterTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(PlayerStatsStarterTask.class
															.getName());

	private final Integer		day;

	public PlayerStatsStarterTask(Integer day) {
		this.day = day;
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			if (!new LicensingHelper(new Server(pm)).isValidLicence())
				return;

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

		TaskOptions taskOptions = withPayload(task).taskName(
				"PlayerStatsTask_" + dbPlayer.getErId() + "_" + day).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.addAsync(taskOptions);
	}
}
