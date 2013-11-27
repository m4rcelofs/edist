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
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "DistConfig"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@FetchGroup(name = "configListing", members = { @Persistent(name = "distributor"), })
public class DistConfig {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key				key;

	@Persistent
	@RemoteProperty
	private int				currentMaxDailyFF;

	@Persistent
	@RemoteProperty
	private boolean			rocketAllowed;

	@Persistent
	@RemoteProperty
	private boolean			autoSend;

	/**
	 * weapon, food, or both
	 */
	@Persistent
	@RemoteProperty
	private RequestType		allowedType;

	@Persistent
	@RemoteProperty
	private ResourceQuality	allowedWeaponQuality;

	@Unowned
	@Persistent
	@RemoteProperty
	private Distributor		distributor;

	public DistConfig() {}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getCurrentMaxDailyFF() {
		return currentMaxDailyFF;
	}

	public void setCurrentMaxDailyFF(Integer currentMaxDailyFF) {
		if (currentMaxDailyFF != null)
			this.currentMaxDailyFF = currentMaxDailyFF;
		else
			this.currentMaxDailyFF = 0;
	}

	public boolean getRocketAllowed() {
		return rocketAllowed;
	}

	public void setRocketAllowed(Boolean rocketAllowed) {
		if (rocketAllowed != null)
			this.rocketAllowed = rocketAllowed;
		else
			this.rocketAllowed = false;
	}

	public boolean getAutoSend() {
		return autoSend;
	}

	public void setAutoSend(Boolean autoSend) {
		if (autoSend != null)
			this.autoSend = autoSend;
		else
			this.autoSend = false;
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
}
