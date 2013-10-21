package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerDistStatus.OK;

import java.util.List;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Player;
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

			DistConfig distConfig = config.getConfig(dbPlayer);

			int today = eRepTimeCalculator.geteRepDay();
			int fromDay = today - distConfig.getWorkVerifDays();

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
					/ (double) trackedTotal : 1;

			DailyWork dbWork = dao.find(new DailyWork(latestWorkKey));
			if (pctWorked < distConfig.getMinWorkedDaysPct()) {
				dbWork.setVerificationStatus(distConfig.getWorkVerifAction().statusToSet);
				dbPlayer.setWorkStatus(distConfig.getWorkVerifAction().statusToSet);
			} else {
				dbWork.setVerificationStatus(OK);
				dbPlayer.setWorkStatus(OK);
			}
		}
	}
}
