package com.erep.dist.instances.frontend;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Employer;
import com.erep.dist.data.Player;
import com.erep.dist.instances.backend.tasks.WorkVerificationTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
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

			String encodedKey = jsObj.get("key").toString();
			String id = jsObj.get("id").toString();
			JSONArray employees = (JSONArray) jsObj.get("employees");

			checkParams(encodedKey, id);

			Employer dbEmployer = verifyEmployer(dao, encodedKey, id);
			createWorks(dao, dbEmployer, employees);

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

	private Employer verifyEmployer(DataAccessObject dao, String encodedKey,
			String id) {
		Employer emp = dao.find(new Employer(encodedKey));
		if (!emp.getErId().equals(id.trim()))
			throw new IllegalArgumentException("Invalid employer");
		return emp;
	}

	private void createWorks(DataAccessObject dao, Employer employer,
			JSONArray employees) {
		for (Object employeeObj : employees) {
			JSONObject employee = (JSONObject) employeeObj;
			String playerId = employee.get("id").toString();
			try {
				Player dbPlayer = dao.find(new Player(dao
						.createPlayerKey(playerId)));
				JSONArray works = (JSONArray) employee.get("work");

				createWorks(dao, employer, dbPlayer, works);
			} catch (JDOObjectNotFoundException e) {
				// some other player that works for the employer, just ignore
			}
		}
	}

	private void createWorks(DataAccessObject dao, Employer employer,
			Player player, JSONArray works) {
		for (Object workObj : works) {
			JSONObject workDay = (JSONObject) workObj;

			DailyWork work = new DailyWork();
			work.setDay(Integer.valueOf(workDay.get("day").toString()));
			work.setEmployer(employer);
			work.setGroup(player.getGroup());
			work.setPlayer(player);
			work.setKey(dao.createDailyWorkKey(player, work.getDay()));

			Object worked = workDay.get("worked");
			work.setWorked(worked != null ? Boolean.valueOf(worked.toString())
					: null);

			dao.save(work);

			if (worked != null)
				startWorkVerificationTask(player, work);
		}
	}

	private void startWorkVerificationTask(Player player, DailyWork work) {
		DeferredTask task = new WorkVerificationTask(player, work);
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.add(taskOptions);
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
