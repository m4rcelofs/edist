package com.erep.dist;

import static com.erep.dist.CipherUtils.encrypt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.cache.Cacheable;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.HasTransitID;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;
import com.erep.dist.data.ServerConfig;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class DataAccessObject {

	private static final Integer	PAGE_LIMIT	= 20;

	private PersistenceManager		pm;

	private static Logger			logger		= Logger.getLogger(DataAccessObject.class
														.getName());

	public DataAccessObject(PersistenceManager pm) {
		this.pm = pm;
	}

	public boolean save(Distributor dist) {
		if (dist.getId() != null)
			return edit(dist);
		return add(dist);
	}

	private boolean add(Distributor dist) {
		if (dist.getPassword() == null || dist.getPassword().trim().isEmpty()
				|| dist.getEmail() == null
				|| dist.getPassword().trim().isEmpty())
			return false;
		try {
			dist.setPassword(encrypt(dist.getPassword()));
			pm.makePersistent(dist);
			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Exception while trying to add a distributor", e);
			return false;
		}
	}

	private boolean edit(Distributor dist) {
		try {
			Distributor old = pm.getObjectById(Distributor.class,
					createKey(Distributor.class, dist));
			return edit(old, dist);
		} catch (JDOObjectNotFoundException e) {
			return false;
		}
	}

	private boolean edit(Distributor old, Distributor newest) {
		try {
			old.setName(newest.getName());
			old.setEmail(newest.getEmail());
			if (newest.getPassword() != null
					&& !newest.getPassword().trim().isEmpty())
				old.setPassword(encrypt(newest.getPassword()));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Distributor> listDistributors() {
		Query q = pm.newQuery(Distributor.class);
		return (List<Distributor>) q.execute();
	}

	public boolean save(PlayerGroup group) {
		if (group.getId() != null)
			return edit(group);
		else
			return add(group);
	}

	private boolean add(PlayerGroup group) {
		group.setPlayers(new HashSet<Player>());
		pm.makePersistent(group);
		return true;
	}

	private boolean edit(PlayerGroup group) {
		try {
			PlayerGroup old = pm.getObjectById(PlayerGroup.class,
					createKey(PlayerGroup.class, group));
			return edit(old, group);
		} catch (JDOObjectNotFoundException e) {
			return false;
		}
	}

	private boolean edit(PlayerGroup old, PlayerGroup newest) {

		old.setGroupName(newest.getGroupName());
		if (newest.getConfig() != null) {
			old.getConfig().setAutoSend(newest.getConfig().getAutoSend());
			old.getConfig().setCurrentMaxDailyFF(
					newest.getConfig().getCurrentMaxDailyFF());
			old.getConfig().setRocketAllowed(
					newest.getConfig().getRocketAllowed());
			old.getConfig().setDistributor(
					find(newest.getConfig().getDistributor()));
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	public List<PlayerGroup> listGroups() {
		Query q = pm.newQuery(PlayerGroup.class);
		return (List<PlayerGroup>) q.execute();
	}

	public boolean save(Player player) {
		Key playerKey = createKey(Player.class, player);
		try {
			Player oldPlayer = pm.getObjectById(Player.class, playerKey);
			return edit(oldPlayer, player);
		} catch (JDOObjectNotFoundException e) {
			player.setKey(playerKey);
			return add(player);
		}
	}

	private boolean add(Player newPlayer) {

		newPlayer.setRequests(new ArrayList<Request>());

		PlayerGroup group = find(newPlayer.getGroup());
		newPlayer.setGroup(group);
		group.addPlayer(newPlayer);

		return true;
	}

	private boolean edit(Player old, Player newest) {
		if (old.getConfig() == null)
			old.setConfig(newest.getConfig());
		else {
			old.getConfig().setAutoSend(newest.getConfig().getAutoSend());
			old.getConfig().setCurrentMaxDailyFF(
					newest.getConfig().getCurrentMaxDailyFF());
		}

		old.setRocketEnabled(newest.getRocketEnabled());
		old.setStatus(newest.getStatus());

		PlayerGroup oldGroup = old.getGroup();
		PlayerGroup newGroup = find(newest.getGroup());

		old.setGroup(newGroup);
		newGroup.addPlayer(old);
		oldGroup.removePlayer(old);
		return true;
	}

	public Player getPlayer(String id) {
		Key playerKey = KeyFactory.createKey(Player.class.getSimpleName(),
				Long.valueOf(id));
		return pm.getObjectById(Player.class, playerKey);
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

	public Player find(Player player) {
		try {
			if (player == null || player.getId() == null)
				return null;

			return pm.getObjectById(Player.class,
					createKey(Player.class, player));

		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	@Cacheable
	public Key findPlayerKey(String playerName) {
		Query q = pm.newQuery(Player.class);
		q.setFilter("name == '" + playerName + "'");
		q.setResult("key");
		q.setUnique(true);
		return (Key) q.execute();
	}

	public PlayerGroup find(PlayerGroup group) {
		try {
			if (group == null || group.getId() == null)
				return defaultGroup();

			return pm.getObjectById(PlayerGroup.class,
					createKey(PlayerGroup.class, group));
		} catch (JDOObjectNotFoundException e) {
			return defaultGroup();
		}
	}

	public Distributor find(Distributor dist) {
		try {
			if (dist == null || dist.getId() == null)
				return defaultDistributor();

			return pm.getObjectById(Distributor.class,
					createKey(Distributor.class, dist));
		} catch (JDOObjectNotFoundException e) {
			return defaultDistributor();
		}
	}

	private Distributor defaultDistributor() {
		return pm.getObjectById(Distributor.class, KeyFactory.createKey(
				Distributor.class.getSimpleName(), ServerConstants.DEFAULT_ID));
	}

	private PlayerGroup defaultGroup() {
		return pm.getObjectById(PlayerGroup.class, KeyFactory.createKey(
				PlayerGroup.class.getSimpleName(), ServerConstants.DEFAULT_ID));
	}

	private Key createKey(Class<?> clazz, HasTransitID object) {
		return KeyFactory.createKey(clazz.getSimpleName(),
				Long.valueOf(object.getId()));
	}

	@Cacheable
	// TODO clear cache
	public ServerConfig getServerConfig() {
		return pm.getObjectById(ServerConfig.class, ServerConstants.DEFAULT_ID);
	}

}
