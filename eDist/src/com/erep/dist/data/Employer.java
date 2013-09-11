package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Employer implements HasEncodedKey {

	@PrimaryKey
	@Persistent
	private Key		key;

	@NotPersistent
	@RemoteProperty
	private String	encodedKey;

	@Persistent
	@RemoteProperty
	private String	name;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getEncodedKey() {
		if (key != null)
			return KeyFactory.keyToString(key);
		return encodedKey;
	}

	public void setEncodedKey(String id) {
		if (id != null)
			this.key = KeyFactory.stringToKey(id);
		this.encodedKey = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
