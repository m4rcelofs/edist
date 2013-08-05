package com.erep.dist;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.data.DistRequest;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.HasTransitID;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.RocketRequest;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class DataAccessObject {

	private static final Integer	PAGE_LIMIT	= 20;

	private PersistenceManager		pm;

	public DataAccessObject(PersistenceManager pm) {
		this.pm = pm;
	}

	public boolean add(Distributor dist) {
		try {
			Distributor old = pm.getObjectById(Distributor.class,
					createKey(Distributor.class, dist));
			return edit(old, dist);
		} catch (JDOObjectNotFoundException e) {
			pm.makePersistent(dist);
			return true;
		}
	}

	public boolean edit(Distributor dist) {
		Distributor old = pm.getObjectById(Distributor.class,
				createKey(Distributor.class, dist));
		return edit(old, dist);
	}

	private boolean edit(Distributor old, Distributor newest) {
		old.setEmail(newest.getEmail());
		if (newest.getPassword() != null
				&& !newest.getPassword().trim().isEmpty())
			old.setPassword(newest.getPassword());
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<Distributor> listDistributors() {
		Query q = pm.newQuery(Distributor.class);
		return (List<Distributor>) q.execute();
	}

	public boolean add(PlayerGroup group) {
		try {
			PlayerGroup old = pm.getObjectById(PlayerGroup.class,
					createKey(PlayerGroup.class, group));
			return edit(old, group);
		} catch (JDOObjectNotFoundException e) {
			group.setPlayers(new HashSet<Player>());
			pm.makePersistent(group);
			return true;
		}
	}

	public boolean edit(PlayerGroup group) {
		PlayerGroup old = find(group);
		return edit(old, group);
	}

	private boolean edit(PlayerGroup old, PlayerGroup newest) {

		old.setGroupName(newest.getGroupName());
		old.getConfig().setAutoSend(newest.getConfig().getAutoSend());
		old.getConfig().setCurrentMaxDailyFF(
				newest.getConfig().getCurrentMaxDailyFF());
		old.getConfig().setRocketAllowed(newest.getConfig().getRocketAllowed());
		old.getConfig().setDistributor(
				find(newest.getConfig().getDistributor()));

		return true;
	}

	public boolean delete(PlayerGroup dto) {
		if (ServerConstants.DEFAULT.equalsIgnoreCase(dto.getId()))
			return false;
		try {
			PlayerGroup group = pm.getObjectById(PlayerGroup.class,
					createKey(PlayerGroup.class, dto));

			PlayerGroup defaultGroup = find(new PlayerGroup());

			defaultGroup.getPlayers().addAll(group.getPlayers());
			for (Player p : group.getPlayers())
				p.setGroup(defaultGroup);

			pm.deletePersistent(group);

		} catch (JDOObjectNotFoundException e) {
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public List<PlayerGroup> listGroups() {
		Query q = pm.newQuery(PlayerGroup.class);
		return (List<PlayerGroup>) q.execute();
	}

	public boolean add(Player newPlayer) {
		Key playerKey = createKey(Player.class, newPlayer);

		try {
			Player oldPlayer = pm.getObjectById(Player.class, playerKey);
			return edit(oldPlayer, newPlayer);
		} catch (JDOObjectNotFoundException e) {

			newPlayer.setKey(playerKey);
			newPlayer.setFfRequests(new ArrayList<DistRequest>());
			newPlayer.setRocketRequests(new ArrayList<RocketRequest>());

			PlayerGroup group = find(newPlayer.getGroup());
			newPlayer.setGroup(group);
			group.addPlayer(newPlayer);

			pm.makePersistent(newPlayer);

			return true;
		}
	}

	public boolean edit(Player newPlayer) {
		Player oldPlayer = pm.getObjectById(Player.class,
				createKey(Player.class, newPlayer));
		return edit(oldPlayer, newPlayer);
	}

	private boolean edit(Player old, Player newest) {
		old.getConfig().setAutoSend(newest.getConfig().getAutoSend());
		old.getConfig().setCurrentMaxDailyFF(
				newest.getConfig().getCurrentMaxDailyFF());

		old.setRocketEnabled(newest.getRocketEnabled());
		old.setStatus(newest.getStatus());

		PlayerGroup oldGroup = old.getGroup();
		PlayerGroup newGroup = find(newest.getGroup());

		old.setGroup(newGroup);
		newGroup.addPlayer(old);
		oldGroup.removePlayer(old);
		return true;
	}

	public Player getPlayer(Key key) {
		return pm.getObjectById(Player.class, key);
	}

	@SuppressWarnings("unchecked")
	public List<Player> listPlayers(String encodedCursor) {
		Query q = pm.newQuery(Player.class);

		if (encodedCursor != null && !encodedCursor.trim().isEmpty()) {
			Cursor cursor = Cursor.fromWebSafeString(encodedCursor);
			Map<String, Object> extensionMap = new HashMap<>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			q.setExtensions(extensionMap);
		}

		q.setRange(0, PAGE_LIMIT);

		return (List<Player>) q.execute();
	}

	public PlayerGroup find(PlayerGroup group) {
		try {
			if (group == null || group.getId() == null
					|| group.getId().trim().isEmpty())
				return defaultGroup();

			return pm.getObjectById(PlayerGroup.class,
					createKey(PlayerGroup.class, group));
		} catch (JDOObjectNotFoundException e) {
			return defaultGroup();
		}
	}

	public Distributor find(Distributor dist) {
		try {
			if (dist == null || dist.getId() == null
					|| dist.getId().trim().isEmpty())
				return defaultDistributor();

			return pm.getObjectById(Distributor.class,
					createKey(Distributor.class, dist));
		} catch (JDOObjectNotFoundException e) {
			return defaultDistributor();
		}
	}

	private Distributor defaultDistributor() {
		return pm.getObjectById(Distributor.class, KeyFactory.createKey(
				Distributor.class.getSimpleName(), ServerConstants.DEFAULT));
	}

	private PlayerGroup defaultGroup() {
		return pm.getObjectById(PlayerGroup.class, KeyFactory.createKey(
				PlayerGroup.class.getSimpleName(), ServerConstants.DEFAULT));
	}

	private Key createKey(Class<?> clazz, HasTransitID object) {
		return KeyFactory.createKey(clazz.getSimpleName(), object.getId());
	}

}
