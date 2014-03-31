package com.erep.dist.instances.backend.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.PlayerStatsDaily;
import com.erep.dist.data.dao.Fights;
import com.erep.dist.data.dao.PlayerStatistics;
import com.erep.dist.data.dao.Stats;
import com.erep.dist.data.enums.PlayerStatus;
import com.google.appengine.api.taskqueue.DeferredTask;

public class PlayerDailyStatisticsTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(PlayerDailyStatisticsTask.class
															.getName());

	private final Integer		day;

	public PlayerDailyStatisticsTask(Integer day) {
		this.day = day;
	}

	@Override
	public void run() {

		logger.log(Level.WARNING, "Starting PlayerDailyStatisticsTask for day "
				+ day);

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			PlayerStatistics stats = new PlayerStatistics(pm);

			PlayerStatsDaily dailyStats = calculateStatistics(new Fights(pm),
					new Stats(pm));

			stats.save(dailyStats);
		}
		logger.log(Level.WARNING, "PlayerDailyStatisticsTask for day " + day
				+ " finished");
	}

	private PlayerStatsDaily calculateStatistics(Fights fights, Stats stats) {

		PlayerStatsDaily dailyStats = new PlayerStatsDaily();
		dailyStats.setDay(day);

		for (DailyFights fight : fights.getAllDaily(day)) {

			if (fight.getStats().getIsDead()) {
				dailyStats.addDead(fight.getPlayer());
				continue;
			}

			if (fight.getStats().getIsPermaBan())
				dailyStats.addPermaBanned(fight.getPlayer());

			if (fight.getStats().getIsTempBan())
				dailyStats.addTempBanned(fight.getPlayer());

			if (fight.getPlayer().getDbStatus() == PlayerStatus.ENABLED)
				dailyStats.addEnabled(fight.getStats());

			if (fight.getCalculatedRankDamage() > 0) {
				dailyStats.addFighter(fight.getStats());
				dailyStats.addInfluence(fight.getStats(),
						fight.getCalculatedRankDamage());
			} else {
				DailyStats today = fight.getStats();
				DailyStats previous = stats.find(today.getPlayer(), day - 1);
				if (previous != null && today.getXp() - previous.getXp() > 0) {
					dailyStats.addActive(today);
				}
			}
		}

		return dailyStats;
	}

}
