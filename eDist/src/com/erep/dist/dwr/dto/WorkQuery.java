package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.DailyWork;

@DataTransferObject
public class WorkQuery {

	@RemoteProperty
	private String			encodedCursor;

	@RemoteProperty
	private List<DailyWork>	results = new ArrayList<>();

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<DailyWork> getResults() {
		return results;
	}

	public void setResults(List<DailyWork> results) {
		this.results = results;
	}
}
