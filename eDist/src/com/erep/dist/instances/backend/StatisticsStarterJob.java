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
import com.erep.dist.instances.backend.tasks.PlayerDailyStatisticsTask;
import com.erep.dist.instances.backend.tasks.RequestDailyStatisticsTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class StatisticsStarterJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(StatisticsStarterJob.class
															.getName());

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		int day = eRepTimeCalculator.geteRepDay() - 1;

		String dayParam = req.getParameter("day");
		if (dayParam != null && !dayParam.isEmpty())
			day = Integer.valueOf(dayParam);

		DeferredTask requestDaily = new RequestDailyStatisticsTask(day);
		addToQueue(requestDaily);

		DeferredTask playersDaily = new PlayerDailyStatisticsTask(day);
		addToQueue(playersDaily);

		logger.log(Level.WARNING,
				"Started RequestDailyStatisticsTask and PlayerDailyStatisticsTask for day "
						+ day);
	}

	private void addToQueue(DeferredTask task) {
		Queue queue = QueueFactory.getDefaultQueue();

		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.addAsync(taskOptions);
	}

}
