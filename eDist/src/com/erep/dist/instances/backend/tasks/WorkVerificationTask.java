package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerDistStatus.OK;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.WorkVerification;
import com.erep.dist.data.dao.Players;
import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

public class WorkVerificationTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Key			playerKey;
	private final Key			latestWorkKey;

	private static Logger		logger				= Logger.getLogger(WorkVerificationTask.class
															.getName());

	public WorkVerificationTask(Player player, DailyWork work) {
		this.playerKey = player.getKey();
		this.latestWorkKey = work.getKey();
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			Players players = new Players(pm);
			ConfigHelper config = new ConfigHelper(pm);

			Player dbPlayer = players.find(new Player(playerKey));

			logger.log(Level.WARNING, "Starting player work verification for "
					+ dbPlayer.getName());

			int today = eRepTimeCalculator.geteRepDay();

			PlayerDistStatus statusToSet = OK;
			for (PlayerGroup group : dbPlayer.getGroups()) {
				WorkVerification workVerif = config.getWorkVerification(
						dbPlayer, group);
				int fromDay = today - workVerif.getWorkVerifDays();

				List<DailyWork> works = dao.getWork(dbPlayer, fromDay);

				int worked = 0;
				int trackedTotal = 0;
				for (DailyWork work : works) {
					if (work.getWorked() != null) {
						if (work.getWorked())
							worked++;
						trackedTotal++;
					}
				}

				double pctWorked = trackedTotal != 0 ? worked
						/ (double) trackedTotal : 0;

				if ((pctWorked * 100) < workVerif.getMinWorkedDaysPct())
					if (workVerif.getWorkVerifAction().statusToSet.priority > statusToSet.priority)
						statusToSet = workVerif.getWorkVerifAction().statusToSet;
			}

			DailyWork dbWork = dao.find(new DailyWork(latestWorkKey));
			dbWork.setVerificationStatus(statusToSet);
			dbPlayer.setWorkStatus(statusToSet);

			logger.log(
					Level.WARNING,
					"Finished player work verification for "
							+ dbPlayer.getName() + ", setting status to "
							+ statusToSet.toString());

		}
	}
}
