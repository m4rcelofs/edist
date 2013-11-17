package com.erep.dist.data;

import javax.jdo.annotations.FetchGroup;
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
@FetchGroup(name = "configListing", members = { @Persistent(name = "distConfig") })
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerConfig {

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent
	@RemoteProperty
	private String				muName;

	@Persistent(dependent = "true")
	@RemoteProperty
	private DistConfig			distConfig;

	@Persistent(dependent = "true")
	@RemoteProperty
	private FightVerification	fightVerif;

	@Persistent(dependent = "true")
	@RemoteProperty
	private WorkVerification	workVerif;

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
		return distConfig;
	}

	public void setMuConfig(DistConfig muConfig) {
		this.distConfig = muConfig;
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

	public DistConfig getDistConfig() {
		return distConfig;
	}

	public void setDistConfig(DistConfig distConfig) {
		this.distConfig = distConfig;
	}

	public FightVerification getFightVerif() {
		return fightVerif;
	}

	public void setFightVerif(FightVerification fightVerif) {
		this.fightVerif = fightVerif;
	}

	public WorkVerification getWorkVerif() {
		return workVerif;
	}

	public void setWorkVerif(WorkVerification workVerif) {
		this.workVerif = workVerif;
	}

}
