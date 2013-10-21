package com.erep.dist.data;

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
import com.erep.dist.data.enums.VerificationAction;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "DistConfig"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroup(name = "configListing", members = { @Persistent(name = "distributor"), })
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

	// Fight Verification
	@Persistent
	@RemoteProperty
	private int					fightVerifDays;

	@Persistent
	@RemoteProperty
	private double				fightVerifPct;

	@Persistent
	@RemoteProperty
	private VerificationAction	fightVerifAction;

	// Work Verification
	@Persistent
	@RemoteProperty
	private int					workVerifDays;

	@Persistent
	@RemoteProperty
	private double				minWorkedDaysPct;

	@Persistent
	@RemoteProperty
	private VerificationAction	workVerifAction;

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

	public int getFightVerifDays() {
		return fightVerifDays;
	}

	public void setFightVerifDays(int fightVerifDays) {
		this.fightVerifDays = fightVerifDays;
	}

	public double getFightVerifPct() {
		return fightVerifPct;
	}

	public void setFightVerifPct(double fightVerifPct) {
		this.fightVerifPct = fightVerifPct;
	}

	public VerificationAction getFightVerifAction() {
		return fightVerifAction;
	}

	public void setFightVerifAction(VerificationAction fightVerifAction) {
		this.fightVerifAction = fightVerifAction;
	}

	public int getWorkVerifDays() {
		return workVerifDays;
	}

	public void setWorkVerifDays(int workVerifDays) {
		this.workVerifDays = workVerifDays;
	}

	public double getMinWorkedDaysPct() {
		return minWorkedDaysPct;
	}

	public void setMinWorkedDaysPct(double minWorkedDaysPct) {
		this.minWorkedDaysPct = minWorkedDaysPct;
	}

	public VerificationAction getWorkVerifAction() {
		return workVerifAction;
	}

	public void setWorkVerifAction(VerificationAction workVerifAction) {
		this.workVerifAction = workVerifAction;
	}

}
