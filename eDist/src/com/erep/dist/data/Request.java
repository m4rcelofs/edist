package com.erep.dist.data;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erep.dist.data.enums.DistType;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Request {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key				key;

	@Persistent
	private Player			player;

	@Persistent
	private Integer			eRepDay;

	@Persistent
	private RequestStatus	status;

	@Persistent
	private DistType		type;

	// if DistType == rocket only
	@Persistent
	private RocketQuality	quality;

	// if DistType != rocket
	@Persistent
	private Integer			requestedFF;

	@Persistent
	private Date			requestedAt;

	@Persistent
	private Date			completedAt;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer geteRepDay() {
		return eRepDay;
	}

	public void seteRepDay(Integer eRepDay) {
		this.eRepDay = eRepDay;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public Date getRequestedAt() {
		return requestedAt;
	}

	public void setRequestedAt(Date requestedAt) {
		this.requestedAt = requestedAt;
	}

	public Date getCompletedAt() {
		return completedAt;
	}

	public void setCompletedAt(Date completedAt) {
		this.completedAt = completedAt;
	}

	public DistType getType() {
		return type;
	}

	public void setType(DistType type) {
		this.type = type;
	}

	public RocketQuality getQuality() {
		return quality;
	}

	public void setQuality(RocketQuality quality) {
		this.quality = quality;
	}

	public Integer getRequestedFF() {
		return requestedFF;
	}

	public void setRequestedFF(Integer requestedFF) {
		this.requestedFF = requestedFF;
	}
}
