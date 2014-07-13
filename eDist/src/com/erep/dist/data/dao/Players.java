package com.erep.dist.data.dao;

import static com.erep.dist.data.enums.PlayerStatus.ERROR;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.Extent;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.erep.dist.DataAccessObject;
import com.erep.dist.ServerConstants;
import com.erep.dist.cache.Cacheable;
import com.erep.dist.data.Employer;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.PlayerRoot;
import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.instances.frontend.tasks.GetUserInfoTask;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class Players extends GenericDAO {

	DataAccessObject			dao;
	private final Distributors	distributors;

	public Players(PersistenceManager pm) {
		super(pm);
		dao = new DataAccessObject(pm);
		distributors = new Distributors(pm);
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
		Transaction tx = pm().currentTransaction();
		boolean result = true;
		try {
			tx.begin();
			Player oldPlayer = pm()
					.getObjectById(Player.class, player.getKey());
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

		Key key = createKey(newPlayer.getErId());
		newPlayer.setKey(key);

		if (newPlayer.getGroups() != null && !newPlayer.getGroups().isEmpty()) {
			Set<PlayerGroup> groups = new HashSet<>();
			for (PlayerGroup group : newPlayer.getGroups())
				groups.add(dao.find(group));
			newPlayer.setGroups(groups);
		} else
			newPlayer.addGroup(dao.defaultGroup());

		root().addPlayer(newPlayer);

		createGetUserInfoTask(newPlayer);

		return true;
	}

	private void createGetUserInfoTask(Player dbPlayer) {
		DeferredTask task = new GetUserInfoTask(dbPlayer);
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");
		queue.add(withPayload(task));
	}

	public Key createKey(String erId) {
		return KeyFactory.createKey(rootKey(), Player.class.getSimpleName(),
				Long.valueOf(erId));
	}

	private boolean edit(Player old, Player newest) {
		if (newest.getConfig() != null) {
			newest.getConfig().setDistributor(
					distributors.find(newest.getConfig().getDistributor()));
		}
		old.setConfig(newest.getConfig());
		old.setFightVerif(newest.getFightVerif());
		old.setWorkVerif(newest.getWorkVerif());

		old.setRocketEnabled(newest.getRocketEnabled());
		old.setDbStatus(newest.getDbStatus());
		old.setFightStatus(newest.getFightStatus());
		old.setWorkStatus(newest.getWorkStatus());

		if (newest.getCurrentEmployerName() == null) {
			if (old.getCurrentEmployer() != null)
				old.getCurrentEmployer().getPlayers().remove(old);
			old.setCurrentEmployer(null);
		}

		Set<PlayerGroup> groups = new HashSet<>();
		if (newest.getGroups() == null || newest.getGroups().isEmpty()) {
			groups.add(dao.defaultGroup());
		} else {
			for (PlayerGroup group : newest.getGroups())
				groups.add(dao.find(group));
		}
		old.setGroups(groups);

		return true;
	}

	/**
	 * Don't forget to closeAll() after you are done!
	 * 
	 * @return
	 */
	public Extent<Player> listAll() {
		Extent<Player> players = pm().getExtent(Player.class);
		return players;
	}

	@SuppressWarnings("unchecked")
	public List<Player> list(String encodedCursor) {
		Query q = queryWithCursor(Player.class, "root == rootParam",
				encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<Player>) q.execute(rootKey());
	}

	@SuppressWarnings("unchecked")
	public List<Player> list(PlayerGroup group, String encodedCursor) {
		Query q = queryWithCursor(Player.class,
				"root == rootParam && groups.contains(groupParam)",
				encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam, "
				+ Key.class.getName() + " groupParam");
		return (List<Player>) q.execute(rootKey(), group.getKey());
	}

	@SuppressWarnings("unchecked")
	public List<Player> list(Employer employer, String encodedCursor) {
		Query q = queryWithCursor(Player.class,
				"root == rootParam && currentEmployer == employerParam",
				encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam, "
				+ Key.class.getName() + " employerParam");
		return (List<Player>) q.execute(rootKey(), employer.getKey());
	}

	@SuppressWarnings("unchecked")
	public List<Player> listByFight(PlayerDistStatus status,
			String encodedCursor) {
		Query q = queryWithCursor(Player.class,
				"root == rootParam && fightStatus == statusParam",
				encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam, "
				+ PlayerDistStatus.class.getName() + " statusParam");
		return (List<Player>) q.execute(rootKey(), status);
	}

	@SuppressWarnings("unchecked")
	public List<Player> list(PlayerStatus status, String encodedCursor) {
		Query q = queryWithCursor(Player.class,
				"root == rootParam && dbStatus == dbStatusParam", encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam, "
				+ PlayerStatus.class.getName() + " dbStatusParam");
		return (List<Player>) q.execute(rootKey(), status);
	}

	@SuppressWarnings("unchecked")
	public List<Player> listByWork(PlayerDistStatus status, String encodedCursor) {
		Query q = queryWithCursor(Player.class,
				"root == rootParam && workStatus == statusParam", encodedCursor);
		q.declareParameters(Key.class.getName() + " rootParam, "
				+ PlayerDistStatus.class.getName() + " statusParam");
		return (List<Player>) q.execute(rootKey(), status);
	}

	public Player find(Player player) {
		return pm().getObjectById(Player.class, player.getKey());
	}

	public boolean remove(Player player) {
		Player dbPlayer = find(player);

		if (dbPlayer.getDbStatus() != ERROR)
			return false;

		pm().deletePersistent(dbPlayer);
		return true;
	}

	@Cacheable
	public Key findPlayerKey(String playerName) {
		Query q = pm().newQuery(Player.class);
		q.setFilter("nameNormalized == nameNormalizedParam");
		q.declareParameters(String.class.getName() + " nameNormalizedParam");
		q.setResult("key");
		q.setUnique(true);
		Object key = q.execute(playerName.toLowerCase());
		if (key == null)
			throw new JDOObjectNotFoundException();
		return (Key) key;
	}

	public PlayerRoot root() {
		return pm().getObjectById(PlayerRoot.class, rootKey());
	}

	public static Key rootKey() {
		return KeyFactory.createKey(PlayerRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}
}
