package com.erep.dist.dwr.dto;

import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.enums.ConfigGranularity;

@DataTransferObject
public class PlayerGroupQuery {

	@RemoteProperty
	private ConfigGranularity	granularity;

	@RemoteProperty
	private int					groupsCount;
	
	@RemoteProperty
	private int					playersCount;

	@RemoteProperty
	private List<PlayerGroup>	results;

	public ConfigGranularity getGranularity() {
		return granularity;
	}

	public void setGranularity(ConfigGranularity granularity) {
		this.granularity = granularity;
	}

	public List<PlayerGroup> getResults() {
		return results;
	}

	public void setResults(List<PlayerGroup> results) {
		this.results = results;
	}

	public int getGroupsCount() {
		return groupsCount;
	}

	public void setGroupsCount(int groupsCount) {
		this.groupsCount = groupsCount;
	}

	public int getPlayersCount() {
		return playersCount;
	}

	public void setPlayersCount(int playersCount) {
		this.playersCount = playersCount;
	}

}
