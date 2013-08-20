package com.erep.dist.dwr.dto;

import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.Request;

@DataTransferObject
public class ReportQuery {

	@RemoteProperty
	private String			encodedCursor;

	@RemoteProperty
	private List<Request>	results;

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<Request> getResults() {
		return results;
	}

	public void setResults(List<Request> results) {
		this.results = results;
	}

}
