package com.erep.dist.data;

import java.io.Serializable;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.Param;
import org.directwebremoting.annotations.RemoteProperty;
import org.directwebremoting.convert.BeanConverter;

import com.erep.dist.data.enums.VerificationAction;
import com.google.appengine.api.datastore.Key;

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "WorkVerification"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class WorkVerification implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key					key;

	@Persistent
	@RemoteProperty
	private int					workVerifDays;

	@Persistent
	@RemoteProperty
	private double				minWorkedDaysPct;

	@Persistent
	@RemoteProperty
	private VerificationAction	workVerifAction;

	public WorkVerification() {}

	public WorkVerification(VerificationAction action) {
		this.workVerifAction = action;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public int getWorkVerifDays() {
		return workVerifDays;
	}

	public void setWorkVerifDays(int workVerifDays) {
		this.workVerifDays = workVerifDays;
	}

	public double getMinWorkedDaysPct() {
		return minWorkedDaysPct;
	}

	public void setMinWorkedDaysPct(double minWorkedDaysPct) {
		this.minWorkedDaysPct = minWorkedDaysPct;
	}

	public VerificationAction getWorkVerifAction() {
		return workVerifAction;
	}

	public void setWorkVerifAction(VerificationAction workVerifAction) {
		this.workVerifAction = workVerifAction;
	}

}
