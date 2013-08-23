package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Licence {

	@PrimaryKey
	@Persistent
	private Key		id;

	@Persistent
	private String	signed;

	public Key getId() {
		return id;
	}

	public void setId(Key id) {
		this.id = id;
	}

	public String getSigned() {
		return signed;
	}

	public void setSigned(String signed) {
		this.signed = signed;
	}

}
