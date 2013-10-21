package com.erep.dist.data;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({ @FetchGroup(name = "configListing", members = { @Persistent(name = "config") }) })
public class PlayerGroup implements HasEncodedKey {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key				key;

	@NotPersistent
	@RemoteProperty
	private String			encodedKey;

	@Persistent
	@RemoteProperty
	private String			groupName;

	@Persistent(dependent = "true")
	@RemoteProperty
	private DistConfig		config;

	@Persistent
	private PlayerGroupRoot	root;

	public Key getKey() {
		return key;
	}

	public String getEncodedKey() {
		if (key != null)
			return KeyFactory.keyToString(key);
		return encodedKey;
	}

	public void setEncodedKey(String id) {
		if (id != null)
			this.key = KeyFactory.stringToKey(id);
		this.encodedKey = id;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public DistConfig getConfig() {
		// fallback to default
		return config;
	}

	public void setConfig(DistConfig config) {
		this.config = config;
	}

	public PlayerGroupRoot getRoot() {
		return root;
	}

	public void setRoot(PlayerGroupRoot root) {
		this.root = root;
	}
}
