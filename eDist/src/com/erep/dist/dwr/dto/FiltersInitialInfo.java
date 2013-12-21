package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.Distributor;
import com.erep.dist.data.Employer;
import com.erep.dist.data.PlayerGroup;

@DataTransferObject
public class FiltersInitialInfo {

	@RemoteProperty
	private List<Distributor>	distributors	= new ArrayList<>();

	@RemoteProperty
	private List<PlayerGroup>	groups			= new ArrayList<>();

	@RemoteProperty
	private List<Employer>		employers		= new ArrayList<>();

	public List<Distributor> getDistributors() {
		return distributors;
	}

	public void setDistributors(List<Distributor> distributors) {
		this.distributors = distributors;
	}

	public List<PlayerGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<PlayerGroup> groups) {
		this.groups = groups;
	}

	public List<Employer> getEmployers() {
		return employers;
	}

	public void setEmployers(List<Employer> employers) {
		this.employers = employers;
	}

}
