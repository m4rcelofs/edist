package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.RocketQuality;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RocketRequest extends Request {

	@Persistent
	@RemoteProperty
	private RocketQuality	quality;

}
