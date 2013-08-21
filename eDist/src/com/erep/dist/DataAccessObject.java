package com.erep.dist;

import static com.erep.dist.CipherUtils.encrypt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.auth.RegisteredUser;
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
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class DataAccessObject {

	private PersistenceManager	pm;

	private static Logger		logger	= Logger.getLogger(DataAccessObject.class
												.getName());

	public DataAccessObject(PersistenceManager pm) {
		this.pm = pm;
	}

	public void save(Request request) {
		pm.makePersistent(request);
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

		PlayerGroup group = find(newPlayer.getGroup());
		newPlayer.setGroup(group);
		pm.makePersistent(newPlayer);

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

		PlayerGroup newGroup = find(newest.getGroup());

		old.setGroup(newGroup);

		return true;
	}

	public Player getPlayer(String id) {
		Key playerKey = KeyFactory.createKey(Player.class.getSimpleName(),
				Long.valueOf(id));
		return pm.getObjectById(Player.class, playerKey);
	}

	@SuppressWarnings("unchecked")
	public List<Player> listAllPlayers() {
		Query q = pm.newQuery(Player.class);
		return (List<Player>) q.execute();
	}

	@SuppressWarnings("unchecked")
	public List<Player> listPlayers(String encodedCursor) {
		Query q = pm.newQuery(Player.class);
		setCursor(q, encodedCursor);
		q.setRange(0, ServerConstants.QUERY_PAGE_LIMIT);
		return (List<Player>) q.execute();
	}

	public List<Request> listRequests(String encodedCursor) {
		return listRequests("", encodedCursor);
	}

	public List<Request> listRequests(Integer eRepDay, String encodedCursor) {
		String filter = "eRepDay == " + eRepDay;
		return listRequests(filter, encodedCursor);
	}

	public List<Request> listRequests(Distributor dist, String encodedCursor) {
		String filter = "distributor == " + dist.getId();
		return listRequests(filter, encodedCursor);
	}

	public List<Request> listRequests(Player player, String encodedCursor) {
		String filter = "player == " + player.getId();
		return listRequests(filter, encodedCursor);
	}

	public List<Request> listRequests(PlayerGroup group, String encodedCursor) {
		String filter = "group == " + group.getId();
		return listRequests(filter, encodedCursor);
	}

	@SuppressWarnings("unchecked")
	private List<Request> listRequests(String filter, String encodedCursor) {
		Query q = pm.newQuery(Request.class);
		if (filter != null && !filter.trim().isEmpty())
			q.setFilter(filter);
		setCursor(q, encodedCursor);
		q.setRange(0, ServerConstants.QUERY_PAGE_LIMIT);
		return (List<Request>) q.execute();
	}

	public Request getRequest(Long id) {
		Key key = KeyFactory.createKey(Request.class.getSimpleName(), id);
		return pm.getObjectById(Request.class, key);
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

	private void setCursor(Query query, String encodedCursor) {
		if (encodedCursor != null && !encodedCursor.trim().isEmpty()) {
			Cursor cursor = Cursor.fromWebSafeString(encodedCursor);
			Map<String, Object> extensionMap = new HashMap<>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
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

	@Cacheable
	public RegisteredUser find(User user) {
		try {
			return pm.getObjectById(RegisteredUser.class, user.getUserId());
		} catch (JDOObjectNotFoundException e) {
			RegisteredUser regUser = new RegisteredUser(user.getUserId(),
					user.getEmail());
			pm.makePersistent(regUser);
			return regUser;
		}
	}

	public RegisteredUser find(RegisteredUser user) {
		return pm.getObjectById(RegisteredUser.class, user.getgId());
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
