package com.erep.dist.data;

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
		@Persistent(name = "active"), @Persistent(name = "fighters"),
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
	private Map<Division, Player>	active;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<Division, Player>	fighters;

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
		return enabledCount;
	}

	public void setEnabledCount(Integer enabledCount) {
		this.enabledCount = enabledCount;
	}

	public Map<Division, Player> getActive() {
		return active;
	}

	public void setActive(Map<Division, Player> active) {
		this.active = active;
	}

	public Map<Division, Player> getFighters() {
		return fighters;
	}

	public void setFighters(Map<Division, Player> fighters) {
		this.fighters = fighters;
	}

	public Integer getDeadCount() {
		return deadCount;
	}

	public void setDeadCount(Integer deadCount) {
		this.deadCount = deadCount;
	}

	public Integer getPermaBannedCount() {
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
		return influence;
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
		return dead;
	}

	public void setDead(Set<Player> dead) {
		this.dead = dead;
		setDeadCount(dead != null ? dead.size() : 0);
	}

	public Set<Player> getPermaBanned() {
		return permaBanned;
	}

	public void setPermaBanned(Set<Player> permaBanned) {
		this.permaBanned = permaBanned;
		setPermaBannedCount(permaBanned != null ? permaBanned.size() : 0);
	}

	public Set<Player> getTempBanned() {
		return tempBanned;
	}

	public void setTempBanned(Set<Player> tempBanned) {
		this.tempBanned = tempBanned;
		setTempBannedCount(tempBanned != null ? tempBanned.size() : 0);
	}

}
