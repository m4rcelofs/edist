package com.erep.dist.dwr;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class PlayerInfo {

	@RemoteProperty
	private Boolean			registered,rocket;
	
	@RemoteProperty
	private String			name,image;

	@RemoteProperty
	private int	  			availableFF,rechargeTime;

	PlayerInfo(Boolean reg){
		this.setRegistered(reg);
	}
	

	PlayerInfo(Boolean reg,Boolean rock,String nam,String imag,int ff,int recharge){
		this.setRegistered(reg);
		this.rocket = rock;
		this.name = nam;
		this.image = imag;
		this.availableFF = ff;
		this.rechargeTime = recharge;
	}


	private Boolean getRegistered() {
		return registered;
	}


	private void setRegistered(Boolean registered) {
		this.registered = registered;
	}

}
