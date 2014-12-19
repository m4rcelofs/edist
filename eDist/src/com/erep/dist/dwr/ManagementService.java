package com.erep.dist.dwr;

import java.util.Arrays;
import java.util.List;

import javax.jdo.JDOObjectNotFoundException;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.PlayerIdentificator;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.dao.Distributors;
import com.erep.dist.data.dao.Players;
import com.erep.dist.data.dao.Server;
import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.dwr.dto.PlayerGroupQuery;
import com.erep.dist.dwr.dto.PlayerQuery;
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
			Players players = new Players(pm);

			dto.setGroupsCount(dao.groupRoot().getGroupsCount());
			dto.setPlayersCount(players.root().getPlayersCount());
			dto.setResults(dao.listGroups());

			return dto;
		}
	}

	@RemoteMethod
	public PlayerGroupQuery listGroups() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			PlayerGroupQuery dto = new PlayerGroupQuery();
			DataAccessObject dao = new DataAccessObject(pm);
			Server server = new Server(pm);
			Players players = new Players(pm);

			pm.getFetchPlan().addGroup("groupConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			dto.setGroupsCount(dao.groupRoot().getGroupsCount());
			dto.setPlayersCount(players.root().getPlayersCount());

			List<PlayerGroup> groups = dao.listGroups();
			dto.setResults(pm.detachCopyAll(groups));
			dto.setGranularity(server.getConfig().getGranularity());

			return dto;
		}
	}

	@RemoteMethod
	public boolean addPlayer(Player player) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new Players(pm).save(player, true);
		}
	}

	@RemoteMethod
	public boolean editPlayer(Player player) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new Players(pm).save(player);
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayers(Void param, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			PlayerQuery queryResults = new PlayerQuery();
			Server server = new Server(pm);
			Players players = new Players(pm);

			List<Player> playerList = players.list(cursor);

			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(playerList)
					.toWebSafeString());
			queryResults.setResults(pm.detachCopyAll(playerList));

			queryResults.setGranularity(server.getConfig().getGranularity());

			return queryResults;
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayersByGroup(PlayerGroup param, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			PlayerQuery queryResults = new PlayerQuery();

			Server server = new Server(pm);
			Players players = new Players(pm);

			if (param != null) {
				List<Player> playerList = players.list(param, cursor);
				queryResults.setResults(playerList);
				queryResults.setEncodedCursor(JDOCursorHelper.getCursor(
						playerList).toWebSafeString());
			}

			queryResults.setGranularity(server.getConfig().getGranularity());

			return queryResults;
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayersByString(String param, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			PlayerQuery queryResults = new PlayerQuery();

			Server server = new Server(pm);

			try {
				Player player = new PlayerIdentificator(pm)
						.identifyRequestString(param);
				if (player != null)
					queryResults.setResults(Arrays.asList(player));
			} catch (JDOObjectNotFoundException e) {
				// ignore
			}

			queryResults.setGranularity(server.getConfig().getGranularity());

			return queryResults;
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayersByStatus(PlayerStatus param, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			PlayerQuery queryResults = new PlayerQuery();

			Server server = new Server(pm);
			Players players = new Players(pm);

			List<Player> playerList = players.list(param, cursor);

			queryResults.setResults(playerList);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(playerList)
					.toWebSafeString());

			queryResults.setGranularity(server.getConfig().getGranularity());

			return queryResults;
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayersByFight(PlayerDistStatus param, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			PlayerQuery queryResults = new PlayerQuery();

			Server server = new Server(pm);
			Players players = new Players(pm);

			List<Player> playerList = players.listByFight(param, cursor);

			queryResults.setResults(playerList);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(playerList)
					.toWebSafeString());

			queryResults.setGranularity(server.getConfig().getGranularity());

			return queryResults;
		}
	}

	@RemoteMethod
	public PlayerQuery listPlayersByWork(PlayerDistStatus param, String cursor) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			pm.getFetchPlan().addGroup("playerConfigListing");
			pm.getFetchPlan().addGroup("configListing");

			PlayerQuery queryResults = new PlayerQuery();

			Server server = new Server(pm);
			Players players = new Players(pm);

			List<Player> playerList = players.listByWork(param, cursor);

			queryResults.setResults(playerList);
			queryResults.setEncodedCursor(JDOCursorHelper.getCursor(playerList)
					.toWebSafeString());

			queryResults.setGranularity(server.getConfig().getGranularity());

			return queryResults;
		}
	}

	@RemoteMethod
	public List<Distributor> listDistributors() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("distributorListing");
			List<Distributor> dists = new Distributors(pm).listDistributors();
			return dists;
		}
	}

	@RemoteMethod
	public boolean removePlayer(Player player) {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			return new Players(pm).remove(player);
		} catch (JDOObjectNotFoundException | NullPointerException e) {
			return false;
		}
	}

}
