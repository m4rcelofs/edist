package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerDistStatus.OK;

import java.util.List;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.WorkVerification;
import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

public class WorkVerificationTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Key			playerKey;
	private final Key			latestWorkKey;

	public WorkVerificationTask(Player player, DailyWork work) {
		this.playerKey = player.getKey();
		this.latestWorkKey = work.getKey();
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			ConfigHelper config = new ConfigHelper(dao);

			Player dbPlayer = dao.find(new Player(playerKey));

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

		}
	}
}
