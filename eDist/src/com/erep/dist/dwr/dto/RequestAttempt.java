package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.Player;
import com.erep.dist.data.enums.DistType;

@DataTransferObject
public class RequestAttempt {

	@RemoteProperty
	private Player		player;

	@RemoteProperty
	private Integer		availableFF;

	@RemoteProperty
	private Boolean		rocketAllowed;

	@RemoteProperty
	private DistType	allowedType;

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

	public DistType getAllowedType() {
		return allowedType;
	}

	public void setAllowedType(DistType allowedType) {
		this.allowedType = allowedType;
	}

}
