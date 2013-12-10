package com.erep.dist.dwr;

import java.util.Date;
import java.util.List;

import javax.jdo.FetchPlan;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Employer;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.dwr.dto.RequestQuery;
import com.erep.dist.dwr.dto.WorkQuery;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class ReportService {

	@RemoteMethod
	public List<ResourceDist> requestDetails(Request request) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			Request dbRequest = dao.find(request);
			return dbRequest.getResources();
		}
	}

	@RemoteMethod
	public RequestQuery listRequests(String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().setMaxFetchDepth(-1);
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

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					eRepDay, cursor);

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

			pm.getFetchPlan().addGroup("reportListing");

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					dist, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public RequestQuery listRequestsByPlayer(Player player, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					player, cursor);

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

			RequestQuery queryResults = new RequestQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					group, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public WorkQuery listWorkByPlayer(Player player, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("workListing");

			WorkQuery queryResults = new WorkQuery();

			List<DailyWork> requests = new DataAccessObject(pm).listWork(
					player, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public WorkQuery listWorkByGroup(PlayerGroup group, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("workListing");

			WorkQuery queryResults = new WorkQuery();

			List<DailyWork> requests = new DataAccessObject(pm).listWork(group,
					cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public WorkQuery listWorkByEmployer(Employer employer, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("workListing");

			WorkQuery queryResults = new WorkQuery();

			List<DailyWork> requests = new DataAccessObject(pm).listWork(
					employer, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public WorkQuery listWorkByDay(Integer day, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("workListing");

			WorkQuery queryResults = new WorkQuery();

			List<DailyWork> requests = new DataAccessObject(pm).listWork(day,
					cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}
}
