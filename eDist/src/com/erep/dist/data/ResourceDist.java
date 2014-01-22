package com.erep.dist.data;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.ResourceDistStatus;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.google.appengine.api.datastore.Key;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ResourceDist {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key					key;

	@Persistent
	@RemoteProperty
	private ResourceType		type;

	@Persistent
	@RemoteProperty
	private ResourceQuality		quality;

	@Persistent
	@RemoteProperty
	private Integer				quantity;

	@Persistent
	@RemoteProperty
	private ResourceDistStatus	status;

	public ResourceDist() {}
	
	public ResourceDist(Key key) {
		this.key = key;
	}

	public ResourceDist(ResourceType type, ResourceQuality quality,
			Integer quantity) {
		this.type = type;
		this.quality = quality;
		this.quantity = quantity;
		this.status = ResourceDistStatus.REQUESTED;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}

	public ResourceQuality getQuality() {
		return quality;
	}

	public void setQuality(ResourceQuality quality) {
		this.quality = quality;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public ResourceDistStatus getStatus() {
		return status;
	}

	public void setStatus(ResourceDistStatus status) {
		this.status = status;
	}

}
