package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class DistConfigDTO {

	@RemoteProperty
	private Integer	currentMaxDailyFF;

	@RemoteProperty
	private Boolean	rocketAllowed;

	@RemoteProperty
	private Boolean	autoSend;

	public Integer getCurrentMaxDailyFF() {
		return currentMaxDailyFF;
	}

	public void setCurrentMaxDailyFF(Integer currentMaxDailyFF) {
		this.currentMaxDailyFF = currentMaxDailyFF;
	}

	public Boolean getRocketAllowed() {
		return rocketAllowed;
	}

	public void setRocketAllowed(Boolean rocketAllowed) {
		this.rocketAllowed = rocketAllowed;
	}

	public Boolean getAutoSend() {
		return autoSend;
	}

	public void setAutoSend(Boolean autoSend) {
		this.autoSend = autoSend;
	}
}
