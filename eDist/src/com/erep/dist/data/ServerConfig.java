package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.ConfigGranularity;
import com.erep.dist.data.enums.ReportsAccessLevel;
import com.google.appengine.api.datastore.Key;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerConfig {

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent
	@RemoteProperty
	private String				muName;

	@Persistent
	@RemoteProperty
	private DistConfig			muConfig;

	@Persistent
	@RemoteProperty
	private ConfigGranularity	granularity;

	@Persistent
	@RemoteProperty
	private ReportsAccessLevel	reportsAccess;

	@Persistent
	@RemoteProperty
	private ReportsAccessLevel	playerReportsAccess;

	@Persistent
	@RemoteProperty
	private String				contactEmail;

	public String getMuName() {
		return muName;
	}

	public void setMuName(String muName) {
		this.muName = muName;
	}

	public DistConfig getMuConfig() {
		return muConfig;
	}

	public void setMuConfig(DistConfig muConfig) {
		this.muConfig = muConfig;
	}

	public ConfigGranularity getGranularity() {
		return granularity;
	}

	public void setGranularity(ConfigGranularity granularity) {
		this.granularity = granularity;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public ReportsAccessLevel getReportsAccess() {
		return reportsAccess;
	}

	public void setReportsAccess(ReportsAccessLevel reportsAccess) {
		this.reportsAccess = reportsAccess;
	}

	public String getContactEmail() {
		return contactEmail;
	}

	public void setContactEmail(String contactEmail) {
		this.contactEmail = contactEmail;
	}

	public ReportsAccessLevel getPlayerReportsAccess() {
		return playerReportsAccess;
	}

	public void setPlayerReportsAccess(ReportsAccessLevel playerReportsAccess) {
		this.playerReportsAccess = playerReportsAccess;
	}

}
