package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.dao.Distributors;
import com.erep.dist.data.dao.Server;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.eRepURL;
import com.erep.dist.erep.parser.MilitaryUnitParser;
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;

public class NewMembersStarterTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(NewMembersStarterTask.class
															.getName());
	private final Integer		day;

	public NewMembersStarterTask(Integer day) {
		this.day = day;
	}

	@Override
	public void run() {
		logger.log(Level.WARNING, "Starting NewMembersStarterTask for day "
				+ day);

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			Server server = new Server(pm);
			String muId = server.getConfig().getSyncWithMuId();

			logger.log(Level.WARNING, "muId is '" + muId + "'");
			if (muId == null || muId.trim().isEmpty()) {
				logger.log(Level.WARNING,
						"muID is null or empty, cancelling sync");
				return;
			}

			Distributor dist = new Distributors(pm).defaultDistributor();
			String[] regiments = getRegiments(dist, muId);

			int count = 0;
			for (String regiment : regiments) {
				addToQueue(muId, regiment);
				count++;
			}
			logger.warning("Added " + count + " regiments to queue");

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private String[] getRegiments(Distributor dist, String muId)
			throws Exception {
		try (ErepScrapper scrapper = new ErepScrapper(dist.getEmail(),
				dist.decryptPassword())) {
			scrapper.login();
			String html = scrapper.scrape(eRepURL.MU_REGIMENTS.getUrl(muId
					.trim()));
			return new MilitaryUnitParser().parseGroups(html);
		}
	}

	private void addToQueue(String muId, String regiment) {
		DeferredTask task = new NewMembersRegimentTask(muId, regiment);
		Queue queue = QueueFactory.getQueue("erepublik-logged-in");
		TaskOptions taskOptions = withPayload(task).taskName(
				"NewMembersRegimentTask_" + muId + "_" + regiment).header(
				"Host",
				BackendServiceFactory.getBackendService().getBackendAddress(
						"edist-default-backend"));
		queue.addAsync(taskOptions);
	}
}
