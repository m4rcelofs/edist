package com.erep.dist.data;

import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class AvailableStatistics {

	// 1
	@PrimaryKey
	@Persistent
	private Key			key;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private Set<String>	requestMonthly;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Set<String> getRequestMonthly() {
		return requestMonthly;
	}

	public void setRequestMonthly(Set<String> requestMonthly) {
		this.requestMonthly = requestMonthly;
	}

	public void addRequestMonthly(RequestStatsMonthly monthly) {
		if (getRequestMonthly() == null)
			setRequestMonthly(new HashSet<String>());
		if (monthly.getYearMonth() == null || monthly.getYearMonth().isEmpty())
			throw new IllegalArgumentException(
					"RequestStatsMonthly yearMonth cannot be null or empty");
		getRequestMonthly().add(monthly.getYearMonth());
	}

}
