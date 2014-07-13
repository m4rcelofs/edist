package com.erep.dist.data.dao;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.List;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.DataAccessObject;
import com.erep.dist.ServerConstants;
import com.erep.dist.cache.CacheFacade;
import com.erep.dist.cache.Cacheable;
import com.erep.dist.data.Licence;
import com.erep.dist.data.RegisteredUser;
import com.erep.dist.data.ServerConfig;
import com.erep.dist.instances.frontend.tasks.NotifyEmailChangeTask;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.users.User;

public class Server extends GenericDAO {

	public Server(PersistenceManager pm) {
		super(pm);
	}

	@SuppressWarnings("unchecked")
	public List<RegisteredUser> listUsers(String encodedCursor) {
		Query q = queryWithCursor(RegisteredUser.class, null, encodedCursor);
		return (List<RegisteredUser>) q.execute();
	}

	public Licence getLicence() {
		return (Licence) pm().getObjectById(
				Licence.class,
				KeyFactory.createKey(Licence.class.getSimpleName(),
						ServerConstants.DEFAULT_ID));
	}

	@Cacheable
	public RegisteredUser find(User user) {
		if (user == null)
			return null;
		try {
			return pm().getObjectById(RegisteredUser.class, user.getUserId());
		} catch (JDOObjectNotFoundException e) {
			RegisteredUser regUser = new RegisteredUser(user);
			pm().makePersistent(regUser);
			return regUser;
		}
	}

	public RegisteredUser find(RegisteredUser user) {
		return pm().getObjectById(RegisteredUser.class, user.getgId());
	}

	public ServerConfig getConfig() {
		return pm().getObjectById(ServerConfig.class,
				ServerConstants.DEFAULT_ID);
	}

	public boolean save(ServerConfig newConfig) {
		ServerConfig dbConfig = getConfig();

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

		if (newConfig.getConfig() != null) {
			// FIXME
			newConfig.getConfig().setDistributor(
					new DataAccessObject(pm()).find(newConfig.getConfig()
							.getDistributor()));
			dbConfig.setConfig(newConfig.getConfig());
		}

		if (newConfig.getWorkVerif() != null)
			dbConfig.setWorkVerif(newConfig.getWorkVerif());

		if (newConfig.getFightVerif() != null)
			dbConfig.setFightVerif(newConfig.getFightVerif());

		// refresh the entity to create the fields if needed
		dbConfig.setAvatarHistory(dbConfig.isAvatarHistory());
		dbConfig.setShowDistributorInventory(dbConfig
				.isShowDistributorInventory());

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

		CacheFacade.remove(getClass().getName() + ".find(User="
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
