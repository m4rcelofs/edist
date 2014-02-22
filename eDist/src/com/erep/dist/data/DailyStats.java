package com.erep.dist.data;

import java.util.Date;
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
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.enums.CountryCode;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "DailyStats"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({ @FetchGroup(name = "statsListing", members = {
		@Persistent(name = "groups"), @Persistent(name = "day"),
		@Persistent(name = "xp"), @Persistent(name = "strength"),
		@Persistent(name = "rank"), @Persistent(name = "maxHit"),
		@Persistent(name = "collectedAt") }) })
public class DailyStats {

	/**
	 * Player + day
	 */
	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent
	private Player				player;

	// v0.5
	@Deprecated
	@NotPersistent
	private Player				playerInfo;

	@Persistent
	@RemoteProperty
	private String				name;

	@Persistent
	@RemoteProperty
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String				avatar;

	// keep a copy, never know when it might be useful
	@Unowned
	@Persistent(defaultFetchGroup = "false")
	private Avatar				avatarKey;

	@Unowned
	@Persistent
	@RemoteProperty
	private Set<PlayerGroup>	groups;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private Integer				day;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private Long				xp;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private Double				strength;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private Long				rank;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private Integer				maxHit;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private Date				collectedAt;

	@Persistent
	@RemoteProperty
	private boolean				isDead;

	@Persistent
	@RemoteProperty
	private boolean				isPermaBan;

	@Persistent
	@RemoteProperty
	private boolean				isTempBan;

	@Persistent
	@RemoteProperty
	private String				banTip;

	@Persistent
	@RemoteProperty
	private String				militaryUnit;

	@Persistent
	@RemoteProperty
	private CountryCode			citizenship;

	public DailyStats() {}

	public DailyStats(Player player, Integer day) {
		this(KeyFactory.createKey(player.getKey(),
				DailyStats.class.getSimpleName(), day));
		setDay(day);
		setPlayer(player);
	}

	public DailyStats(Key key) {
		setKey(key);
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
		setGroups(player.getGroups());
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

	public Set<PlayerGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<PlayerGroup> groups) {
		this.groups = groups;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@RemoteProperty
	public Integer geterId() {
		return (int) getKey().getParent().getId();
	}

	public Avatar getAvatarKey() {
		return avatarKey;
	}

	public void setAvatarKey(Avatar avatarKey) {
		this.avatarKey = avatarKey;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isPermaBan() {
		return isPermaBan;
	}

	public void setPermaBan(boolean isPermaBan) {
		this.isPermaBan = isPermaBan;
	}

	public boolean isTempBan() {
		return isTempBan;
	}

	public void setTempBan(boolean isTempBan) {
		this.isTempBan = isTempBan;
	}

	public String getBanTip() {
		return banTip;
	}

	public void setBanTip(String banTip) {
		this.banTip = banTip;
	}

	public String getMilitaryUnit() {
		return militaryUnit;
	}

	public void setMilitaryUnit(String militaryUnit) {
		this.militaryUnit = militaryUnit;
	}

	public CountryCode getCitizenship() {
		return citizenship;
	}

	public void setCitizenship(CountryCode citizenship) {
		this.citizenship = citizenship;
	}

}
