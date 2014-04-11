package com.erep.dist.data.dao;

import static com.erep.dist.erep.eRepTimeCalculator.toRLMonth;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.joda.time.YearMonth;

import com.erep.dist.data.AvailableStatistics;
import com.erep.dist.data.PlayerStatsDaily;
import com.erep.dist.data.PlayerStatsMonthly;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PlayerStatistics extends GenericDAO {

	public PlayerStatistics(PersistenceManager pm) {
		super(pm);
	}

	public PlayerStatsDaily find(PlayerStatsDaily stats) {
		return pm().getObjectById(PlayerStatsDaily.class, stats.getKey());
	}

	public PlayerStatsMonthly find(PlayerStatsMonthly stats) {
		return pm().getObjectById(PlayerStatsMonthly.class, stats.getKey());
	}

	public void save(PlayerStatsDaily daily) {
		Key dailyKey = createPlayersDailyKey(daily.getDay());
		daily.setKey(dailyKey);

		Transaction tx = pm().currentTransaction();
		try {
			tx.begin();
			PlayerStatsMonthly monthly = getPlayersMonthly(daily.getDay());
			monthly.addDaily(daily);
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();
		}
	}

	public AvailableStatistics getAvailable() {
		Key key = createAvailableKey();
		return pm().getObjectById(AvailableStatistics.class, key);
	}

	public PlayerStatsMonthly getPlayersMonthly(Integer day) {
		return getPlayersMonthly(toRLMonth(day));
	}

	public PlayerStatsMonthly getPlayersMonthly(YearMonth yearMonth) {
		Key monthlyKey = createPlayersMonthlyKey(yearMonth);
		try {
			return pm().getObjectById(PlayerStatsMonthly.class, monthlyKey);
		} catch (JDOObjectNotFoundException e) {
			return createMonthly(monthlyKey);
		}
	}

	private PlayerStatsMonthly createMonthly(Key monthlyKey) {
		PlayerStatsMonthly monthly = new PlayerStatsMonthly();
		monthly.setKey(monthlyKey);

		pm().makePersistent(monthly);
		getAvailable().addPlayerMonthly(monthly);

		return monthly;
	}

	protected Key createAvailableKey() {
		return KeyFactory.createKey(AvailableStatistics.class.getSimpleName(),
				1);
	}

	protected Key createPlayersDailyKey(Integer day) {
		return KeyFactory.createKey(createPlayersMonthlyKey(day),
				PlayerStatsDaily.class.getSimpleName(), day);
	}

	protected Key createPlayersMonthlyKey(Integer day) {
		return createPlayersMonthlyKey(toRLMonth(day));
	}

	protected Key createPlayersMonthlyKey(YearMonth month) {
		return KeyFactory.createKey(PlayerStatsMonthly.class.getSimpleName(),
				month.toString());
	}
}
