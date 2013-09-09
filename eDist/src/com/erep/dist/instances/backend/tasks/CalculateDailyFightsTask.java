package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import javax.jdo.JDOObjectNotFoundException;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.erep.HitCalculator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class CalculateDailyFightsTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Integer		today;
	private final Key			playerKey;

	public CalculateDailyFightsTask(Integer today, Key playerKey) {
		this.today = today;
		this.playerKey = playerKey;
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			ConfigHelper config = new ConfigHelper(dao);

			Player dbPlayer = dao.find(new Player(playerKey));
			DailyStats previousDayStats = dao.getStats(dbPlayer, today - 1);
			DailyStats todayStats = dao.getStats(dbPlayer, today);

			ResourceQuality quality = config.getAllowedWeaponQuality(dbPlayer);

			DailyFights fights = calculateFights(today, dbPlayer,
					previousDayStats, todayStats, quality);
			fights.setGroup(dbPlayer.getGroup());

			dbPlayer.addDailyFights(fights);

			createFightVerificationTask(dbPlayer);

		} catch (JDOObjectNotFoundException e) {
			// first scrapping today, ignore
		}
	}

	private DailyFights calculateFights(Integer today, Player dbPlayer,
			DailyStats previousDayStats, DailyStats todayStats,
			ResourceQuality quality) {

		DailyFights fights = new DailyFights(createKey(dbPlayer, today));

		fights.setDay(today);
		fights.setCalculatedWithWeaponQuality(quality);

		int calculatedFights = calculateFights(previousDayStats, todayStats,
				quality);

		fights.setCalculatedFfFights(calculatedFights);

		return fights;
	}

	private int calculateFights(DailyStats previousDayStats,
			DailyStats todayStats, ResourceQuality quality) {

		int hit = todayStats.getMaxHit();
		if (quality != ResourceQuality.maxQuality())
			hit = new HitCalculator().calculateHit(todayStats, quality);

		long rankChange = todayStats.getRank() - previousDayStats.getRank();
		long dailyInfluence = rankChange * 10;

		return (int) (dailyInfluence / hit);
	}

	private void createFightVerificationTask(Player dbPlayer) {
		DeferredTask task = new FightsVerificationTask(dbPlayer.getKey());
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");
		queue.add(withPayload(task));
	}

	private Key createKey(Player dbPlayer, Integer day) {
		return KeyFactory.createKey(dbPlayer.getKey(),
				DailyFights.class.getSimpleName(), day);
	}
}
