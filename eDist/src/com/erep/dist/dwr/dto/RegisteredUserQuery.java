package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.RegisteredUser;

@DataTransferObject
public class RegisteredUserQuery {

	@RemoteProperty
	private String					encodedCursor;

	@RemoteProperty
	private List<RegisteredUser>	results = new ArrayList<>();

	public String getEncodedCursor() {
		return encodedCursor;
	}

	public void setEncodedCursor(String encodedCursor) {
		this.encodedCursor = encodedCursor;
	}

	public List<RegisteredUser> getResults() {
		return results;
	}

	public void setResults(List<RegisteredUser> results) {
		this.results = results;
	}

}
