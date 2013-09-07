package com.erep.dist.dwr.dto;

import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.DailyFights;

@DataTransferObject
public class FightsQuery {

	@RemoteProperty
	private String				encodedCursor;

	@RemoteProperty
	private List<DailyFights>	results;

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<DailyFights> getResults() {
		return results;
	}

	public void setResults(List<DailyFights> results) {
		this.results = results;
	}
}
