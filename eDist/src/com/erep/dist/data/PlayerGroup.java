package com.erep.dist.data;

import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PlayerGroup {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key			id;

	@Persistent
	private String		groupName;

	@Persistent
	private DistConfig	config;

	@Unowned
	@Persistent
	private Set<Player>	players;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
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
