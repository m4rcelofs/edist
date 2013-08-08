package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DistRequest extends Request {

	@Persistent
	private Integer	requestedFF;

	public Integer getRequestedFF() {
		return requestedFF;
	}

	public void setRequestedFF(Integer requestedFF) {
		this.requestedFF = requestedFF;
	}

}
