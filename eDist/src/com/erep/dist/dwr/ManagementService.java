package com.erep.dist.dwr;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.dwr.dto.PlayerGroupQuery;
import com.erep.dist.dwr.dto.PlayerQuery;
import com.erep.dist.instances.frontend.tasks.GetUserInfoTask;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class ManagementService {

	@RemoteMethod
	public boolean saveGroup(PlayerGroup group) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(group);
		}
	}

	@RemoteMethod
	public PlayerGroupQuery listGroupNames() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			PlayerGroupQuery dto = new PlayerGroupQuery();
			DataAccessObject dao = new DataAccessObject(pm);
			dto.setResults(dao.listGroups());

			return dto;
		}
	}

	@RemoteMethod
	public PlayerGroupQuery listGroups() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			PlayerGroupQuery dto = new PlayerGroupQuery();
			DataAccessObject dao = new DataAccessObject(pm);

			pm.getFetchPlan().addGroup("groupConfigListing");

			dto.setResults(dao.listGroups());
			dto.setGranularity(dao.getServerConfig().getGranularity());

			return dto;
		}
	}

	@RemoteMethod
	public boolean addPlayer(Player player) {

		// TODO check licence
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			boolean created = new DataAccessObject(pm).save(player, true);

			if (created) {
				DeferredTask task = new GetUserInfoTask(player);
				Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");
				queue.add(withPayload(task));
			}
			return created;
		}

	}

	@RemoteMethod
	public boolean editPlayer(Player player) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(player);
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayers(String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");

			PlayerQuery queryResults = new PlayerQuery();
			DataAccessObject dao = new DataAccessObject(pm);
			List<Player> players = dao.listPlayers(cursor);

			queryResults.setResults(players);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(players)
					.toWebSafeString());

			queryResults.setGranularity(dao.getServerConfig().getGranularity());

			return queryResults;
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
