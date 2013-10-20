package com.erep.dist.data;

import javax.jdo.annotations.Embedded;
import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "DistConfig"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroup(name = "configListing", members = {
		@Persistent(name = "distributor"),
		@Persistent(name = "fightVerification"),
		@Persistent(name = "workVerification") })
public class DistConfig {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key					key;

	@Persistent
	@RemoteProperty
	private Integer				currentMaxDailyFF;

	@Persistent
	@RemoteProperty
	private Boolean				rocketAllowed;

	@Persistent
	@RemoteProperty
	private Boolean				autoSend;

	/**
	 * weapon, food, or both
	 */
	@Persistent
	@RemoteProperty
	private RequestType			allowedType;

	@Persistent
	@RemoteProperty
	private ResourceQuality		allowedWeaponQuality;

	@Unowned
	@Persistent
	@RemoteProperty
	private Distributor			distributor;

	@Embedded
	@Persistent
	@RemoteProperty
	private FightVerification	fightVerification;

	@Embedded
	@Persistent
	@RemoteProperty
	private WorkVerification	workVerification;

	public DistConfig() {}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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

	public RequestType getAllowedType() {
		return allowedType;
	}

	public void setAllowedType(RequestType allowedType) {
		this.allowedType = allowedType;
	}

	public ResourceQuality getAllowedWeaponQuality() {
		return allowedWeaponQuality;
	}

	public void setAllowedWeaponQuality(ResourceQuality allowedWeaponQuality) {
		this.allowedWeaponQuality = allowedWeaponQuality;
	}

	public FightVerification getFightVerification() {
		return fightVerification;
	}

	public void setFightVerification(FightVerification fightVerification) {
		this.fightVerification = fightVerification;
	}

	public WorkVerification getWorkVerification() {
		return workVerification;
	}

	public void setWorkVerification(WorkVerification workVerification) {
		this.workVerification = workVerification;
	}

}
