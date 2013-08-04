package com.erep.dist.dwr;

import static com.erep.dist.dwr.dto.TransferHelper.toDTO;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Player;
import com.erep.dist.dwr.dto.PlayerDTO;
import com.erep.dist.dwr.dto.PlayerGroupDTO;
import com.erep.dist.dwr.dto.PlayerQuery;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

@RemoteProxy
public class AdminService {

	@RemoteMethod
	public boolean addGroup(PlayerGroupDTO group) {
		// TODO
		return false;
	}

	@RemoteMethod
	public boolean editGroup(PlayerGroupDTO group) {
		// TODO
		return false;
	}

	@RemoteMethod
	public boolean addPlayer(PlayerDTO player) {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			boolean created = new DataAccessObject(pm).add(player);

			if (created) {
				Queue queue = QueueFactory.getQueue("api");
				queue.add(withUrl("/admin/tasks/getUserInfo").param("id",
						player.getErId()));
			}
			return created;
		}

	}

	@RemoteMethod
	public boolean editPlayer(PlayerDTO player) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new DataAccessObject(pm).edit(player);
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayers(String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			PlayerQuery queryResults = new PlayerQuery();
			List<Player> players = new DataAccessObject(pm)
					.listAllPlayers(cursor);

			List<PlayerDTO> dtos = new ArrayList<>();

			for (Player p : players)
				dtos.add(toDTO(p));

			queryResults.setResults(dtos);

			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(players)
					.toWebSafeString());
			return queryResults;
		}
	}

}
