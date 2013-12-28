package com.erep.dist.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Vanilla entity to force strong consistency on Distributors <br />
 * All Distributors should have this entity as ancestor
 * 
 * @author Marcelo
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DistributorRoot implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent(mappedBy="root")
	public List<Distributor>	distributors;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public List<Distributor> getDistributors() {
		return distributors;
	}

	public void setDistributors(List<Distributor> distributors) {
		this.distributors = distributors;
	}

	public void addDistributor(Distributor distributor) {
		if (getDistributors() == null)
			setDistributors(new ArrayList<Distributor>());
		getDistributors().add(distributor);
	}

}
