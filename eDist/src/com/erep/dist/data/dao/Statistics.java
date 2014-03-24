package com.erep.dist.data.dao;

import static com.erep.dist.erep.eRepTimeCalculator.toRLMonth;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.joda.time.YearMonth;

import com.erep.dist.data.AvailableStatistics;
import com.erep.dist.data.RequestStatsDaily;
import com.erep.dist.data.RequestStatsMonthly;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Statistics extends GenericDAO {

	public Statistics(PersistenceManager pm) {
		super(pm);
	}

	public RequestStatsDaily find(RequestStatsDaily stats) {
		return pm().getObjectById(RequestStatsDaily.class, stats.getKey());
	}

	public RequestStatsMonthly find(RequestStatsMonthly stats) {
		return pm().getObjectById(RequestStatsMonthly.class, stats.getKey());
	}

	public void save(RequestStatsDaily daily) {
		Key dailyKey = createRequestsDailyKey(daily.getDay());
		daily.setKey(dailyKey);

		Transaction tx = pm().currentTransaction();
		try {
			tx.begin();
			RequestStatsMonthly monthly = getRequestsMonthly(daily.getDay());
			monthly.addDaily(daily);
			tx.commit();
		} finally {
			if (tx.isActive())
				tx.rollback();
		}
	}

	public AvailableStatistics getAvailable() {
		Key key = createAvailableKey();
		try {
			return pm().getObjectById(AvailableStatistics.class, key);
		} catch (JDOObjectNotFoundException e) {
			AvailableStatistics stats = new AvailableStatistics();
			stats.setKey(key);
			pm().makePersistent(stats);
			return stats;
		}
	}

	public RequestStatsMonthly getRequestsMonthly(Integer day) {
		return getRequestsMonthly(toRLMonth(day));
	}

	public RequestStatsMonthly getRequestsMonthly(YearMonth yearMonth) {
		Key monthlyKey = createRequestsMonthlyKey(yearMonth);
		try {
			return pm().getObjectById(RequestStatsMonthly.class, monthlyKey);
		} catch (JDOObjectNotFoundException e) {
			return createMonthly(monthlyKey);
		}
	}

	private RequestStatsMonthly createMonthly(Key monthlyKey) {
		RequestStatsMonthly monthly = new RequestStatsMonthly();
		monthly.setKey(monthlyKey);

		pm().makePersistent(monthly);
		getAvailable().addRequestMonthly(monthly);

		return monthly;
	}

	protected Key createAvailableKey() {
		return KeyFactory.createKey(AvailableStatistics.class.getSimpleName(),
				1);
	}

	protected Key createRequestsDailyKey(Integer day) {
		return KeyFactory.createKey(createRequestsMonthlyKey(day),
				RequestStatsDaily.class.getSimpleName(), day);
	}

	protected Key createRequestsMonthlyKey(Integer day) {
		return createRequestsMonthlyKey(toRLMonth(day));
	}

	protected Key createRequestsMonthlyKey(YearMonth month) {
		return KeyFactory.createKey(RequestStatsMonthly.class.getSimpleName(),
				month.toString());
	}
}
