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
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "Player"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroups({
		@FetchGroup(name = "playerConfigListing", members = {
				@Persistent(name = "groups"), @Persistent(name = "config"),
				@Persistent(name = "fightVerif"),
				@Persistent(name = "workVerif") }),
		@FetchGroup(name = "requestAttempt", members = {}) })
public class Player implements HasEncodedKey {

	/**
	 * Player ID on eRepublik
	 */
	@PrimaryKey
	@Persistent
	private Key					key;

	@NotPersistent
	@RemoteProperty
	private String				erId;

	@NotPersistent
	@RemoteProperty
	private String				encodedKey;

	@Persistent
	@RemoteProperty
	private String				name;

	@Persistent
	@RemoteProperty
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String				avatar;

	@Persistent
	@RemoteProperty
	private PlayerStatus		dbStatus;

	@Persistent
	@RemoteProperty
	private PlayerDistStatus	fightStatus;

	@Persistent
	@RemoteProperty
	private PlayerDistStatus	workStatus;

	@Unowned
	@Persistent
	@Deprecated
	private PlayerGroup			group;

	@Unowned
	@Persistent
	private Set<PlayerGroup>	groups;

	@Persistent(dependent = "true")
	@RemoteProperty
	private DistConfig			config;

	@Persistent(dependent = "true")
	@RemoteProperty
	private FightVerification	fightVerif;

	@Persistent(dependent = "true")
	@RemoteProperty
	private WorkVerification	workVerif;

	@Persistent
	@RemoteProperty
	private RocketQuality		rocketEnabled;

	@Persistent(mappedBy = "player")
	private Set<Request>		requests;

	@Persistent(mappedBy = "player")
	private Set<DailyStats>		dailyStats;

	@Persistent(mappedBy = "player")
	private Set<DailyFights>	dailyFights;

	@Persistent(mappedBy = "player")
	private Set<DailyWork>		dailyWork;

	@Persistent
	private PlayerRoot			root;

	public Player() {}

	public Player(Key key) {
		this.key = key;
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

	public PlayerStatus getDbStatus() {
		return dbStatus;
	}

	public void setDbStatus(PlayerStatus dbStatus) {
		this.dbStatus = dbStatus;
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

	@Deprecated
	public PlayerGroup getGroup() {
		return group;
	}

	@Deprecated
	public void setGroup(PlayerGroup group) {
		this.group = group;
	}

	public DistConfig getConfig() {
		// fallback to group
		// return config != null ? config : group.getConfig();
		return config;
	}

	public void setConfig(DistConfig config) {
		this.config = config;
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

	public PlayerDistStatus getFightStatus() {
		return fightStatus;
	}

	public void setFightStatus(PlayerDistStatus fightStatus) {
		this.fightStatus = fightStatus;
	}

	public PlayerDistStatus getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(PlayerDistStatus workStatus) {
		this.workStatus = workStatus;
	}

	public String getErId() {
		if (key != null)
			return key.getId() + "";
		return erId;
	}

	public void setErId(String erId) {
		this.erId = erId;
	}

	public Set<DailyWork> getDailyWork() {
		return dailyWork;
	}

	public void setDailyWork(Set<DailyWork> dailyWork) {
		this.dailyWork = dailyWork;
	}

	public void addDailyWork(DailyWork work) {
		if (getDailyWork() == null)
			setDailyWork(new HashSet<DailyWork>());
		this.getDailyWork().add(work);
	}

	public FightVerification getFightVerif() {
		return fightVerif;
	}

	public void setFightVerif(FightVerification fightVerif) {
		this.fightVerif = fightVerif;
	}

	public WorkVerification getWorkVerif() {
		return workVerif;
	}

	public void setWorkVerif(WorkVerification workVerif) {
		this.workVerif = workVerif;
	}

	public PlayerRoot getRoot() {
		return root;
	}

	public void setRoot(PlayerRoot root) {
		this.root = root;
	}

	public Set<PlayerGroup> getGroups() {
		return groups;
	}

	public void setGroups(Set<PlayerGroup> groups) {
		this.groups = groups;
	}

	@RemoteProperty
	public Set<String> getGroupIds() {
		if (getGroups() == null)
			return null;

		Set<String> ids = new HashSet<>();
		for (PlayerGroup group : getGroups()) {
			ids.add(group.getEncodedKey());
		}
		return ids;
	}

	@RemoteProperty
	public void setGroupIds(Set<String> groupIds) {
		if (groupIds == null)
			return;
		
		Set<PlayerGroup> groups = new HashSet<>();
		for (String id : groupIds) {
			PlayerGroup group = new PlayerGroup();
			group.setEncodedKey(id);
			groups.add(group);
		}
		setGroups(groups);
	}

}
