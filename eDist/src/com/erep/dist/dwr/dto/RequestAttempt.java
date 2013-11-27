package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "RequestAttempt"))
public class RequestAttempt {

	@RemoteProperty
	private PlayerGroup		group;

	@RemoteProperty
	private Integer			availableFF;

	@RemoteProperty
	private Integer			rechargeTime;

	@RemoteProperty
	private Boolean			rocketAllowed;

	@RemoteProperty
	private RequestType		allowedType;

	@RemoteProperty
	private ResourceQuality	allowedQuality;

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

	public PlayerGroup getGroup() {
		return group;
	}

	public void setGroup(PlayerGroup group) {
		this.group = group;
	}

}
