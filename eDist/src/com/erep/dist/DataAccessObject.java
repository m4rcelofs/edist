package com.erep.dist;

import static com.erep.dist.auth.SymmetricCipherUtils.encrypt;
import static com.erep.dist.data.enums.RequestType.ROCKET;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.DistributorRoot;
import com.erep.dist.data.Employer;
import com.erep.dist.data.EmployerRoot;
import com.erep.dist.data.Licence;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.PlayerGroupRoot;
import com.erep.dist.data.PlayerRoot;
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

	public boolean save(Employer employer) {
		employer.setKey(KeyFactory.createKey(employerRootKey(),
				Employer.class.getSimpleName(), employer.getErId()));
		employerRoot().addEmployers(employer);
		return true;
	}

	public boolean save(Distributor dist) {
		if (dist.getEncodedKey() != null)
			return edit(dist);
		return add(dist);
	}

	public boolean save(Request request) {
		pm.makePersistent(request);
		return true;
	}

	public boolean save(DailyWork work) {
		pm.makePersistent(work);
		return true;
	}

	private boolean add(Distributor dist) {
		if (dist.getPassword() == null || dist.getPassword().trim().isEmpty()
				|| dist.getEmail() == null
				|| dist.getPassword().trim().isEmpty())
			return false;
		try {
			dist.setPassword(encrypt(dist.getPassword()));

			distributorRoot().addDistributor(dist);
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
			Distributor old = pm
					.getObjectById(Distributor.class, dist.getKey());
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
	public List<Employer> listEmployers() {
		Query q = pm.newQuery(Employer.class);
		q.setFilter("root == rootParam");
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<Employer>) q.execute(employerRootKey());
	}

	@SuppressWarnings("unchecked")
	public List<Distributor> listDistributors() {
		Query q = pm.newQuery(Distributor.class);
		q.setFilter("root == rootParam");
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<Distributor>) q.execute(distributorRootKey());
	}

	public boolean save(PlayerGroup group) {
		if (group.getEncodedKey() != null)
			return edit(group);
		else
			return add(group);
	}

	private boolean add(PlayerGroup group) {
		groupRoot().addGroup(group);
		pm.makePersistent(group);
		CacheFacade.remove(getClass().getName() + ".listGroups();");
		return true;
	}

	private boolean edit(PlayerGroup group) {
		Transaction tx = pm.currentTransaction();
		boolean result = true;
		try {
			tx.begin();
			PlayerGroup old = pm.getObjectById(PlayerGroup.class,
					group.getKey());
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
			newest.getConfig().setDistributor(
					find(newest.getConfig().getDistributor()));
		}
		old.setConfig(newest.getConfig());
		old.setFightVerif(newest.getFightVerif());
		old.setWorkVerif(newest.getWorkVerif());

		return true;
	}

	@Cacheable
	@SuppressWarnings("unchecked")
	public List<PlayerGroup> listGroups() {
		Query q = pm.newQuery(PlayerGroup.class);
		q.setFilter("root == rootParam");
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<PlayerGroup>) q.execute(groupRootKey());
	}

	public boolean save(Player player) {
		return save(player, false);
	}

	public boolean save(Player player, boolean create) {
		if (!create)
			try {
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

		Key key = createPlayerKey(newPlayer.getErId());
		newPlayer.setKey(key);

		if (newPlayer.getGroups() != null)
			for (PlayerGroup group : newPlayer.getGroups())
				newPlayer.addGroup(find(group));
		else
			newPlayer.addGroup(defaultGroup());

		pm.makePersistent(newPlayer);

		return true;
	}

	public Key createPlayerKey(String erId) {
		return KeyFactory.createKey(playerRootKey(),
				Player.class.getSimpleName(), Long.valueOf(erId));
	}

	public Key createDailyWorkKey(Player player, Integer day) {
		return KeyFactory.createKey(player.getKey(),
				DailyWork.class.getSimpleName(), day);
	}

	private boolean edit(Player old, Player newest) {
		if (newest.getConfig() != null) {
			newest.getConfig().setDistributor(
					find(newest.getConfig().getDistributor()));
		}
		old.setConfig(newest.getConfig());
		old.setFightVerif(newest.getFightVerif());
		old.setWorkVerif(newest.getWorkVerif());

		old.setRocketEnabled(newest.getRocketEnabled());
		old.setDbStatus(newest.getDbStatus());

		Set<PlayerGroup> groups = new HashSet<>();
		if (newest.getGroups() == null || newest.getGroups().isEmpty()) {
			groups.add(defaultGroup());
		} else {
			for (PlayerGroup group : newest.getGroups())
				groups.add(find(group));
		}
		old.setGroups(groups);

		return true;
	}

	public List<RegisteredUser> listUsers(String encodedCursor) {
		return executeQueryWithCursor(RegisteredUser.class, null, encodedCursor);
	}

	@SuppressWarnings("unchecked")
	public List<Player> listAllPlayers() {
		Query q = pm.newQuery(Player.class);
		q.setFilter("root == rootParam");
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<Player>) q.execute(playerRootKey());
	}

	@SuppressWarnings("unchecked")
	public List<Player> listPlayers(String encodedCursor) {
		Query q = queryWithCursor(Player.class, "root == rootParam",
				encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<Player>) q.execute(playerRootKey());
	}

	public List<Request> listRequests(String encodedCursor) {
		return executeQueryWithCursor(Request.class, "", encodedCursor);
	}

	public List<Request> listRequests(Integer eRepDay, String encodedCursor) {
		String filter = "eRepDay == " + eRepDay;
		return executeQueryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<Request> listRequests(Distributor dist, String encodedCursor) {
		String filter = "distributor == '" + dist.getKey() + "'";
		return executeQueryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<Request> listRequests(Player player, String encodedCursor) {
		String filter = "player == '" + player.getKey() + "'";
		return executeQueryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<Request> listRequests(PlayerGroup group, String encodedCursor) {
		String filter = "group == '" + group.getKey() + "'";
		return executeQueryWithCursor(Request.class, filter, encodedCursor);
	}

	public List<DailyStats> listStats(Player player, String encodedCursor) {
		String filter = "player == '" + player.getKey() + "'";
		return executeQueryWithCursor(DailyStats.class, filter, encodedCursor);
	}

	public List<DailyWork> listWork(Player player, String encodedCursor) {
		String filter = "player == '" + player.getKey() + "'";
		return executeQueryWithCursor(DailyWork.class, filter, encodedCursor);
	}

	public List<DailyWork> listWork(PlayerGroup group, String encodedCursor) {
		String filter = "group == '" + group.getKey() + "'";
		return executeQueryWithCursor(DailyWork.class, filter, encodedCursor);
	}

	public List<DailyWork> listWork(Employer employer, String encodedCursor) {
		String filter = "employer == '" + employer.getKey() + "'";
		return executeQueryWithCursor(DailyWork.class, filter, encodedCursor);
	}

	public List<DailyWork> listWork(Integer day, String encodedCursor) {
		String filter = "day == " + day;
		return executeQueryWithCursor(DailyWork.class, filter, encodedCursor);
	}

	public List<DailyStats> listStats(Integer day, String encodedCursor) {
		String filter = "day == " + day;
		return executeQueryWithCursor(DailyStats.class, filter, encodedCursor);
	}

	public List<DailyFights> listFights(Player player, String encodedCursor) {
		String filter = "player == '" + player.getKey() + "'";
		return executeQueryWithCursor(DailyFights.class, filter, encodedCursor);
	}

	public List<DailyFights> listFights(Integer day, String encodedCursor) {
		String filter = "day == " + day;
		return executeQueryWithCursor(DailyFights.class, filter, encodedCursor);
	}

	public List<DailyFights> listFights(PlayerGroup group, String encodedCursor) {
		String filter = "group == '" + group.getKey() + "'";
		return executeQueryWithCursor(DailyFights.class, filter, encodedCursor);
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> executeQueryWithCursor(Class<T> clazz, String filter,
			String encodedCursor) {
		Query q = queryWithCursor(clazz, filter, encodedCursor);
		return (List<T>) q.execute();
	}

	private <T> Query queryWithCursor(Class<T> clazz, String filter,
			String encodedCursor) {
		Query q = pm.newQuery(clazz);
		if (filter != null && !filter.trim().isEmpty())
			q.setFilter(filter);
		setCursor(q, encodedCursor);
		q.setRange(0, ServerConstants.QUERY_PAGE_LIMIT);
		return q;
	}

	public int getDailyRequestedFF(Player player, PlayerGroup group, int eRepDay) {
		Query q = pm.newQuery(Request.class);

		q.setFilter("player == playerParam && group == groupParam && eRepDay == eRepDayParam");
		q.declareParameters(Key.class.getName() + " playerParam, "
				+ Key.class.getName() + " groupParam, "
				+ Integer.class.getName() + " eRepDayParam");
		q.setResult("sum(requestedFF)");

		Object response = q.execute(player.getKey(), group.getKey(), eRepDay);
		return response != null ? ((Long) response).intValue() : 0;
	}

	public int getRequestedFFSum(Player player, PlayerGroup group, int fromDay) {
		Query q = pm.newQuery(Request.class);
		q.setFilter("player == playerParam && group == groupParam && eRepDay >= fromDayParam");
		q.declareParameters(Key.class.getName() + " playerParam, "
				+ Key.class.getName() + " groupParam, "
				+ Integer.class.getName() + " fromDayParam");
		q.setResult("sum(requestedFF)");
		Object response = q.execute(player.getKey(), group.getKey(), fromDay);
		return response != null ? ((Long) response).intValue() : 0;
	}

	public int getFightsFFSum(Player player, int fromDay) {
		Query q = pm.newQuery(DailyFights.class);
		q.setFilter("player == '" + player.getKey() + "' && day >= " + fromDay);
		q.setResult("sum(calculatedFfFights)");
		Long response = ((Long) q.execute());
		return response != null ? response.intValue() : 0;
	}

	@SuppressWarnings("unchecked")
	public List<DailyWork> getWork(Player dbPlayer, int fromDay) {
		Query q = pm.newQuery(DailyWork.class);
		q.setFilter("player == '" + dbPlayer.getKey() + "' && day >= "
				+ fromDay);
		return (List<DailyWork>) q.execute();
	}

	public boolean requestedRocket(Player player, PlayerGroup group, int eRepDay) {
		Query q = pm.newQuery(Request.class);
		q.setFilter("player == playerParam && group == groupParam && eRepDay == eRepDayParam && type == typeParam");
		q.declareParameters(Key.class.getName() + " playerParam, "
				+ Key.class.getName() + " groupParam, "
				+ Integer.class.getName() + " eRepDayParam, "
				+ RequestType.class.getName() + " typeParam");
		q.setUnique(true);

		Map<String, Object> parameters = new HashMap<>();
		{
			parameters.put("playerParam", player.getKey());
			parameters.put("groupParam", group.getKey());
			parameters.put("eRepDayParam", eRepDay);
			parameters.put("typeParam", ROCKET);
		}

		Object request = q.executeWithMap(parameters);
		return request != null;
	}

	public Request find(Request request) {
		try {
			return pm.getObjectById(Request.class, request.getKey());
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	public Employer find(Employer employer) {
		return pm.getObjectById(Employer.class, employer.getKey());
	}

	public Licence getLicence() {
		return (Licence) pm.getObjectById(Licence.class, KeyFactory.createKey(
				Licence.class.getSimpleName(), ServerConstants.DEFAULT_ID));
	}

	public Player find(Player player) {
		return pm.getObjectById(Player.class, player.getKey());
	}

	public DailyFights find(DailyFights fights) {
		try {
			if (fights == null || fights.getKey() == null)
				return null;

			return pm.getObjectById(DailyFights.class, fights.getKey());

		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}

	public DailyWork find(DailyWork work) {
		return pm.getObjectById(DailyWork.class, work.getKey());
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
		q.setFilter("name == nameParam");
		q.declareParameters(String.class.getName() + " nameParam");
		q.setResult("key");
		q.setUnique(true);
		Object key = q.execute(playerName);
		if (key == null)
			throw new JDOObjectNotFoundException();
		return (Key) key;
	}

	public PlayerGroup find(PlayerGroup group) {
		try {
			if (group == null || group.getKey() == null)
				return defaultGroup();

			return pm.getObjectById(PlayerGroup.class, group.getKey());
		} catch (JDOObjectNotFoundException e) {
			return defaultGroup();
		}
	}

	public Distributor find(Distributor dist) {
		try {
			if (dist == null || dist.getKey() == null)
				return defaultDistributor();

			return pm.getObjectById(Distributor.class, dist.getKey());
		} catch (JDOObjectNotFoundException e) {
			return defaultDistributor();
		}
	}

	@Cacheable
	public RegisteredUser find(User user) {
		if (user == null)
			return null;
		try {
			return pm.getObjectById(RegisteredUser.class, user.getUserId());
		} catch (JDOObjectNotFoundException e) {
			RegisteredUser regUser = new RegisteredUser(user);
			pm.makePersistent(regUser);
			return regUser;
		}
	}

	public RegisteredUser find(RegisteredUser user) {
		return pm.getObjectById(RegisteredUser.class, user.getgId());
	}

	private DistributorRoot distributorRoot() {
		return pm.getObjectById(DistributorRoot.class, distributorRootKey());
	}

	private Key distributorRootKey() {
		return KeyFactory.createKey(DistributorRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	private Distributor defaultDistributor() {
		return pm.getObjectById(Distributor.class, KeyFactory.createKey(
				distributorRootKey(), Distributor.class.getSimpleName(),
				ServerConstants.DEFAULT_ID));
	}

	private EmployerRoot employerRoot() {
		return pm.getObjectById(EmployerRoot.class, employerRootKey());
	}

	private Key employerRootKey() {
		return KeyFactory.createKey(EmployerRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	public static Key playerRootKey() {
		return KeyFactory.createKey(PlayerRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	private PlayerGroupRoot groupRoot() {
		return pm.getObjectById(PlayerGroupRoot.class, groupRootKey());
	}

	private Key groupRootKey() {
		return KeyFactory.createKey(PlayerGroupRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	private PlayerGroup defaultGroup() {
		return pm.getObjectById(PlayerGroup.class, KeyFactory.createKey(
				groupRootKey(), PlayerGroup.class.getSimpleName(),
				ServerConstants.DEFAULT_ID));
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

		if (newConfig.getPlayerReportsAccess() != null)
			dbConfig.setPlayerReportsAccess(newConfig.getPlayerReportsAccess());

		if (newConfig.getContactEmail() != null
				&& !newConfig.getContactEmail().equalsIgnoreCase(
						dbConfig.getContactEmail())) {

			startEmailChangeNotificationTask(dbConfig.getContactEmail(),
					newConfig.getContactEmail());
			dbConfig.setContactEmail(newConfig.getContactEmail());
		}

		if (newConfig.getMuConfig() != null) {
			newConfig.getMuConfig().setDistributor(
					find(newConfig.getMuConfig().getDistributor()));
			dbConfig.setMuConfig(newConfig.getMuConfig());
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
