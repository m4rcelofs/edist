package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.DailyStats;

@DataTransferObject
public class StatsQuery {

	@RemoteProperty
	private String				encodedCursor;

	@RemoteProperty
	private List<DailyStats>	results = new ArrayList<>();

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<DailyStats> getResults() {
		return results;
	}

	public void setResults(List<DailyStats> results) {
		this.results = results;
	}
}
