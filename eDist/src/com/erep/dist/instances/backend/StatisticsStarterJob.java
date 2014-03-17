package com.erep.dist.instances.backend;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.instances.backend.tasks.RequestDailyStatisticsTask;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class StatisticsStarterJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int today = eRepTimeCalculator.geteRepDay();
		DeferredTask requestDaily = new RequestDailyStatisticsTask(today - 1);
		addToQueue(requestDaily);
	}

	private void addToQueue(DeferredTask task) {
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");

		TaskOptions taskOptions = withPayload(task).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.add(taskOptions);
	}

}
