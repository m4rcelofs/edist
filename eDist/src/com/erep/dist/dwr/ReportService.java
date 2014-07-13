package com.erep.dist.dwr;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.jdo.JDOObjectNotFoundException;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;
import org.joda.time.YearMonth;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.PlayerIdentificator;
import com.erep.dist.auth.AuthHelper;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Employer;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;
import com.erep.dist.data.RequestStatsDaily;
import com.erep.dist.data.RequestStatsMonthly;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.dao.RequestStatistics;
import com.erep.dist.data.dao.Server;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.dwr.dto.FightsQuery;
import com.erep.dist.dwr.dto.FiltersInitialInfo;
import com.erep.dist.dwr.dto.RequestQuery;
import com.erep.dist.dwr.dto.WorkQuery;
import com.erep.dist.dwr.dto.WorkWeekQuery;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class ReportService {

	@RemoteMethod
	public FiltersInitialInfo getInitialInfo() {
		FiltersInitialInfo dto = new FiltersInitialInfo();

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			AuthHelper auth = new AuthHelper(new Server(pm));
			if (auth.hasManagementAccess())
				dto.setDistributors(dao.listDistributors());

			dto.setEmployers(dao.listEmployers());
			dto.setGroups(dao.listGroups());
		}

		return dto;
	}

	@RemoteMethod
	public List<ResourceDist> requestDetails(Request request) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			Request dbRequest = dao.find(request);
			return dbRequest.getResources();
		}
	}

	// @RemoteMethod
	public RequestQuery listRequests(String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			RequestQuery queryResults = new RequestQuery();
			List<Request> requests = new DataAccessObject(pm)
					.listRequests(cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByDate(Date date, String cursor) {
		Integer eRepDay = eRepTimeCalculator.toErepDate(date);
		return listRequestsByDay(eRepDay, cursor);
	}

	@RemoteMethod
	public RequestQuery listRequestsByDay(Integer eRepDay, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			DataAccessObject dao = new DataAccessObject(pm);
			AuthHelper auth = new AuthHelper(new Server(pm));
			if (auth.hasManagementAccess())
				pm.getFetchPlan().addGroup("managementAccess");

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = dao.listRequests(eRepDay, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByDistributor(Distributor dist,
			String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			RequestQuery queryResults = new RequestQuery();

			DataAccessObject dao = new DataAccessObject(pm);
			AuthHelper auth = new AuthHelper(new Server(pm));
			if (!auth.hasManagementAccess())
				return queryResults;

			pm.getFetchPlan().addGroup("reportListing");
			pm.getFetchPlan().addGroup("managementAccess");

			List<Request> requests = dao.listRequests(dist, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByPlayer(String idNameOrLink, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			RequestQuery queryResults = new RequestQuery();
			DataAccessObject dao = new DataAccessObject(pm);

			Player player = null;
			try {
				player = new PlayerIdentificator(dao)
						.identifyRequestString(idNameOrLink);
			} catch (JDOObjectNotFoundException e) {
				return queryResults;
			}

			pm.getFetchPlan().addGroup("reportListing");

			AuthHelper auth = new AuthHelper(new Server(pm));
			if (auth.hasManagementAccess())
				pm.getFetchPlan().addGroup("managementAccess");

			List<Request> requests = dao.listRequests(player, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByGroup(PlayerGroup group, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			DataAccessObject dao = new DataAccessObject(pm);
			AuthHelper auth = new AuthHelper(new Server(pm));
			if (auth.hasManagementAccess())
				pm.getFetchPlan().addGroup("managementAccess");

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = dao.listRequests(group, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByStatus(RequestStatus status, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			DataAccessObject dao = new DataAccessObject(pm);
			AuthHelper auth = new AuthHelper(new Server(pm));
			if (auth.hasManagementAccess())
				pm.getFetchPlan().addGroup("managementAccess");

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = dao.listRequests(status, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByType(RequestType type, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			DataAccessObject dao = new DataAccessObject(pm);
			AuthHelper auth = new AuthHelper(new Server(pm));
			if (auth.hasManagementAccess())
				pm.getFetchPlan().addGroup("managementAccess");

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = dao.listRequests(type, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public WorkQuery listWorkByPlayer(String idNameOrLink, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);
			WorkQuery queryResults = new WorkQuery();

			Player player = null;
			try {
				player = new PlayerIdentificator(dao)
						.identifyRequestString(idNameOrLink);
			} catch (JDOObjectNotFoundException e) {
				return queryResults;
			}

			pm.getFetchPlan().addGroup("workListing");

			List<DailyWork> requests = dao.listWork(player, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public WorkWeekQuery listWorkByGroup(PlayerGroup group, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("workWeekListing");

			WorkWeekQuery queryResults = new WorkWeekQuery();

			DataAccessObject dao = new DataAccessObject(pm);

			List<Player> players = dao.listPlayersByGroup(group, cursor);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(players)
					.toWebSafeString());

			queryResults.setResults(dao.getWeeklyWorks(players));

			return queryResults;
		}
	}

	@RemoteMethod
	public WorkWeekQuery listWorkByEmployer(Employer employer, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("workWeekListing");

			WorkWeekQuery queryResults = new WorkWeekQuery();

			DataAccessObject dao = new DataAccessObject(pm);

			List<Player> players = dao.listPlayersByEmployer(employer, cursor);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(players)
					.toWebSafeString());

			queryResults.setResults(dao.getWeeklyWorks(players));

			return queryResults;
		}
	}

	@RemoteMethod
	public FightsQuery fightsByPlayer(String idNameOrLink, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			FightsQuery queryResults = new FightsQuery();
			DataAccessObject dao = new DataAccessObject(pm);

			Player player = null;
			try {
				player = new PlayerIdentificator(dao)
						.identifyRequestString(idNameOrLink);
			} catch (JDOObjectNotFoundException e) {
				return queryResults;
			}

			pm.getFetchPlan().addGroup("fightsListing");

			List<DailyFights> requests = dao.listFights(player, encodedCursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public FightsQuery fightsByDate(Date date, String encodedCursor) {
		Integer day = eRepTimeCalculator.toErepDate(date);
		return fightsByDay(day, encodedCursor);
	}

	@RemoteMethod
	public FightsQuery fightsByDay(Integer day, String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("fightsListing");

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
			pm.getFetchPlan().addGroup("fightsListing");

			FightsQuery queryResults = new FightsQuery();

			List<DailyFights> requests = new DataAccessObject(pm).listFights(
					group, encodedCursor);

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
			return stats.getAvailable().getRequestMonthly();
		}
	}

	@RemoteMethod
	public RequestStatsMonthly getRequestStats(String yearMonth) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("monthlyListing");
			RequestStatistics stats = new RequestStatistics(pm);
			RequestStatsMonthly result = stats.getRequestsMonthly(new YearMonth(yearMonth));
			return pm.detachCopy(result);
		}
	}

	@RemoteMethod
	public Map<ResourceType, Map<ResourceQuality, Integer>> loadDailyDetails(
			RequestStatsDaily daily) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			RequestStatistics statistics = new RequestStatistics(pm);
			daily = statistics.find(daily);
			return daily.getItemsSent().rowMap();
		}
	}

	@RemoteMethod
	public Map<ResourceType, Map<ResourceQuality, Integer>> loadMonthlyDetails(
			RequestStatsMonthly monthly) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			RequestStatistics statistics = new RequestStatistics(pm);
			monthly = statistics.find(monthly);
			return monthly.getItemsSent().rowMap();
		}
	}

}
