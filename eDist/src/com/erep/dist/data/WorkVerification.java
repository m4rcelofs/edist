package com.erep.dist.data;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.VerificationAction;

/**
 * Must work at least X % from the last Y days
 */
@EmbeddedOnly
@DataTransferObject
@PersistenceCapable
public class WorkVerification {

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
