package com.erep.dist.instances.frontend.tasks;

import static com.erep.dist.auth.SymmetricCipherUtils.decrypt;
import static com.erep.dist.data.enums.RequestStatus.ERROR;
import static com.erep.dist.data.enums.RequestStatus.SENT;
import static com.erep.dist.data.enums.RequestStatus.SENT_PARTIAL;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.Transaction;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.ResourceDistStatus;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.dist.DistStrategy;
import com.erep.dist.erep.dist.FoodStrategy;
import com.erep.dist.erep.dist.WeaponStrategy;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

public class SendResourceTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private static final Logger	logger				= Logger.getLogger(SendResourceTask.class
															.getName());

	private final Key			requestKey;

	public SendResourceTask(Request request) {
		this.requestKey = request.getKey();
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			Request dbRequest = dao.find(new Request(requestKey));

			if (dbRequest == null || dbRequest.getStatus() == SENT)
				return;

			Transaction tx = pm.currentTransaction();
			tx.begin();
			checkDistributorAccess(dbRequest.getDistributor());
			tx.commit();

			new Date().getTime();

			PlayerStatus dbStatus = dbRequest.getPlayer().getDbStatus();
			if (dbStatus == PlayerStatus.ERROR
					|| dbStatus == PlayerStatus.DISABLED) {
				dbRequest.setStatus(ERROR);
				return;
			}

			execute(dbRequest);

			dbRequest.setStatus(SENT);
			dbRequest.setCompletedAt(new Date());

			tx = pm.currentTransaction();
			tx.begin();
			dbRequest.getDistributor().setLastAccessTimestamp(
					new Date().getTime());
			tx.commit();

		} catch (Exception e) {
			// meh
			throw new RuntimeException(e);
		}
	}

	private void checkDistributorAccess(Distributor distributor) {
		Long lastAccess = distributor.getLastAccessTimestamp();
		Long now = new Date().getTime();

		if (now < (lastAccess + 10000))
			throw new IllegalStateException(distributor.getEmail()
					+ " cooldown period, will retry later");
	}

	private void execute(Request request) throws Exception {
		Distributor dist = request.getDistributor();
		String passwordPlain = decryptPassword(dist);

		if (dist.getEmail() == null || dist.getEmail().isEmpty()
				|| passwordPlain == null || passwordPlain.isEmpty())
			throw new IllegalStateException("Distributor is not configured");

		try (ErepScrapper scrapper = new ErepScrapper(dist.getEmail(),
				passwordPlain)) {
			scrapper.login();
			for (ResourceDist resource : request.getResources())
				if (resource.getStatus() != ResourceDistStatus.SENT)
					try {
						DistStrategy strategy = getStrategy(request, resource,
								scrapper);
						strategy.execute();
						resource.setStatus(ResourceDistStatus.SENT);
						throw new IllegalStateException(dist.getEmail()
								+ " cooldown period, will retry later");
					} catch (IllegalStateException e) {
						request.setStatus(SENT_PARTIAL);
						throw e;
					} catch (Exception e) {
						logger.log(Level.SEVERE, "Exception sending resource",
								e);
						resource.setStatus(ResourceDistStatus.ERROR);
						request.setStatus(SENT_PARTIAL);
						throw e;
					}
		}
	}

	private String decryptPassword(Distributor distributor) throws Exception {
		try {
			return decrypt(distributor.getPassword());
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Could not decrypt password for distributor {0}",
					distributor.getEmail());
			throw e;
		}
	}

	private DistStrategy getStrategy(Request request, ResourceDist resource,
			ErepScrapper scrapper) throws Exception {
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
