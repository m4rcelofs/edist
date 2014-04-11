package com.erep.dist.dwr.dto;

import java.util.Collection;

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
	private Collection<PlayerGroup>	results;

	public ConfigGranularity getGranularity() {
		return granularity;
	}

	public void setGranularity(ConfigGranularity granularity) {
		this.granularity = granularity;
	}

	public Collection<PlayerGroup> getResults() {
		return results;
	}

	public void setResults(Collection<PlayerGroup> results) {
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
