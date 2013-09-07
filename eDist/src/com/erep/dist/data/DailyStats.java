package com.erep.dist.data;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DailyStats {

	/**
	 * Player + day
	 */
	@PrimaryKey
	@Persistent
	private Key		key;

	@Persistent
	private Player	player;

	@Persistent
	@RemoteProperty
	private Integer	day;

	@Persistent
	@RemoteProperty
	private Long	xp;

	@Persistent
	@RemoteProperty
	private Double	strength;

	@Persistent
	@RemoteProperty
	private Long	rank;

	@Persistent
	@RemoteProperty
	private Integer	maxHit;
	
	@Persistent
	@RemoteProperty
	private Date collectedAt;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Long getXp() {
		return xp;
	}

	public void setXp(Long xp) {
		this.xp = xp;
	}

	public Double getStrength() {
		return strength;
	}

	public void setStrength(Double strength) {
		this.strength = strength;
	}

	public Long getRank() {
		return rank;
	}

	public void setRank(Long rank) {
		this.rank = rank;
	}

	public Integer getMaxHit() {
		return maxHit;
	}

	public void setMaxHit(Integer maxHit) {
		this.maxHit = maxHit;
	}

	public Date getCollectedAt() {
		return collectedAt;
	}

	public void setCollectedAt(Date collectedAt) {
		this.collectedAt = collectedAt;
	}

}
