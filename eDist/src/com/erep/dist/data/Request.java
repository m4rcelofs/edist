package com.erep.dist.data;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.Inheritance;
import javax.jdo.annotations.InheritanceStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.RequestStatus;
import com.google.appengine.api.datastore.Key;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
@Inheritance(strategy = InheritanceStrategy.SUBCLASS_TABLE)
public abstract class Request {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key				id;

	@Persistent
	@RemoteProperty
	private Player			player;

	@Persistent
	@RemoteProperty
	private Integer			eRepDay;

	@Persistent
	@RemoteProperty
	private RequestStatus	status;

	@Persistent
	@RemoteProperty
	private Date			requestedAt;

	@Persistent
	@RemoteProperty
	private Date			completedAt;
}
