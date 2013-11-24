package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerDistStatus.OK;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.FightVerification;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.enums.PlayerDistStatus;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

public class FightsVerificationTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Integer		today;
	private final Key			playerKey;
	private final Key			dailyFightsKey;

	public FightsVerificationTask(Integer today, Player player,
			DailyFights dailyFights) {
		this.today = today;
		this.playerKey = player.getKey();
		this.dailyFightsKey = dailyFights.getKey();
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			ConfigHelper config = new ConfigHelper(dao);

			DailyFights fights = dao.find(new DailyFights(dailyFightsKey));
			Player dbPlayer = dao.find(new Player(playerKey));

			PlayerDistStatus statusToSet = OK;
			for (PlayerGroup group : dbPlayer.getGroups()) {
				FightVerification fightVerif = config.getFightVerification(
						dbPlayer, group);

				int fromDay = today - fightVerif.getFightVerifDays();
				double requestedFF = dao.getRequestedFFSum(dbPlayer, group,
						fromDay);
				double fightsFF = dao.getFightsFFSum(dbPlayer, fromDay);

				double pctUsed = fightsFF / requestedFF;

				if ((pctUsed * 100) < fightVerif.getFightVerifPct())
					if (fightVerif.getFightVerifAction().statusToSet.priority > statusToSet.priority)
						statusToSet = fightVerif.getFightVerifAction().statusToSet;

			}

			fights.setVerificationStatus(statusToSet);
			dbPlayer.setFightStatus(statusToSet);
		}
	}
}
