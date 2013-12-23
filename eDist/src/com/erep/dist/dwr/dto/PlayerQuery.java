package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

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
	private List<Player>	results = new ArrayList<>();

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<Player> getResults() {
		return results;
	}

	public void setResults(List<Player> results) {
		this.results = results;
	}

	public ConfigGranularity getGranularity() {
		return granularity;
	}

	public void setGranularity(ConfigGranularity granularity) {
		this.granularity = granularity;
	}
}
