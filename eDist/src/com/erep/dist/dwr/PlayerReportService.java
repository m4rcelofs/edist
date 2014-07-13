package com.erep.dist.dwr;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.joda.time.YearMonth;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.PlayerIdentificator;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.PlayerStatsMonthly;
import com.erep.dist.data.dao.PlayerStatistics;
import com.erep.dist.data.dao.RequestStatistics;
import com.erep.dist.data.enums.Division;
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
				player = new PlayerIdentificator(pm)
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
	public StatsQuery statsByGroup(Integer day, PlayerGroup group,
			String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("statsListing");

			StatsQuery queryResults = new StatsQuery();
			List<DailyStats> requests = new DataAccessObject(pm).listStats(day,
					group, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public StatsQuery statsByDivision(Integer day, Division division,
			String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("statsListing");

			StatsQuery queryResults = new StatsQuery();
			List<DailyStats> requests = new DataAccessObject(pm).listStats(day,
					division, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public Set<String> getAvailableRequestStats() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			RequestStatistics stats = new RequestStatistics(pm);
			return stats.getAvailable().getPlayersMonthly();
		}
	}

	@RemoteMethod
	public PlayerStatsMonthly getPlayerStats(String yearMonth) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("monthlyListing");
			PlayerStatistics stats = new PlayerStatistics(pm);
			PlayerStatsMonthly result = stats.getPlayersMonthly(new YearMonth(
					yearMonth));
			return pm.detachCopy(result);
		}
	}
}
