package com.erep.dist.data;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;

import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;
import com.google.common.collect.HashBasedTable;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RequestStatsMonthly {

	// YYYY-MM
	@PrimaryKey
	@Persistent
	private Key														key;

	// YYYY-MM
	@Persistent
	private String													yearMonth;

	@Unowned
	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<Player>												players;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Integer													ffs;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Map<RocketQuality, Integer>								rockets;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Map<RequestType, Integer>								requests;

	@Persistent(defaultFetchGroup = "false", serialized = "true")
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private HashBasedTable<ResourceType, ResourceQuality, Integer>	itemsSent;

	@Persistent
	private Set<RequestStatsDaily>									dailies;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
		this.yearMonth = key.getName();
	}

	public String getYearMonth() {
		return yearMonth;
	}

	public void setYearMonth(String yearMonth) {
		this.yearMonth = yearMonth;
	}

	public Set<Player> getPlayers() {
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
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

	public Set<RequestStatsDaily> getDailies() {
		return dailies;
	}

	public void setDailies(Set<RequestStatsDaily> dailies) {
		this.dailies = dailies;
	}

	public void addDaily(RequestStatsDaily daily) {
		if (getDailies() == null)
			setDailies(new HashSet<RequestStatsDaily>());
		getDailies().add(daily);

		addFFs(daily.getFfs());
		addItems(daily.getItemsSent());
		addPlayers(daily.getPlayers());
		addRequests(daily.getRequests());
		addRockets(daily.getRockets());
	}

	private void addFFs(Integer ffs) {
		if (getFfs() == null)
			setFfs(0);
		setFfs(getFfs() + ffs);
	}

	private void addItems(
			HashBasedTable<ResourceType, ResourceQuality, Integer> map) {
		for (ResourceType type : ResourceType.values())
			for (ResourceQuality quality : ResourceQuality.values()) {
				Integer toAdd = map.get(type, quality);
				if (toAdd != null) {
					Integer current = getItemsSent().get(type, quality);
					if (current == null)
						current = 0;
					getItemsSent().put(type, quality, current + toAdd);
				}
			}
	}

	private void addPlayers(Set<Player> players) {
		if (getPlayers() == null)
			setPlayers(new HashSet<Player>());
		getPlayers().addAll(players);
	}

	private void addRequests(Map<RequestType, Integer> map) {
		for (RequestType type : map.keySet()) {
			Integer toAdd = map.get(type);
			Integer current = getRequests().get(type);
			if (current == null)
				current = 0;
			getRequests().put(type, current + toAdd);
		}
	}

	private void addRockets(Map<RocketQuality, Integer> map) {
		for (RocketQuality quality : map.keySet()) {
			Integer toAdd = map.get(quality);
			Integer current = getRockets().get(quality);
			if (current == null)
				current = 0;
			getRockets().put(quality, current + toAdd);
		}
	}

}
