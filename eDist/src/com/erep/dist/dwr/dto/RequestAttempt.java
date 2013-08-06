package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.Player;

@DataTransferObject
public class RequestAttempt {

	@RemoteProperty
	private Player	player;

	@RemoteProperty
	private Integer	availableFF;

	@RemoteProperty
	private Boolean	rocketEnabled;

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

	public Boolean getRocketEnabled() {
		return rocketEnabled;
	}

	public void setRocketEnabled(Boolean rocketEnabled) {
		this.rocketEnabled = rocketEnabled;
	}

}
