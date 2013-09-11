package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerDistStatus.OK;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.FightVerification;
import com.erep.dist.data.Player;
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

			Player dbPlayer = dao.find(new Player(playerKey));
			DailyFights fights = dao.find(new DailyFights(dailyFightsKey));
			FightVerification verif = config.getFightVerification(dbPlayer);

			int fromDay = today - verif.getFightVerifDays();
			double requestedFF = dao.getRequestedFFSum(dbPlayer, fromDay);
			double fightsFF = dao.getFightsFFSum(dbPlayer, fromDay);

			double pctUsed = fightsFF / requestedFF;

			if (pctUsed < verif.getFightVerifPct()) {
				fights.setVerificationStatus(verif.getFightVerifAction().statusToSet);
				dbPlayer.setFightStatus(verif.getFightVerifAction().statusToSet);
			} else {
				fights.setVerificationStatus(OK);
				dbPlayer.setFightStatus(OK);
			}
		}
	}

}
