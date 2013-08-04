package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.erep.dist.data.enums.DistType;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DistRequest extends Request {

	@Persistent
	private DistType	type;

	@Persistent
	private Integer		requestedFF;

}
