package com.erep.dist.instances.frontend.tasks;

import static com.erep.dist.auth.SymmetricCipherUtils.decrypt;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

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

			Request dbRequest = dao.find(new Request(requestKey));
			ResourceDist dbResource = dao.find(new ResourceDist(resourceKey));

			if (dbRequest == null || dbResource == null
					|| dbResource.getStatus() == ResourceDistStatus.SENT)
				return;

			checkDistributorAccess(pm, dbRequest.getDistributor());

			PlayerStatus dbStatus = dbRequest.getPlayer().getDbStatus();
			if (dbStatus == PlayerStatus.ERROR
					|| dbStatus == PlayerStatus.DISABLED) {
				dbRequest.setStatus(RequestStatus.ERROR);
				dbResource.setStatus(ResourceDistStatus.ERROR);
				return;
			}

			execute(dbRequest, dbResource);

			dbRequest.setStatus(RequestStatus.SENT);
			dbRequest.setCompletedAt(new Date());
			
			setDistributorAccess(pm, dbRequest.getDistributor());

		} catch (Exception e) {
			// meh
			throw new RuntimeException(e);
		}
	}

	private void checkDistributorAccess(PersistenceManager pm,
			Distributor distributor) {

		Transaction tx = pm.currentTransaction();
		tx.begin();
		Long lastAccess = distributor.getLastAccessTimestamp();
		tx.commit();

		Long now = new Date().getTime();
		if (now < (lastAccess + 10000))
			throw new IllegalStateException(distributor.getEmail()
					+ " cooldown period, will retry later");

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
		String passwordPlain = decryptPassword(dist);

		try (ErepScrapper scrapper = new ErepScrapper(dist.getEmail(),
				passwordPlain)) {
			scrapper.login();

			ManufacturedStrategy strategy = getStrategy(dbRequest, dbResource,
					scrapper);
			strategy.execute();

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

	private String decryptPassword(Distributor distributor) throws Exception {
		try {
			String password = decrypt(distributor.getPassword());

			if (distributor.getEmail() == null
					|| distributor.getEmail().isEmpty() || password == null
					|| password.trim().isEmpty())
				throw new IllegalStateException("Distributor is not configured");

			return password;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Could not decrypt password for distributor {0}",
					distributor.getEmail());
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
