package com.erep.dist.data;

import java.util.List;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Player {

	/**
	 * Player ID on eRepublik
	 */
	@PrimaryKey
	@Persistent
	private Key					id;

	@Persistent
	@RemoteProperty
	private String				erId;

	@Persistent
	@RemoteProperty
	private String				name;

	@Persistent
	@RemoteProperty
	private String				avatar;

	@Persistent
	@RemoteProperty
	private PlayerStatus		status;

	@Persistent
	private PlayerGroup			group;

	@Persistent
	private DistConfig			config;

	@Persistent
	@RemoteProperty
	private RocketQuality		rocketEnabled;

	@Persistent(mappedBy = "player")
	private List<DistRequest>	ffRequests;

	@Persistent(mappedBy = "player")
	private List<RocketRequest>	rocketRequests;

	public String getErId() {
		return erId;
	}

	public void setErId(String erId) {
		this.erId = erId;
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

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

}
