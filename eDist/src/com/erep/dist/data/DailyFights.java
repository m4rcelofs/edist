package com.erep.dist.data;

import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.FetchGroups;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.data.enums.ResourceQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({ @FetchGroup(name = "fightsListing", members = {
		@Persistent(name = "stats"), @Persistent(name = "groups") }) })
public class DailyFights {

	/**
	 * Player + day
	 */
	@PrimaryKey
	@Persistent
	private Key					key;

	@Unowned
	@Persistent
	private Player				player;

	// v0.5
	@Deprecated
	@NotPersistent
	private Player				playerInfo;

	@Unowned
	@Persistent
	@RemoteProperty
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private DailyStats			stats;

	@Unowned
	@Persistent
	@RemoteProperty
	private Set<PlayerGroup>	groups;

	@Persistent
	@RemoteProperty
	private Integer				day;

	@Persistent
	@RemoteProperty
	private Integer				calculatedFfFights;

	@Persistent
	@RemoteProperty
	private ResourceQuality		calculatedWithWeaponQuality;

	@Persistent
	@RemoteProperty
	private Long				calculatedRankDamage;

	@Persistent
	@RemoteProperty
	private PlayerDistStatus	verificationStatus;

	@Persistent
	@RemoteProperty
	private Integer				guerrillaWon;

	@Persistent
	@RemoteProperty
	private Integer				guerrillaLost;

	@Persistent
	@RemoteProperty
	private Integer				smallBombsUsed;

	@Persistent
	@RemoteProperty
	private Integer				bigBombsUsed;

	@Deprecated
	public DailyFights() {
		// DWR only
	}

	public DailyFights(Key key) {
		this.key = key;
	}

	public DailyFights(Player dbPlayer, Integer day) {
		this(KeyFactory.createKey(dbPlayer.getKey(),
				DailyFights.class.getSimpleName(), day));
		setPlayer(dbPlayer);
		setDay(day);
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

	public PlayerDistStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(PlayerDistStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

	public Set<PlayerGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<PlayerGroup> groups) {
		this.groups = groups;
	}

	public DailyStats getStats() {
		return stats;
	}

	public void setStats(DailyStats stats) {
		this.stats = stats;
	}

	public Long getCalculatedRankDamage() {
		return calculatedRankDamage;
	}

	public void setCalculatedRankDamage(Long calculatedRankDamage) {
		this.calculatedRankDamage = calculatedRankDamage;
	}

	public Integer getGuerrillaWon() {
		return guerrillaWon;
	}

	public void setGuerrillaWon(Integer guerrillaWon) {
		this.guerrillaWon = guerrillaWon;
	}

	public Integer getGuerrillaLost() {
		return guerrillaLost;
	}

	public void setGuerrillaLost(Integer guerrillaLost) {
		this.guerrillaLost = guerrillaLost;
	}

	public Integer getSmallBombsUsed() {
		return smallBombsUsed;
	}

	public void setSmallBombsUsed(Integer smallBombsUsed) {
		this.smallBombsUsed = smallBombsUsed;
	}

	public Integer getBigBombsUsed() {
		return bigBombsUsed;
	}

	public void setBigBombsUsed(Integer bigBombsUsed) {
		this.bigBombsUsed = bigBombsUsed;
	}

}
