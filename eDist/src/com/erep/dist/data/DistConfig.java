package com.erep.dist.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DistConfig {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key			id;

	@Persistent
	@RemoteProperty
	private Integer		currentMaxDailyFF;

	@Persistent
	@RemoteProperty
	private Boolean		rocketAllowed;

	@Persistent
	@RemoteProperty
	private Boolean		autoSend;

	@Unowned
	@Persistent
	private Distributor	distributor;

	public DistConfig() {}

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public Integer getCurrentMaxDailyFF() {
		return currentMaxDailyFF;
	}

	public void setCurrentMaxDailyFF(Integer currentMaxDailyFF) {
		this.currentMaxDailyFF = currentMaxDailyFF;
	}

	public Boolean getRocketAllowed() {
		return rocketAllowed;
	}

	public void setRocketAllowed(Boolean rocketAllowed) {
		this.rocketAllowed = rocketAllowed;
	}

	public Boolean getAutoSend() {
		return autoSend;
	}

	public void setAutoSend(Boolean autoSend) {
		this.autoSend = autoSend;
	}

	public Distributor getDistributor() {
		return distributor;
	}

	public void setDistributor(Distributor distributor) {
		this.distributor = distributor;
	}

}
