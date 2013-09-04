package com.erep.dist.data;

import java.util.HashSet;
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

import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({
		@FetchGroup(name = "playerListing", members = { @Persistent(name = "group") }),
		@FetchGroup(name = "requestAttempt", members = { @Persistent(name = "group") }) })
public class Player implements HasTransitID {

	/**
	 * Player ID on eRepublik
	 */
	@PrimaryKey
	@Persistent
	private Key					key;

	@NotPersistent
	@RemoteProperty
	private String				id;

	@Persistent
	@RemoteProperty
	private String				name;

	@Persistent
	@RemoteProperty
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String				avatar;

	@Persistent
	@RemoteProperty
	private PlayerStatus		status;

	@Unowned
	@Persistent
	@RemoteProperty
	private PlayerGroup			group;

	@Persistent
	@RemoteProperty
	private DistConfig			config;

	@Persistent
	@RemoteProperty
	private RocketQuality		rocketEnabled;

	@Persistent(mappedBy = "player")
	private Set<Request>		requests;

	@Persistent(mappedBy = "player")
	private Set<DailyStats>		dailyStats;

	@Persistent(mappedBy = "player")
	private Set<DailyFights>	dailyFights;

	public Player() {}

	public Player(Key key) {
		this.key = key;
	}

	public String getId() {
		if (key != null)
			return KeyFactory.keyToString(key);
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public RocketQuality getRocketEnabled() {
		return rocketEnabled;
	}

	public void setRocketEnabled(RocketQuality rocketEnabled) {
		this.rocketEnabled = rocketEnabled;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public PlayerGroup getGroup() {
		return group;
	}

	public void setGroup(PlayerGroup group) {
		this.group = group;
	}

	public DistConfig getConfig() {
		return config;
	}

	public void setConfig(DistConfig config) {
		this.config = config;
	}

	public Boolean getAutoSend() {
		if (config != null && config.getAutoSend() != null)
			return config.getAutoSend();
		else
			// fallback to group
			return group.getAutoSend();
	}

	public Integer getCurrentMaxDailyFF() {
		if (config != null && config.getCurrentMaxDailyFF() != null)
			return config.getCurrentMaxDailyFF();
		else
			// fallback to group
			return group.getCurrentMaxDailyFF();
	}

	public Boolean getRocketAllowed() {
		if (config != null && config.getRocketAllowed() != null)
			return config.getRocketAllowed();
		else
			// fallback to group
			return group.getRocketAllowed();
	}

	public Distributor getDistributor() {
		if (config != null && config.getDistributor() != null)
			return config.getDistributor();
		else
			// fallback to group
			return group.getDistributor();
	}

	public RequestType getAllowedType() {
		if (config != null && config.getAllowedType() != null)
			return config.getAllowedType();
		else
			// fallback to group
			return group.getAllowedType();
	}

	public ResourceQuality getAllowedWeaponQuality() {
		if (config != null && config.getAllowedWeaponQuality() != null)
			return config.getAllowedWeaponQuality();
		else
			// fallback to group
			return group.getAllowedWeaponQuality();
	}

	public Set<Request> getRequests() {
		return requests;
	}

	public void setRequests(Set<Request> requests) {
		this.requests = requests;
	}

	public void addRequest(Request request) {
		if (getRequests() == null)
			setRequests(new HashSet<Request>());
		this.getRequests().add(request);
	}

	public Set<DailyStats> getDailyStats() {
		return dailyStats;
	}

	public void setDailyStats(Set<DailyStats> stats) {
		this.dailyStats = stats;
	}

	public void addDailyStats(DailyStats stats) {
		if (getDailyStats() == null)
			setDailyStats(new HashSet<DailyStats>());
		this.getDailyStats().add(stats);
	}

	public Set<DailyFights> getDailyFights() {
		return dailyFights;
	}

	public void setDailyFights(Set<DailyFights> dailyFights) {
		this.dailyFights = dailyFights;
	}

	public void addDailyFights(DailyFights fights) {
		if (getDailyFights() == null)
			setDailyFights(new HashSet<DailyFights>());
		this.getDailyFights().add(fights);
	}
}
