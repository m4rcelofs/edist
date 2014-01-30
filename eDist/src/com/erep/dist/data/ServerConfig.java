package com.erep.dist.data;

import java.io.Serializable;

import javax.jdo.annotations.FetchGroup;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.enums.ConfigGranularity;
import com.erep.dist.data.enums.ReportsAccessLevel;
import com.google.appengine.api.datastore.Key;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "ServerConfig"))
@FetchGroup(name = "configListing", members = { @Persistent(name = "config"),
		@Persistent(name = "fightVerif"), @Persistent(name = "workVerif") })
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerConfig implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent
	@RemoteProperty
	private String				muName;

	@Persistent(dependent = "true")
	@RemoteProperty
	private DistConfig			config;

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

	@Persistent
	private Boolean				avatarHistory;

	@Persistent
	private Boolean				showDistributorInventory;

	public String getMuName() {
		return muName;
	}

	public void setMuName(String muName) {
		this.muName = muName;
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

	public DistConfig getConfig() {
		return config;
	}

	public void setConfig(DistConfig distConfig) {
		this.config = distConfig;
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

	public boolean isAvatarHistory() {
		if (avatarHistory == null)
			setAvatarHistory(true);
		return avatarHistory;
	}

	public void setAvatarHistory(boolean avatarHistory) {
		this.avatarHistory = avatarHistory;
	}

	public boolean isShowDistributorInventory() {
		if (showDistributorInventory == null)
			setShowDistributorInventory(true);
		return showDistributorInventory;
	}

	public void setShowDistributorInventory(boolean showDistributorInventory) {
		this.showDistributorInventory = showDistributorInventory;
	}

}
