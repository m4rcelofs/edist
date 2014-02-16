package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
public class WorkWeekQuery {

	// player query cursor
	@RemoteProperty
	private String			encodedCursor;

	@RemoteProperty
	private List<WorkWeek>	results	= new ArrayList<>();

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<WorkWeek> getResults() {
		return results;
	}

	public void setResults(List<WorkWeek> results) {
		this.results = results;
	}
}
