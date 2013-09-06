package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erep.dist.data.enums.ResourceQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DailyFights {

	/**
	 * Player + day
	 */
	@PrimaryKey
	@Persistent
	private Key				key;

	@Persistent
	private Player			player;

	@Unowned
	@Persistent
	private PlayerGroup		group;

	@Persistent
	private Integer			day;

	@Persistent
	private Integer			calculatedFfFights;

	@Persistent
	private ResourceQuality	calculatedWithWeaponQuality;

	public DailyFights() {}

	public DailyFights(Key key) {
		this.key = key;
	}

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

	public Integer getCalculatedFfFights() {
		return calculatedFfFights;
	}

	public void setCalculatedFfFights(Integer calculatedFfFights) {
		this.calculatedFfFights = calculatedFfFights;
	}

	public ResourceQuality getCalculatedWithWeaponQuality() {
		return calculatedWithWeaponQuality;
	}

	public void setCalculatedWithWeaponQuality(
			ResourceQuality calculatedWithWeaponQuality) {
		this.calculatedWithWeaponQuality = calculatedWithWeaponQuality;
	}

	public PlayerGroup getGroup() {
		return group;
	}

	public void setGroup(PlayerGroup group) {
		this.group = group;
	}
}
