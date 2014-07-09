package com.erep.dist.instances.backend;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.instances.backend.tasks.PlayerStatsStarterTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class PlayerStatsStarterJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(PlayerStatsStarterJob.class
															.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");

		Integer day = eRepTimeCalculator.geteRepDay();
		DeferredTask task = new PlayerStatsStarterTask(day);

		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.addAsync(taskOptions);
		logger.log(Level.WARNING, "Started PlayerStatsStarterTask for day "
				+ day);
	}
}
