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
	private double				minWorkedDaysPct;

	@Persistent
	private VerificationAction	workVerifAction;

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
