package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;

@DataTransferObject
public class PlayerDTO {

	@RemoteProperty
	private String			erId;

	@RemoteProperty
	private String			name;

	@RemoteProperty
	private String			avatar;

	@RemoteProperty
	private PlayerStatus	status;

	@RemoteProperty
	private PlayerGroupDTO	group;

	@RemoteProperty
	private RocketQuality	rocketEnabled;

	@RemoteProperty
	private DistConfigDTO	config;

	public String getErId() {
		return erId;
	}

	public void setErId(String erId) {
		this.erId = erId;
	}

	public PlayerStatus getStatus() {
		return status;
	}

	public void setStatus(PlayerStatus status) {
		this.status = status;
	}

	public PlayerGroupDTO getGroup() {
		return group;
	}

	public void setGroup(PlayerGroupDTO group) {
		this.group = group;
	}

	public RocketQuality getRocketEnabled() {
		return rocketEnabled;
	}

	public void setRocketEnabled(RocketQuality rocketEnabled) {
		this.rocketEnabled = rocketEnabled;
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

	public DistConfigDTO getConfig() {
		return config;
	}

	public void setConfig(DistConfigDTO config) {
		this.config = config;
	}

}
