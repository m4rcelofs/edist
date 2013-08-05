package com.erep.dist.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Distributor implements HasTransitID {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key		key;

	@NotPersistent
	@RemoteProperty
	private String	id;

	@Persistent
	@RemoteProperty
	private String	email;

	@Persistent(defaultFetchGroup = "false")
	@RemoteProperty
	private String	password;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getId() {
		if (key != null)
			return key.getName();
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
