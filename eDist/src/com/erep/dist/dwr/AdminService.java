package com.erep.dist.dwr;

import java.util.List;
import java.util.Map;

import javax.jdo.Transaction;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Employer;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.dwr.dto.RegisteredUserQuery;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class AdminService {

	@RemoteMethod
	public RegisteredUserQuery listRegisteredUsers(String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			RegisteredUserQuery dto = new RegisteredUserQuery();

			List<RegisteredUser> registered = new DataAccessObject(pm)
					.listUsers(encodedCursor);
			dto.setResults(registered);
			dto.setEncodedCursor(JDOCursorHelper.getCursor(registered)
					.toWebSafeString());

			return dto;
		}
	}

	@RemoteMethod
	public boolean saveRegisteredUser(RegisteredUser user) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(user);
		}
	}

	@RemoteMethod
	public boolean removeRegisteredUser(RegisteredUser user) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			RegisteredUser dbUser = new DataAccessObject(pm).find(user);
			pm.deletePersistent(dbUser);
			return true;
		}
	}

	@RemoteMethod
	public ServerConfig getServerConfig() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().setMaxFetchDepth(-1);
			pm.getFetchPlan().addGroup("configListing");
			ServerConfig config = new DataAccessObject(pm).getServerConfig();
			return config;
		}
	}

	@RemoteMethod
	public boolean saveServerConfig(ServerConfig config) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			Transaction tx = pm.currentTransaction();
			tx.begin();
			boolean response = new DataAccessObject(pm).save(config);
			tx.commit();
			if (!response || tx.isActive()) {
				tx.rollback();
				response = false;
			}
			return response;
		}
	}

	@RemoteMethod
	public boolean saveDistributor(Distributor dist) {

		if (dist == null || dist.getEmail() == null
				|| dist.getEmail().trim().isEmpty())
			return false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(dist);
		}
	}

	@RemoteMethod
	public List<Distributor> listDistributors() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("distributorListing");
			List<Distributor> dists = new DataAccessObject(pm)
					.listDistributors();
			return dists;
		}
	}
	
	@RemoteMethod 
	public Map<ResourceType, Map<ResourceQuality, Integer>> loadInventory(Distributor dist) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("inventoryListing");
			Distributor dbDist = new DataAccessObject(pm).find(dist);
			return dbDist.getLastInventory().rowMap();
		}
	}

	@RemoteMethod
	public boolean saveEmployer(Employer employer) {

		if (employer == null || employer.getErId() == null
				|| employer.getErId().trim().isEmpty()
				|| employer.getName() == null
				|| employer.getName().trim().isEmpty())
			return false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(employer);
		}
	}

	@RemoteMethod
	public List<Employer> listEmployers() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).listEmployers();
		}
	}
}
