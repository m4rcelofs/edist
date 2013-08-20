package com.erep.dist.data;

import java.util.Date;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.DistType;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroup(name = "reportListing", members = {
		@Persistent(name = "distributor"), @Persistent(name = "player"),
		@Persistent(name = "group") })
public class Request {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key				key;

	@Persistent
	@RemoteProperty
	private Integer			eRepDay;

	@Persistent
	@RemoteProperty
	private RequestStatus	status;

	@Persistent
	@RemoteProperty
	private DistType		type;

	// if DistType == rocket only
	@Persistent
	@RemoteProperty
	private RocketQuality	quality;

	// if DistType != rocket
	@Persistent
	@RemoteProperty
	private Integer			requestedFF;

	@Persistent
	@RemoteProperty
	private Date			requestedAt;

	@Persistent
	@RemoteProperty
	private Date			completedAt;

	@Unowned
	@Persistent
	@RemoteProperty
	private Distributor		distributor;

	@Unowned
	@Persistent
	@RemoteProperty
	private Player			player;

	@Unowned
	@Persistent
	@RemoteProperty
	private PlayerGroup		group;

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

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

	public PlayerGroup getGroup() {
		return group;
	}

	public void setGroup(PlayerGroup group) {
		this.group = group;
	}
}
