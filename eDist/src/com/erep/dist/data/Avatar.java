package com.erep.dist.data;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Avatar {

	@PrimaryKey
	@Persistent
	/**
	 * MD5
	 */
	private Key		key;

	@Persistent
	private BlobKey	blob;

	@Persistent
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	private String	servingUrl;

	public Avatar(String digest) {
		setKey(KeyFactory.createKey(Avatar.class.getSimpleName(), digest));
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public BlobKey getBlob() {
		return blob;
	}

	public void setBlob(BlobKey blob) {
		this.blob = blob;
	}

	public String getServingUrl() {
		return servingUrl;
	}

	public void setServingUrl(String servingUrl) {
		this.servingUrl = servingUrl;
	}

}
