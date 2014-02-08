package com.erep.dist.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@FetchGroups({ @FetchGroup(name = "employerListing", members = { @Persistent(name = "players") }) })
@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Employer implements HasEncodedKey {

	@PrimaryKey
	@Persistent
	private Key				key;

	@NotPersistent
	@RemoteProperty
	private String			encodedKey;

	@Persistent
	@RemoteProperty
	private String			authKey;

	@Persistent
	@RemoteProperty
	private String			erId;

	@Persistent
	@RemoteProperty
	private String			name;

	@Persistent
	@RemoteProperty
	private Date			lastUpdatedAt;

	@Unowned
	@Persistent(mappedBy = "currentEmployer")
	private Set<Player>		players;

	@Persistent
	private EmployerRoot	root;

	public Employer() {}

	public Employer(Key key) {
		setKey(key);
	}

	public Employer(String encodedKey) {
		setEncodedKey(encodedKey);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getEncodedKey() {
		if (key != null)
			return KeyFactory.keyToString(key);
		return encodedKey;
	}

	public void setEncodedKey(String encodedKey) {
		if (encodedKey != null)
			this.key = KeyFactory.stringToKey(encodedKey);
		this.encodedKey = encodedKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getErId() {
		return erId;
	}

	public void setErId(String erId) {
		this.erId = erId;
	}

	public EmployerRoot getRoot() {
		return root;
	}

	public void setRoot(EmployerRoot root) {
		this.root = root;
	}

	public Date getLastUpdatedAt() {
		return lastUpdatedAt;
	}

	public void setLastUpdatedAt(Date lastUpdatedAt) {
		this.lastUpdatedAt = lastUpdatedAt;
	}

	public String getAuthKey() {
		if (authKey == null)
			setAuthKey(getEncodedKey());
		return authKey;
	}

	public void setAuthKey(String authKey) {
		this.authKey = authKey;
	}

	public Set<Player> getPlayers() {
		if (players == null)
			resetPlayers();
		return players;
	}

	public void resetPlayers() {
		setPlayers(new HashSet<Player>());
	}

	@RemoteProperty
	public int getPlayersCount() {
		return getPlayers().size();
	}

	@Deprecated
	@RemoteProperty
	public void setPlayersCount() {
		// DWR only
	}

	public void addPlayer(Player player) {
		getPlayers().add(player);
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

}
