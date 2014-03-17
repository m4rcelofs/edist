package com.erep.dist.data;

import java.util.HashMap;
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

	@Persistent(defaultFetchGroup = "true", serialized = "true")
	private Map<RocketQuality, Integer>								rockets;

	@Persistent(defaultFetchGroup = "true", serialized = "true")
	private Map<RequestType, Integer>								requests;

	@Persistent(defaultFetchGroup = "false", serialized = "true")
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
		if (players == null)
			players = new HashSet<>();
		return players;
	}

	public void setPlayers(Set<Player> players) {
		this.players = players;
	}

	public Integer getFfs() {
		if (ffs == null)
			ffs = 0;
		return ffs;
	}

	public void setFfs(Integer ffs) {
		this.ffs = ffs;
	}

	public Map<RocketQuality, Integer> getRockets() {
		if (rockets == null)
			rockets = new HashMap<>();
		return rockets;
	}

	public void setRockets(Map<RocketQuality, Integer> rockets) {
		this.rockets = rockets;
	}

	public Map<RequestType, Integer> getRequests() {
		if (requests == null)
			requests = new HashMap<>();
		return requests;
	}

	public void setRequests(Map<RequestType, Integer> requests) {
		this.requests = requests;
	}

	public HashBasedTable<ResourceType, ResourceQuality, Integer> getItemsSent() {
		if (itemsSent == null)
			itemsSent = HashBasedTable.create();
		return itemsSent;
	}

	public void setItemsSent(
			HashBasedTable<ResourceType, ResourceQuality, Integer> itemsSent) {
		this.itemsSent = itemsSent;
	}

	public Set<RequestStatsDaily> getDailies() {
		if (dailies == null)
			dailies = new HashSet<RequestStatsDaily>();
		return dailies;
	}

	public void setDailies(Set<RequestStatsDaily> dailies) {
		this.dailies = dailies;
	}

	public void addDaily(RequestStatsDaily daily) {
		
		if(getDailies().contains(daily))
			return;
		
		getDailies().add(daily);

		addFFs(daily.getFfs());
		addItems(daily.getItemsSent());
		addPlayers(daily.getPlayers());
		addRequests(daily.getRequests());
		addRockets(daily.getRockets());
	}

	private void addFFs(Integer ffs) {
		setFfs(getFfs() + ffs);
	}

	private void addItems(
			HashBasedTable<ResourceType, ResourceQuality, Integer> map) {
		HashBasedTable<ResourceType, ResourceQuality, Integer> copy = HashBasedTable
				.create(getItemsSent());
		for (ResourceType type : ResourceType.values())
			for (ResourceQuality quality : ResourceQuality.values()) {
				Integer toAdd = map.get(type, quality);
				if (toAdd != null) {
					Integer current = copy.get(type, quality);
					if (current == null)
						current = 0;
					copy.put(type, quality, current + toAdd);
				}
			}
		setItemsSent(copy);
	}

	private void addPlayers(Set<Player> players) {
		getPlayers().addAll(players);
	}

	private void addRequests(Map<RequestType, Integer> map) {
		Map<RequestType, Integer> copy = new HashMap<>(getRequests());
		for (RequestType type : map.keySet()) {
			Integer toAdd = map.get(type);
			Integer current = copy.get(type);
			if (current == null)
				current = 0;
			copy.put(type, current + toAdd);
		}
		setRequests(copy);
	}

	private void addRockets(Map<RocketQuality, Integer> map) {
		Map<RocketQuality, Integer> copy = new HashMap<>(getRockets());
		for (RocketQuality quality : map.keySet()) {
			Integer toAdd = map.get(quality);
			Integer current = copy.get(quality);
			if (current == null)
				current = 0;
			copy.put(quality, current + toAdd);
		}
		setRockets(copy);
	}

}
