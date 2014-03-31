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
@FetchGroups({ @FetchGroup(name = "monthlyListing", members = { @Persistent(name = "influence") }) })
public class PlayerStatsMonthly implements HasEncodedKey {

	// YYYY-MM
	@PrimaryKey
	@Persistent
	private Key						key;

	@RemoteProperty
	@NotPersistent
	private String					encodedKey;

	// YYYY-MM
	@Persistent
	private String					yearMonth;

	@RemoteProperty
	@Persistent
	private Set<PlayerStatsDaily>	dailies;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<Division, Long>		influence;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>				dead;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					deadCount;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>				tempBanned;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					tempBannedCount;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>				permaBanned;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer					permaBannedCount;

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

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Set<PlayerStatsDaily> getDailies() {
		if (dailies == null)
			setDailies(new HashSet<PlayerStatsDaily>());
		return dailies;
	}

	public void setDailies(Set<PlayerStatsDaily> dailies) {
		this.dailies = dailies;
	}

	public void addDaily(PlayerStatsDaily daily) {
		if (getDailies().contains(daily))
			return;

		getDailies().add(daily);

		addInfluence(daily.getInfluence());

		getDead().addAll(daily.getDead());
		setDeadCount(getDead().size());

		getTempBanned().addAll(daily.getTempBanned());
		setTempBannedCount(getTempBanned().size());

		getPermaBanned().addAll(daily.getPermaBanned());
		setPermaBannedCount(getPermaBanned().size());
	}

	private void addInfluence(Map<Division, Long> influence) {
		for (Division div : influence.keySet()) {
			Long original = getInfluence().get(div);
			if (original == null)
				original = 0l;
			original += influence.get(div);
			getInfluence().put(div, original);
		}
	}

	public Map<Division, Long> getInfluence() {
		if (influence == null)
			setInfluence(new HashMap<Division, Long>());
		return influence;
	}

	public void setInfluence(Map<Division, Long> influence) {
		this.influence = influence;
	}

	public Integer getDeadCount() {
		return deadCount;
	}

	public void setDeadCount(Integer deadCount) {
		this.deadCount = deadCount;
	}

	public Integer getTempBannedCount() {
		return tempBannedCount;
	}

	public void setTempBannedCount(Integer tempBannedCount) {
		this.tempBannedCount = tempBannedCount;
	}

	public Integer getPermaBannedCount() {
		return permaBannedCount;
	}

	public void setPermaBannedCount(Integer permaBannedCount) {
		this.permaBannedCount = permaBannedCount;
	}

	public Set<Player> getDead() {
		if (dead == null)
			setDead(new HashSet<Player>());
		return dead;
	}

	public void setDead(Set<Player> dead) {
		this.dead = dead;
	}

	public Set<Player> getTempBanned() {
		if (tempBanned == null)
			setTempBanned(new HashSet<Player>());
		return tempBanned;
	}

	public void setTempBanned(Set<Player> tempBanned) {
		this.tempBanned = tempBanned;
	}

	public Set<Player> getPermaBanned() {
		if (permaBanned == null)
			setPermaBanned(new HashSet<Player>());
		return permaBanned;
	}

	public void setPermaBanned(Set<Player> permaBanned) {
		this.permaBanned = permaBanned;
	}

}
