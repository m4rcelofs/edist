package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Employer;
import com.erep.dist.data.Player;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskAlreadyExistsException;
import com.google.appengine.api.taskqueue.TaskOptions;

public class WorkParseTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(WorkParseTask.class
															.getName());

	private final Key			employerKey;
	private final JSONObject	employeeUpdate;

	private int					tasksCreated		= 0;
	private int					tasksFailed			= 0;
	private int					tasksIgnored		= 0;

	public WorkParseTask(Employer employer, JSONObject employeeUpdate) {
		this.employerKey = employer.getKey();
		this.employeeUpdate = employeeUpdate;
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			String playerId = employeeUpdate.get("id").toString();

			logger.log(Level.WARNING, "Starting player work parse for "
					+ playerId);

			Employer dbEmployer = dao.find(new Employer(employerKey));

			Player dbPlayer = null;
			try {
				dbPlayer = dao.find(new Player(dao.createPlayerKey(playerId)));
			} catch (JDOObjectNotFoundException e) {
				// some other player that works for the employer, just ignore
				logger.log(Level.WARNING, "Player " + playerId
						+ " is not registered");
				return;
			}

			dbEmployer.addPlayer(dbPlayer);
			dbPlayer.setCurrentEmployer(dbEmployer);

			JSONArray works = (JSONArray) employeeUpdate.get("work");

			logger.log(Level.WARNING, "Work received for " + dbPlayer.getName()
					+ ": " + works.toString());

			createWorks(dao, dbEmployer, dbPlayer, works);

			logger.log(Level.WARNING, "Finished player work parse for "
					+ dbPlayer.getName() + ": " + tasksCreated
					+ " tasks created, " + tasksFailed
					+ " tasks failed at creation, " + tasksIgnored
					+ " tasks ignored");

		}
	}

	private void createWorks(DataAccessObject dao, Employer employer,
			Player player, JSONArray works) {
		logger.log(Level.WARNING, "Work info exists for " + works.size()
				+ " days");
		for (Object workObj : works) {
			JSONObject workDay = (JSONObject) workObj;
			createWork(dao, employer, player, workDay);
		}
	}

	private void createWork(DataAccessObject dao, Employer employer,
			Player player, JSONObject workDay) {

		Integer day = Integer.valueOf(workDay.get("day").toString());

		DailyWork work = new DailyWork(player, day);
		work.setEmployer(employer);
		work.setGroups(player.getGroups());
		work.setPlayer(player);

		try {
			work.setStats(dao.find(new DailyStats(player, day - 1)));
		} catch (JDOObjectNotFoundException e) {
			logger.log(Level.WARNING,
					"Status not found for work on day " + day, e);
		}

		Object worked = workDay.get("worked");
		work.setWorked(worked != null ? Boolean.valueOf(worked.toString())
				: null);

		dao.save(work);

		logger.log(Level.WARNING,
				"Day " + day + " saved for player " + player.getName()
						+ ", worked: " + worked);

		if (worked != null)
			startWorkVerificationTask(player, work);
		else
			tasksIgnored++;
	}

	private boolean startWorkVerificationTask(Player player, DailyWork work) {
		DeferredTask task = new WorkVerificationTask(player, work);
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions taskOptions = withPayload(task).taskName(
				"WorkVerificationTask_" + player.getErId() + "_"
						+ work.getDay());
		try {
			queue.add(taskOptions);
			tasksCreated++;
			return true;
		} catch (TaskAlreadyExistsException e) {
			tasksFailed++;
			return false;
		}
	}

}
