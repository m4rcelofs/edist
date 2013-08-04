package com.erep.dist.dwr.dto;

import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class PlayerQuery {

	@RemoteProperty
	private String			encodedCursor;

	@RemoteProperty
	private List<PlayerDTO>	results;

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<PlayerDTO> getResults() {
		return results;
	}

	public void setResults(List<PlayerDTO> results) {
		this.results = results;
	}
}
