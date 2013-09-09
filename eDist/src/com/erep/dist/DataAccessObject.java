package com.erep.dist;

import static com.erep.dist.auth.SymmetricCipherUtils.encrypt;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.erep.dist.cache.CacheFacade;
import com.erep.dist.cache.Cacheable;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.HasTransitID;
import com.erep.dist.data.Licence;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.Request;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.instances.frontend.tasks.NotifyEmailChangeTask;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class DataAccessObject {

	private PersistenceManager	pm;

	private static Logger		logger	= Logger.getLogger(DataAccessObject.class
												.getName());

	public DataAccessObject(PersistenceManager pm) {
		this.pm = pm;
	}

	public boolean save(Distributor dist) {
		if (dist.getId() != null)
			return edit(dist);
		return add(dist);
	}

	public boolean save(Request request) {
		pm.makePersistent(request);
		return true;
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
		boolean result = true;
		Transaction tx = pm.currentTransaction();
		try {
			tx.begin();
			Distributor old = pm.getObjectById(Distributor.class,
					createKey(dist));
			result = edit(old, dist);
			tx.commit();
		} catch (JDOObjectNotFoundException e) {
			result = false;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				result = false;
			}
		}
		return result;
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
		Transaction tx = pm.currentTransaction();
		boolean result = true;
		try {
			tx.begin();
			PlayerGroup old = pm.getObjectById(PlayerGroup.class,
					createKey(group));
			result = edit(old, group);
			tx.commit();
		} catch (JDOObjectNotFoundException e) {
			result = false;
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				result = false;
			}
		}
		return result;
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
		return save(player, false);
	}

	public boolean save(Player player, boolean create) {
		if (!create)
			try {
				Key playerKey = createKey(player);
				player.setKey(playerKey);
				return edit(player);
			} catch (JDOObjectNotFoundException e) {
				return add(player);
			}
		return add(player);
	}

	private boolean edit(Player player) {
		Transaction tx = pm.currentTransaction();
		boolean result = true;
		try {
			tx.begin();
			Player oldPlayer = pm.getObjectById(Player.class, player.getKey());
			result = edit(oldPlayer, player);
			tx.commit();
		} finally {
			if (tx.isActive()) {
				tx.rollback();
				result = false;
			}
		}
		return result;
	}

	private boolean add(Player newPlayer) {

		Key key = KeyFactory.createKey(Player.class.getSimpleName(),
				Long.valueOf(newPlayer.getId()));
		newPlayer.setKey(key);

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
		old.setDbStatus(newest.getDbStatus());

		PlayerGroup newGroup = find(newest.getGroup());

		old.setGroup(newGroup);

		return true;
	}

	@SuppressWarnings("unchecked")
	public List<RegisteredUser> listUsers() {
		Query q = pm.newQuery(RegisteredUser.class);
		return (List<RegisteredUser>) q.execute();
	}

	@SuppressWarnings("unchecked")
	public List<Player> listAllPlayers() {
		Query q = pm.newQuery(Player.class);
		return (List<Player>) q.execute();
	}

	public List<Player> listPlayers(String encodedCursor) {
		return queryWithCursor(Player.class, null, encodedCursor);
	}

	public List<Request> listRequests(String encodedCursor) {
		return queryWithCursor(Request.class, "", encodedCursor);
	}

	public List<Request> listRequests(Integer eRepDay, String encodedCursor) {
		String filter = "eRepDay == " + eRepDay;
		return queryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<Request> listRequests(Distributor dist, String encodedCursor) {
		String filter = "distributor == " + dist.getKey();
		return queryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<Request> listRequests(Player player, String encodedCursor) {
		String filter = "player == " + player.getKey();
		return queryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<Request> listRequests(PlayerGroup group, String encodedCursor) {
		String filter = "group == " + group.getKey();
		return queryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<DailyStats> listStats(Player player, String encodedCursor) {
		String filter = "player == " + player.getKey();
		return queryWithCursor(DailyStats.class, filter, encodedCursor);
	}

	public List<DailyStats> listStats(Integer day, String encodedCursor) {
		String filter = "day == " + day;
		return queryWithCursor(DailyStats.class, filter, encodedCursor);
	}

	public List<DailyFights> listFights(Player player, String encodedCursor) {
		String filter = "player == " + player.getKey();
		return queryWithCursor(DailyFights.class, filter, encodedCursor);
	}

	public List<DailyFights> listFights(Integer day, String encodedCursor) {
		String filter = "day == " + day;
		return queryWithCursor(DailyFights.class, filter, encodedCursor);
	}

	public List<DailyFights> listFights(PlayerGroup group, String encodedCursor) {
		String filter = "group == " + group.getKey();
		return queryWithCursor(DailyFights.class, filter, encodedCursor);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> queryWithCursor(Class<T> clazz, String filter,
			String encodedCursor) {
		Query q = pm.newQuery(clazz);
		if (filter != null && !filter.trim().isEmpty())
			q.setFilter(filter);
		setCursor(q, encodedCursor);
		q.setRange(0, ServerConstants.QUERY_PAGE_LIMIT);
		return (List<T>) q.execute();
	}

	public int getDailyRequestedFF(Player player, int eRepDay) {
		Query q = pm.newQuery(Request.class);
		q.setFilter("player == " + player.getKey() + " && eRepDay == "
				+ eRepDay);
		q.setResult("sum(requestedFF)");
		Long response = ((Long) q.execute());
		return response != null ? response.intValue() : 0;
	}

	public boolean requestedRocket(Player player, int eRepDay) {
		Query q = pm.newQuery(Request.class);
		q.setFilter("player == " + player.getKey() + " && eRepDay == "
				+ eRepDay + " && type == '" + RequestType.ROCKET.name() + "'");
		q.setUnique(true);
		Request request = (Request) q.execute();
		return request != null;
	}

	public Request find(Request request) {
		try {
			Key key = KeyFactory.stringToKey(request.getId());
			return pm.getObjectById(Request.class, key);
		} catch (JDOObjectNotFoundException e) {
			logger.log(Level.WARNING,
					"Tried to get non-existent request with key {0}",
					request.getId());
			return null;
		}
	}

	public Licence getLicence() {
		return (Licence) pm.getObjectById(Licence.class, KeyFactory.createKey(
				Licence.class.getSimpleName(), ServerConstants.DEFAULT_ID));
	}

	public Player find(Player player) {
		try {
			if (player == null || player.getId() == null)
				return null;

			Key key = player.getKey() != null ? player.getKey()
					: createKey(player);

			return pm.getObjectById(Player.class, key);

		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	public DailyStats getStats(Player player, Integer day) {
		Key key = KeyFactory.createKey(player.getKey(),
				DailyStats.class.getSimpleName(), day);
		return pm.getObjectById(DailyStats.class, key);
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

			return pm.getObjectById(PlayerGroup.class, createKey(group));
		} catch (JDOObjectNotFoundException e) {
			return defaultGroup();
		}
	}

	public Distributor find(Distributor dist) {
		try {
			if (dist == null || dist.getId() == null)
				return defaultDistributor();

			return pm.getObjectById(Distributor.class, createKey(dist));
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

	private Key createKey(HasTransitID object) {
		return KeyFactory.stringToKey(object.getId());
	}

	@Cacheable
	public ServerConfig getServerConfig() {
		return pm.getObjectById(ServerConfig.class, ServerConstants.DEFAULT_ID);
	}

	public boolean save(ServerConfig newConfig) {
		ServerConfig dbConfig = pm.getObjectById(ServerConfig.class,
				ServerConstants.DEFAULT_ID);

		if (newConfig.getGranularity() != null)
			dbConfig.setGranularity(newConfig.getGranularity());

		if (newConfig.getMuName() != null)
			dbConfig.setMuName(newConfig.getMuName());

		if (newConfig.getReportsAccess() != null)
			dbConfig.setReportsAccess(newConfig.getReportsAccess());

		if (newConfig.getContactEmail() != null
				&& !newConfig.getContactEmail().equalsIgnoreCase(
						dbConfig.getContactEmail())) {

			startEmailChangeNotificationTask(dbConfig.getContactEmail(),
					newConfig.getContactEmail());
			dbConfig.setContactEmail(newConfig.getContactEmail());
		}

		if (newConfig.getMuConfig() != null) {
			dbConfig.getMuConfig().setAutoSend(
					newConfig.getMuConfig().getAutoSend());
			dbConfig.getMuConfig().setCurrentMaxDailyFF(
					newConfig.getMuConfig().getCurrentMaxDailyFF());
			dbConfig.getMuConfig().setRocketAllowed(
					newConfig.getMuConfig().getRocketAllowed());
			dbConfig.getMuConfig().setDistributor(
					find(newConfig.getMuConfig().getDistributor()));
		}

		CacheFacade.remove("com.erep.dist.DataAccessObject.getServerConfig()");
		return true;
	}

	public boolean save(RegisteredUser user) {
		RegisteredUser dbUser = find(user);
		return edit(dbUser, user);
	}

	private boolean edit(RegisteredUser dbUser, RegisteredUser user) {

		if (user.getName() != null && !user.getName().trim().isEmpty())
			dbUser.setName(user.getName());

		if (user.getRole() != null)
			dbUser.setRole(user.getRole());
		CacheFacade.remove("com.erep.dist.DataAccessObject.find(User="
				+ dbUser.getgEmail() + ";)");
		return true;
	}

	private void startEmailChangeNotificationTask(String oldEmail,
			String newEmail) {
		DeferredTask task = new NotifyEmailChangeTask(oldEmail, newEmail);
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withPayload(task));
	}

}
