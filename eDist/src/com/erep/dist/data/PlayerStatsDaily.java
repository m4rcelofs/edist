package com.erep.dist.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

import com.erep.dist.data.enums.Division;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({ @FetchGroup(name = "monthlyListing", members = {
		@Persistent(name = "activeCount"), @Persistent(name = "fightersCount"),
		@Persistent(name = "influence") }) })
public class PlayerStatsDaily implements HasEncodedKey {

	// monthly key (parent) + day
	@PrimaryKey
	@Persistent
	private Key						key;

	@RemoteProperty
	@NotPersistent
	private String					encodedKey;

	@RemoteProperty
	@Persistent
	private Integer					day;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					enabledCount;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<Division, Integer>	activeCount;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<Division, Integer>	fightersCount;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>				dead;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					deadCount;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>				permaBanned;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					permaBannedCount;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>				tempBanned;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					tempBannedCount;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<Division, Long>		influence;

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

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getEnabledCount() {
		if (enabledCount == null)
			return 0;
		return enabledCount;
	}

	public void addEnabled(DailyStats enabled) {
		setEnabledCount(getEnabledCount() + 1);
	}

	public void setEnabledCount(Integer enabledCount) {
		this.enabledCount = enabledCount;
	}

	public Map<Division, Integer> getActiveCount() {
		if (activeCount == null)
			setActiveCount(new HashMap<Division, Integer>());
		return activeCount;
	}

	public void addActive(DailyStats player) {
		if (player == null)
			return;
		Integer original = getActiveCount().get(player.getDiv());
		if (original == null)
			original = 0;
		getActiveCount().put(player.getDiv(), original + 1);
	}

	public void setActiveCount(Map<Division, Integer> activeCount) {
		this.activeCount = activeCount;
	}

	public Map<Division, Integer> getFightersCount() {
		if (fightersCount == null)
			setFightersCount(new HashMap<Division, Integer>());
		return fightersCount;
	}

	public void addFighter(DailyStats player) {
		if (player == null)
			return;
		Integer original = getFightersCount().get(player.getDiv());
		if (original == null)
			original = 0;
		getFightersCount().put(player.getDiv(), original + 1);
		addActive(player);
	}

	public void setFightersCount(Map<Division, Integer> fightersCount) {
		this.fightersCount = fightersCount;
	}

	public Integer getDeadCount() {
		return deadCount;
	}

	public void setDeadCount(Integer deadCount) {
		this.deadCount = deadCount;
	}

	public Integer getPermaBannedCount() {
		if (permaBannedCount == null)
			setPermaBannedCount(0);
		return permaBannedCount;
	}

	public void setPermaBannedCount(Integer permaBannedCount) {
		this.permaBannedCount = permaBannedCount;
	}

	public Integer getTempBannedCount() {
		return tempBannedCount;
	}

	public void setTempBannedCount(Integer tempBannedCount) {
		this.tempBannedCount = tempBannedCount;
	}

	public Map<Division, Long> getInfluence() {
		if (influence == null)
			setInfluence(new HashMap<Division, Long>());
		return influence;
	}

	public void addInfluence(DailyStats player, Long influenceToAdd) {
		if (player == null || influenceToAdd == null || influenceToAdd == 0)
			return;
		Long original = getInfluence().get(player.getDiv());
		if (original == null)
			original = 0l;
		getInfluence().put(player.getDiv(), original += influenceToAdd);
	}

	public void setInfluence(Map<Division, Long> influence) {
		this.influence = influence;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlayerStatsDaily other = (PlayerStatsDaily) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else
			if (!key.equals(other.key))
				return false;
		return true;
	}

	public Set<Player> getDead() {
		if (dead == null)
			setDead(new HashSet<Player>());
		return dead;
	}

	public void addDead(Player player) {
		getDead().add(player);
		setDeadCount(getDead().size());
	}

	public void setDead(Set<Player> dead) {
		this.dead = dead;
		setDeadCount(dead != null ? dead.size() : 0);
	}

	public Set<Player> getPermaBanned() {
		if (permaBanned == null)
			setPermaBanned(new HashSet<Player>());
		return permaBanned;
	}

	public void addPermaBanned(Player player) {
		getPermaBanned().add(player);
		setPermaBannedCount(getPermaBanned().size());
	}

	public void setPermaBanned(Set<Player> permaBanned) {
		this.permaBanned = permaBanned;
		setPermaBannedCount(permaBanned != null ? permaBanned.size() : 0);
	}

	public Set<Player> getTempBanned() {
		if (tempBanned == null)
			setTempBanned(new HashSet<Player>());
		return tempBanned;
	}

	public void addTempBanned(Player player) {
		getTempBanned().add(player);
		setTempBannedCount(getTempBanned().size());
	}

	public void setTempBanned(Set<Player> tempBanned) {
		this.tempBanned = tempBanned;
		setTempBannedCount(tempBanned != null ? tempBanned.size() : 0);
	}

}
