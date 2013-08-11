package com.erep.dist.dwr;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.dwr.dto.PlayerQuery;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class AdminService {

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
			List<Distributor> dists = new DataAccessObject(pm)
					.listDistributors();
			return dists;
		}
	}

	@RemoteMethod
	public boolean saveGroup(PlayerGroup group) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).save(group);
		}
	}

	@RemoteMethod
	public List<PlayerGroup> listGroups() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("groupListing");
			List<PlayerGroup> groups = new DataAccessObject(pm).listGroups();
			return groups;
		}
	}

	@RemoteMethod
	public boolean addPlayer(Player player) {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			boolean created = new DataAccessObject(pm).save(player);

			if (created) {
				Queue queue = QueueFactory.getQueue("api");
				queue.add(withUrl("/admin/tasks/getUserInfo").param("id",
						player.getId() + ""));
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

			pm.getFetchPlan().addGroup("playerListing");

			PlayerQuery queryResults = new PlayerQuery();
			List<Player> players = new DataAccessObject(pm).listPlayers(cursor);

			queryResults.setResults(players);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(players)
					.toWebSafeString());
			return queryResults;
		}
	}

}
