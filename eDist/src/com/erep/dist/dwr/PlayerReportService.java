package com.erep.dist.dwr;

import java.util.Date;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.PlayerIdentificator;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.dwr.dto.FightsQuery;
import com.erep.dist.dwr.dto.FiltersInitialInfo;
import com.erep.dist.dwr.dto.StatsQuery;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class PlayerReportService {

	@RemoteMethod
	public FiltersInitialInfo getInitialInfo() {
		FiltersInitialInfo dto = new FiltersInitialInfo();

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			dto.setGroups(dao.listGroups());
		}

		return dto;
	}

	@RemoteMethod
	public StatsQuery statsByPlayer(String idNameOrLink, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);
			StatsQuery queryResults = new StatsQuery();

			Player player = null;
			try {
				player = new PlayerIdentificator(dao)
						.identifyRequestString(idNameOrLink);
			} catch (JDOObjectNotFoundException e) {
				return queryResults;
			}

			pm.getFetchPlan().addGroup("statsListing");

			List<DailyStats> requests = dao.listStats(player, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public StatsQuery statsByDate(Date date, String encodedCursor) {
		Integer eRepDay = eRepTimeCalculator.toErepDate(date);
		return statsByDay(eRepDay, encodedCursor);
	}

	@RemoteMethod
	public StatsQuery statsByDay(Integer day, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("statsListing");

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
	public StatsQuery statsByGroup(PlayerGroup group, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("statsListing");

			StatsQuery queryResults = new StatsQuery();
			List<DailyStats> requests = new DataAccessObject(pm).listStats(
					group, encodedCursor);

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
