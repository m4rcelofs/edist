package com.erep.dist.dwr;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.dwr.dto.FightsQuery;
import com.erep.dist.dwr.dto.StatsQuery;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class PlayerReportService {

	@RemoteMethod
	public StatsQuery statsByPlayer(Player player, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			StatsQuery queryResults = new StatsQuery();

			List<DailyStats> requests = new DataAccessObject(pm).listStats(
					player, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public StatsQuery statsByDay(Integer day, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			StatsQuery queryResults = new StatsQuery();

			List<DailyStats> requests = new DataAccessObject(pm).listStats(day,
					encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public FightsQuery fightsByPlayer(Player player, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("fightsByPlayer");

			FightsQuery queryResults = new FightsQuery();

			List<DailyFights> requests = new DataAccessObject(pm).listFights(
					player, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public FightsQuery fightsByDay(Integer day, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("fightsByDay");

			FightsQuery queryResults = new FightsQuery();

			List<DailyFights> requests = new DataAccessObject(pm).listFights(
					day, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public FightsQuery fightsByGroup(PlayerGroup group, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("fightsByGroup");

			FightsQuery queryResults = new FightsQuery();

			List<DailyFights> requests = new DataAccessObject(pm).listFights(
					group, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

}
