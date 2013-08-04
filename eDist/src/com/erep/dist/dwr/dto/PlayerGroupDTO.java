package com.erep.dist.dwr.dto;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class PlayerGroupDTO {

	@RemoteProperty
	private String			id;

	@RemoteProperty
	private String			groupName;

	@RemoteProperty
	private DistConfigDTO	config;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public DistConfigDTO getConfig() {
		return config;
	}

	public void setConfig(DistConfigDTO config) {
		this.config = config;
	}

}
