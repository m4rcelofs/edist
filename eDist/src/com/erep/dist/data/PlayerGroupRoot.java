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
 * Vanilla entity to force strong consistency on PlayerGroup <br />
 * All PlayerGroups should have this entity as ancestor
 * 
 * @author Marcelo
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PlayerGroupRoot implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent(mappedBy = "root")
	protected List<PlayerGroup>	groups;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public List<PlayerGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<PlayerGroup> groups) {
		this.groups = groups;
	}

	public void addGroup(PlayerGroup group) {
		if (getGroups() == null)
			setGroups(new ArrayList<PlayerGroup>());
		getGroups().add(group);
	}

	public int getGroupsCount() {
		if (getGroups() != null)
			return getGroups().size() + 1; // default is not on the collection
		return 0;
	}

}
