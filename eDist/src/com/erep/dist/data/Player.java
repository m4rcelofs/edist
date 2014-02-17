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
				@Persistent(name = "workVerif"),
				@Persistent(name = "currentEmployer") }),
		@FetchGroup(name = "requestIdentify", members = { @Persistent(name = "groups") }) })
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
	// lower case
	private String				nameNormalized;

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
	@RemoteProperty
	private Set<PlayerGroup>	groups;

	@Unowned
	@Persistent
	private Employer			currentEmployer;

	@NotPersistent
	@RemoteProperty
	private String				currentEmployerName;

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
		if (name != null)
			setNameNormalized(name.toLowerCase());
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

	public DistConfig getConfig() {
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

	public void addGroup(PlayerGroup group) {
		if (getGroups() == null)
			setGroups(new HashSet<PlayerGroup>());
		getGroups().add(group);
	}

	@Deprecated
	public void setGroupIds(Set<String> ids) {
		// this is here because otherwise DWR won't include the property on the
		// interface file
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

	public boolean isInGroup(PlayerGroup group) {
		if (getGroups() == null)
			return false;

		for (PlayerGroup pGroup : getGroups())
			if (pGroup.getKey().equals(group.getKey()))
				return true;

		return false;
	}

	public String getNameNormalized() {
		return nameNormalized;
	}

	public void setNameNormalized(String nameNormalized) {
		this.nameNormalized = nameNormalized;
	}

	public Employer getCurrentEmployer() {
		return currentEmployer;
	}

	public void setCurrentEmployer(Employer currentEmployer) {
		this.currentEmployer = currentEmployer;
	}

	public String getCurrentEmployerName() {
		if (getCurrentEmployer() != null)
			return getCurrentEmployer().getName();
		else
			return currentEmployerName;
	}

	public void setCurrentEmployerName(String name) {
		this.currentEmployerName = name;
	}
}
