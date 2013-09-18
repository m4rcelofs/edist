package com.erep.dist.dwr;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.dwr.dto.RegisteredUserQuery;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class AdminService {

	@RemoteMethod
	public RegisteredUserQuery listRegisteredUsers(String encodedCursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			RegisteredUserQuery dto = new RegisteredUserQuery();
			
			List<RegisteredUser> registered = new DataAccessObject(pm).listUsers(encodedCursor);
			dto.setResults(registered);
			dto.setEncodedCursor(JDOCursorHelper.getCursor(registered).toWebSafeString());
			
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
	public ServerConfig getServerConfig() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).getServerConfig();
		}
	}

	@RemoteMethod
	public boolean saveServerConfig(ServerConfig config) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(config);
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
}
