package com.erep.dist.data;

import java.util.List;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroup(name = "playerListing", members = { @Persistent(name = "group"),
		@Persistent(name = "config") })
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
	private List<DistRequest>	ffRequests;

	@Persistent(mappedBy = "player")
	private List<RocketRequest>	rocketRequests;

	public Player() {}

	public String getId() {
		if (key != null)
			return key.getName();
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

	public List<DistRequest> getFfRequests() {
		return ffRequests;
	}

	public void setFfRequests(List<DistRequest> ffRequests) {
		this.ffRequests = ffRequests;
	}

	public List<RocketRequest> getRocketRequests() {
		return rocketRequests;
	}

	public void setRocketRequests(List<RocketRequest> rocketRequests) {
		this.rocketRequests = rocketRequests;
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

}
