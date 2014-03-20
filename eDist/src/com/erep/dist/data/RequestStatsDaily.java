package com.erep.dist.data;

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

import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.common.collect.HashBasedTable;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({ @FetchGroup(name = "monthlyListing", members = {
		@Persistent(name = "players"), @Persistent(name = "rockets"),
		@Persistent(name = "requests") }) })
public class RequestStatsDaily implements HasEncodedKey {

	// monthly key (parent) + day
	@PrimaryKey
	@Persistent
	private Key														key;

	@RemoteProperty
	@NotPersistent
	private String													encodedKey;

	@RemoteProperty
	@Persistent
	private Integer													day;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>												players;

	@RemoteProperty
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer													ffs;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<RocketQuality, Integer>								rockets;

	@RemoteProperty
	@Persistent(serialized = "true")
	private Map<RequestType, Integer>								requests;

	@Persistent(serialized = "true")
	private HashBasedTable<ResourceType, ResourceQuality, Integer>	itemsSent;

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

	public Integer getFfs() {
		return ffs;
	}

	public void setFfs(Integer ffs) {
		this.ffs = ffs;
	}

	public Map<RocketQuality, Integer> getRockets() {
		return rockets;
	}

	public void setRockets(Map<RocketQuality, Integer> rockets) {
		this.rockets = rockets;
	}

	public Map<RequestType, Integer> getRequests() {
		return requests;
	}

	public void setRequests(Map<RequestType, Integer> requests) {
		this.requests = requests;
	}

	public HashBasedTable<ResourceType, ResourceQuality, Integer> getItemsSent() {
		return itemsSent;
	}

	public void setItemsSent(
			HashBasedTable<ResourceType, ResourceQuality, Integer> itemsSent) {
		this.itemsSent = itemsSent;
	}

	public Set<Player> getPlayers() {
		if (players == null)
			players = new HashSet<Player>();
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	@RemoteProperty
	public int getPlayersCount() {
		return getPlayers().size();
	}

	@Deprecated
	public void setPlayersCount() {
		// DWR only
	}

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
		RequestStatsDaily other = (RequestStatsDaily) obj;
		if (key == null) {
			if (other.key != null)
				return false;
		} else
			if (!key.equals(other.key))
				return false;
		return true;
	}

}
