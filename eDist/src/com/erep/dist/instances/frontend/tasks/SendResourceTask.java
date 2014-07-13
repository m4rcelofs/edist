package com.erep.dist.instances.frontend.tasks;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import javax.servlet.http.HttpServletRequest;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.ResourceDistStatus;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.dist.FoodStrategy;
import com.erep.dist.erep.dist.ManufacturedStrategy;
import com.erep.dist.erep.dist.WeaponStrategy;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.DeferredTaskContext;

public class SendResourceTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(SendResourceTask.class
															.getName());

	private final Key			requestKey;
	private final Key			resourceKey;

	public SendResourceTask(Request request, ResourceDist resource) {
		this.requestKey = request.getKey();
		this.resourceKey = resource.getKey();
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			logger.log(Level.WARNING, "Starting SendResourceTask");

			Request dbRequest = dao.find(new Request(requestKey));
			ResourceDist dbResource = dao.find(new ResourceDist(resourceKey));

			if (dbRequest == null || dbResource == null
					|| dbResource.getStatus() == ResourceDistStatus.SENT) {
				logger.log(Level.WARNING,
						"Request not found or resource already sent, aborting");
				return;
			}

			HttpServletRequest req = DeferredTaskContext.getCurrentRequest();
			if (req != null) {
				Integer count = Integer.valueOf(req
						.getHeader("X-AppEngine-TaskExecutionCount"));
				if (count >= 200) {
					dbRequest.setStatus(RequestStatus.ERROR);
					dbResource.setStatus(ResourceDistStatus.ERROR);
					logger.log(Level.SEVERE,
							"Distribution task has been retrying for more then 200 times, aborting");
					return;
				}
			}

			checkDistributorAccess(pm, dbRequest.getDistributor());

			if (dbRequest.getPlayer().getNameNormalized()
					.equalsIgnoreCase(dbRequest.getDistributor().getName())) {
				dbRequest.setStatus(RequestStatus.ERROR);
				dbResource.setStatus(ResourceDistStatus.ERROR);
				logger.log(Level.WARNING,
						"Distributor is trying to send resources to itself, aborting");
				return;
			}

			PlayerStatus dbStatus = dbRequest.getPlayer().getDbStatus();
			if (dbStatus == PlayerStatus.ERROR
					|| dbStatus == PlayerStatus.DISABLED) {
				dbRequest.setStatus(RequestStatus.ERROR);
				dbResource.setStatus(ResourceDistStatus.ERROR);
				logger.log(Level.WARNING,
						"Player has errors or is disabled, aborting");
				return;
			}

			execute(dbRequest, dbResource);

			dbRequest.setStatus(RequestStatus.SENT);
			dbRequest.setCompletedAt(new Date());

			logger.log(Level.WARNING, "SendResourceTask completed successfully");

			setDistributorAccess(pm, dbRequest.getDistributor());

		} catch (Exception e) {
			// meh
			throw new RuntimeException(e);
		}
	}

	// TODO check DeferredTaskContext.markForRetry()
	private void checkDistributorAccess(PersistenceManager pm,
			Distributor distributor) {

		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();

			long lastAccess = distributor.getLastAccessTimestamp();
			long now = new Date().getTime();
			if (now < (lastAccess + 10000))
				throw new IllegalStateException(distributor.getEmail()
						+ " cooldown period, will retry later");
			// other requests might start while sending this one
			distributor.setLastAccessTimestamp(now);

			tx.commit();
		} catch (Exception e) {
			if (tx.isActive())
				tx.rollback();
			throw e;
		}
	}

	private void setDistributorAccess(PersistenceManager pm,
			Distributor distributor) {
		Transaction tx = pm.currentTransaction();
		tx.begin();
		distributor.setLastAccessTimestamp(new Date().getTime());
		tx.commit();
	}

	private void execute(Request dbRequest, ResourceDist dbResource)
			throws Exception {
		Distributor dist = dbRequest.getDistributor();
		String passwordPlain = dist.decryptPassword();

		try (ErepScrapper scrapper = new ErepScrapper(dist.getEmail(),
				passwordPlain)) {

			logger.log(Level.WARNING, "Logging in");
			scrapper.login();
			logger.log(Level.WARNING, "Logged in");

			logger.log(Level.WARNING, "Sending resource...");
			ManufacturedStrategy strategy = getStrategy(dbRequest, dbResource,
					scrapper);
			strategy.execute();
			logger.log(Level.WARNING, "Resource sent");

			dbResource.setStatus(ResourceDistStatus.SENT);
			dbRequest.getDistributor()
					.setLastInventory(strategy.getInventory());

		} catch (Exception e) {
			logger.log(Level.SEVERE, "Exception sending resource", e);
			dbResource.setStatus(ResourceDistStatus.ERROR);
			dbRequest.setStatus(RequestStatus.SENT_PARTIAL);
			throw e;
		}

	}

	private ManufacturedStrategy getStrategy(Request request,
			ResourceDist resource, ErepScrapper scrapper) throws Exception {
		switch (resource.getType()) {
			case WEAPONS:
				return new WeaponStrategy(resource, request.getPlayer(),
						scrapper);
			case FOOD:
				return new FoodStrategy(resource, request.getPlayer(),
						scrapper, request.getRequestedFF());
			default:
				logger.log(Level.WARNING,
						"Could not find strategy to send {0}",
						resource.getType());
				return null;
		}
	}
}
