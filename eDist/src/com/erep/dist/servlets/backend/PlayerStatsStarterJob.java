package com.erep.dist.servlets.backend;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.servlets.backend.tasks.PlayerStatsTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class PlayerStatsStarterJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			Integer day = eRepTimeCalculator.geteRepDay();
			for (Player dbPlayer : dao.listAllPlayers())
				if (dbPlayer.getDbStatus() == PlayerStatus.ENABLED)
					startUpdateTask(day, dbPlayer);
		}
	}

	private void startUpdateTask(Integer day, Player dbPlayer) {
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");
		DeferredTask task = new PlayerStatsTask(day, dbPlayer.getKey());

		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.add(taskOptions);
	}

}
