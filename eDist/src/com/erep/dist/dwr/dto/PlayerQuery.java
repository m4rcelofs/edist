package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.Player;
import com.erep.dist.data.enums.ConfigGranularity;

@DataTransferObject
public class PlayerQuery {

	@RemoteProperty
	private ConfigGranularity granularity;
	
	@RemoteProperty
	private String			encodedCursor;

	@RemoteProperty
	private Collection<Player>	results = new ArrayList<>();

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public Collection<Player> getResults() {
		return results;
	}

	public void setResults(Collection<Player> results) {
		this.results = results;
	}

	public ConfigGranularity getGranularity() {
		return granularity;
	}

	public void setGranularity(ConfigGranularity granularity) {
		this.granularity = granularity;
	}
}
