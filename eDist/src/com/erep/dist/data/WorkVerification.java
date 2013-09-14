package com.erep.dist.data;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.erep.dist.data.enums.VerificationAction;

/**
 * Must work at least X days from the last Y days
 */
@EmbeddedOnly
@PersistenceCapable
public class WorkVerification {

	@Persistent
	private int					workVerifDays;

	@Persistent
	private double				minWorkedDays;

	@Persistent
	private VerificationAction	workVerifAction;

	public int getWorkVerifDays() {
		return workVerifDays;
	}

	public void setWorkVerifDays(int workVerifDays) {
		this.workVerifDays = workVerifDays;
	}

	public double getMinWorkedDays() {
		return minWorkedDays;
	}

	public void setMinWorkedDays(double minWorkedDays) {
		this.minWorkedDays = minWorkedDays;
	}

	public VerificationAction getWorkVerifAction() {
		return workVerifAction;
	}

	public void setWorkVerifAction(VerificationAction workVerifAction) {
		this.workVerifAction = workVerifAction;
	}
}
