package com.erep.dist.data;

import java.util.Set;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroup(name = "groupListing", members = { @Persistent(name = "config") })
public class PlayerGroup implements HasTransitID {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key			key;

	@NotPersistent
	@RemoteProperty
	private Long		id;

	@Persistent
	@RemoteProperty
	private String		groupName;

	@Persistent
	@RemoteProperty
	private DistConfig	config;

	@Unowned
	@Persistent
	private Set<Player>	players;

	public Key getKey() {
		return key;
	}

	public Long getId() {
		if (key != null)
			return key.getId();
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
		return config;
	}

	public void setConfig(DistConfig config) {
		this.config = config;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public boolean addPlayer(Player player) {
		return this.players.add(player);
	}

	public boolean removePlayer(Player player) {
		return this.players.remove(player);
	}

}
