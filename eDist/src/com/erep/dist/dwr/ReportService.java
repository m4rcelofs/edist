package com.erep.dist.dwr;

import java.util.Date;
import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;
import com.erep.dist.dwr.dto.ReportQuery;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class ReportService {

	@RemoteMethod
	public ReportQuery listRequests(String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			ReportQuery queryResults = new ReportQuery();
			List<Request> requests = new DataAccessObject(pm)
					.listRequests(cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public ReportQuery listRequestsByDate(Date date, String cursor) {
		Integer eRepDay = eRepTimeCalculator.toErepDate(date);
		return listRequestsByDay(eRepDay, cursor);
	}

	@RemoteMethod
	public ReportQuery listRequestsByDay(Integer eRepDay, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			ReportQuery queryResults = new ReportQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					eRepDay, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public ReportQuery listRequestsByDistributor(Distributor dist, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			ReportQuery queryResults = new ReportQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					dist, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public ReportQuery listRequestsByPlayer(Player player, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			ReportQuery queryResults = new ReportQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					player, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

	@RemoteMethod
	public ReportQuery listRequestsByGroup(PlayerGroup group, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("reportListing");

			ReportQuery queryResults = new ReportQuery();

			List<Request> requests = new DataAccessObject(pm).listRequests(
					group, cursor);

			queryResults.setResults(requests);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(requests)
					.toWebSafeString());
			return queryResults;
		}
	}

}
