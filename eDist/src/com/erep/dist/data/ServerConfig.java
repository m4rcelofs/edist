package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.erep.dist.data.enums.ConfigGranularity;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ServerConfig {

	@PrimaryKey
	@Persistent
	private Key					id;

	@Persistent
	private String				muName;

	@Persistent
	private DistConfig			muConfig;

	@Persistent
	private ConfigGranularity	granularity;

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

}
