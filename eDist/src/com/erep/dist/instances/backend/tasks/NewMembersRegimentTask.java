package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.dao.Distributors;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.parser.MilitaryUnitParser;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class NewMembersRegimentTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(NewMembersRegimentTask.class
															.getName());
	private final String		muId;
	private final String		regimentId;

	protected NewMembersRegimentTask(String muId, String regimentId) {
		this.muId = muId;
		this.regimentId = regimentId;
	}

	@Override
	public void run() {
		logger.log(Level.WARNING, "Starting NewMembersRegimentTask for MU '"
				+ muId + "', regiment '" + regimentId + "'");

		if (regimentId == null || regimentId.trim().isEmpty())
			return;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			Distributor dist = new Distributors(pm).defaultDistributor();
			String[] members = getMembers(dist);

			int count = 0;
			for (String member : members) {
				addToQueue(member);
				count++;
			}
			logger.warning("Added " + count + " regiment members to queue");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String[] getMembers(Distributor dist) throws Exception {
		try (ErepScrapper scrapper = new ErepScrapper(dist.getEmail(),
				dist.decryptPassword())) {
			scrapper.login();
			String html = scrapper.scrapeRegimentMembers(muId,
					regimentId.trim());
			return new MilitaryUnitParser().parseMembers(html);
		}
	}

	private void addToQueue(String memberId) {
		DeferredTask task = new NewMembersCheckIfExistsTask(memberId);
		Queue queue = QueueFactory.getDefaultQueue();
		TaskOptions taskOptions = withPayload(task).taskName(
				"NewMembersCheckIfExistsTask_" + memberId).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.addAsync(taskOptions);
	}
}
