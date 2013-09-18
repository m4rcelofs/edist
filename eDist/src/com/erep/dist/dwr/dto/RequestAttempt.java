package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.Player;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;

@DataTransferObject
public class RequestAttempt {

	@RemoteProperty
	private Player		player;

	@RemoteProperty
	private Integer		availableFF;

	@RemoteProperty
	private Integer		rechargeTime;

	@RemoteProperty
	private Boolean		rocketAllowed;

	@RemoteProperty
	private RequestType	allowedType;
	
	@RemoteProperty
	private ResourceQuality allowedQuality;

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Integer getAvailableFF() {
		return availableFF;
	}

	public void setAvailableFF(Integer availableFF) {
		this.availableFF = availableFF;
	}

	public Boolean getRocketAllowed() {
		return rocketAllowed;
	}

	public void setRocketAllowed(Boolean rocketAllowed) {
		this.rocketAllowed = rocketAllowed;
	}

	public RequestType getAllowedType() {
		return allowedType;
	}

	public void setAllowedType(RequestType allowedType) {
		this.allowedType = allowedType;
	}

	public Integer getRechargeTime() {
		return rechargeTime;
	}

	public void setRechargeTime(Integer rechargeTime) {
		this.rechargeTime = rechargeTime;
	}

	public ResourceQuality getAllowedQuality() {
		return allowedQuality;
	}

	public void setAllowedQuality(ResourceQuality allowedQuality) {
		this.allowedQuality = allowedQuality;
	}

}
