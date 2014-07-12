package com.erep.dist.instances.frontend;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.IPLogger;
import com.erep.dist.PMF;
import com.erep.dist.data.Employer;
import com.erep.dist.instances.backend.tasks.WorkParseTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskAlreadyExistsException;
import com.google.appengine.api.taskqueue.TaskOptions;

public class EmployerFacadeServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(EmployerFacadeServlet.class
															.getName());

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			String param = req.getParameter("json");
			checkParams(param);

			logger.log(Level.WARNING, "Received update from employer: " + param);

			JSONParser parser = new JSONParser();
			JSONObject jsObj = (JSONObject) parser.parse(param);

			String authKey = jsObj.get("key").toString();
			String id = jsObj.get("id").toString();
			JSONArray employees = (JSONArray) jsObj.get("employees");

			checkParams(authKey, id);

			Employer dbEmployer = verifyEmployer(dao, authKey, id);

			saveStats((JSONObject) jsObj.get("employeeStats"), dbEmployer);

			dbEmployer.resetPlayers();
			createWorks(dbEmployer, employees);

			WebContext ctx = WebContextFactory.get();
			new IPLogger(ctx.getHttpServletRequest()).setIP(dbEmployer);
			
			dbEmployer.setLastUpdatedAt(new Date());

		} catch (NullPointerException | ParseException e) {
			logger.log(Level.WARNING, "Something wrong on EmployerFacade", e);
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} catch (JDOObjectNotFoundException | IllegalArgumentException e) {
			logger.log(Level.WARNING, "Something wrong on EmployerFacade", e);
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
	}

	private void saveStats(JSONObject jsStats, Employer dbEmployer) {
		dbEmployer.setEmployeesCount(Integer.valueOf(jsStats.get("count")
				.toString()));
		dbEmployer.setJobsOpen(Integer.valueOf(jsStats.get("open").toString()));
		dbEmployer.setJobsTotal(Integer
				.valueOf(jsStats.get("total").toString()));

		String jobsSalary = jsStats.get("salary").toString().trim();
		if (jobsSalary.length() == 0 || Character.isDigit(jobsSalary.charAt(0)))
			dbEmployer.setJobsSalary(jobsSalary);
	}

	private Employer verifyEmployer(DataAccessObject dao, String authKey,
			String id) {
		Employer emp = dao.find(new Employer(dao.createEmployerKey(id)));
		if (!emp.getErId().equals(id.trim()) || !emp.isAuthKeyValid(authKey))
			throw new IllegalArgumentException("Invalid employer");
		return emp;
	}

	private void createWorks(Employer dbEmployer, JSONArray employees) {

		int tasksCreated = 0;
		int tasksFailed = 0;

		for (Object employeeObj : employees) {
			JSONObject employee = (JSONObject) employeeObj;
			boolean ok = startWorkParseTask(dbEmployer, employee);
			if (ok)
				tasksCreated++;
			else
				tasksFailed++;
		}

		logger.log(Level.WARNING,
				"Finished employer update for " + dbEmployer.getName() + ": "
						+ tasksCreated + " tasks created, " + tasksFailed
						+ " tasks failed at creation.");
	}

	private boolean startWorkParseTask(Employer dbEmployer,
			JSONObject employeeUpdate) {
		DeferredTask task = new WorkParseTask(dbEmployer, employeeUpdate);
		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend")).taskName(
				"WorkParseTask_" + employeeUpdate.get("id").toString() + "_"
						+ new Date().getTime());
		try {
			Queue queue = QueueFactory.getDefaultQueue();
			queue.add(taskOptions);
			return true;
		} catch (TaskAlreadyExistsException e) {
			// multiple requests going on, ignore
			return false;
		}
	}

	private boolean checkParams(String... params) {
		for (String param : params)
			if (param == null || param.isEmpty()
					|| param.equalsIgnoreCase("null"))
				throw new IllegalArgumentException(
						"Argument cannot be null or empty");
		return true;
	}

}
