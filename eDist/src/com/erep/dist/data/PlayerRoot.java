package com.erep.dist.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Vanilla entity to force strong consistency on Player<br />
 * All Players should have this entity as ancestor
 * 
 * @author Marcelo
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class PlayerRoot {

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent(mappedBy = "root")
	public List<Player>	players;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	public void addPlayer(Player player) {
		if (getPlayers() == null)
			setPlayers(new ArrayList<Player>());
		getPlayers().add(player);
	}

}
