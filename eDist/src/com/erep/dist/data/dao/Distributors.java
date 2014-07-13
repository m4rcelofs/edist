package com.erep.dist.data.dao;

import static com.erep.dist.auth.SymmetricCipherUtils.encrypt;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import com.erep.dist.ServerConstants;
import com.erep.dist.data.Distributor;
import com.erep.dist.data.DistributorRoot;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Distributors extends GenericDAO {

	private static Logger	logger	= Logger.getLogger(Distributors.class
											.getName());

	public Distributors(PersistenceManager pm) {
		super(pm);
	}

	public boolean save(Distributor dist) {
		if (dist.getEncodedKey() != null)
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

			root().addDistributor(dist);
			pm().makePersistent(dist);

			return true;
		} catch (Exception e) {
			logger.log(Level.SEVERE,
					"Exception while trying to add a distributor", e);
			return false;
		}
	}

	private boolean edit(Distributor dist) {
		boolean result = true;
		Transaction tx = pm().currentTransaction();
		try {
			tx.begin();
			Distributor old = pm().getObjectById(Distributor.class,
					dist.getKey());
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
		Query q = pm().newQuery(Distributor.class);
		q.setFilter("root == rootParam");
		q.declareParameters(Key.class.getName() + " rootParam");
		return (List<Distributor>) q.execute(rootKey());
	}
	
	public Distributor find(Distributor dist) {
		try {
			if (dist == null || dist.getKey() == null)
				return defaultDistributor();

			return pm().getObjectById(Distributor.class, dist.getKey());
		} catch (JDOObjectNotFoundException e) {
			return defaultDistributor();
		}
	}

	private DistributorRoot root() {
		return pm().getObjectById(DistributorRoot.class, rootKey());
	}

	private Key rootKey() {
		return KeyFactory.createKey(DistributorRoot.class.getSimpleName(),
				ServerConstants.DEFAULT_ID);
	}

	public Distributor defaultDistributor() {
		return pm().getObjectById(
				Distributor.class,
				KeyFactory.createKey(rootKey(),
						Distributor.class.getSimpleName(),
						ServerConstants.DEFAULT_ID));
	}

}
