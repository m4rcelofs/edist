package com.erep.dist.data;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.VerificationAction;

/**
 * Check last X days for the use of at least Y% of the requested supplies
 */
@EmbeddedOnly
@DataTransferObject
@PersistenceCapable
public class FightVerification {

	@Persistent
	@RemoteProperty
	private int					fightVerifDays;

	@Persistent
	@RemoteProperty
	private double				fightVerifPct;

	@Persistent
	@RemoteProperty
	private VerificationAction	fightVerifAction;

	public FightVerification() {}

	public FightVerification(VerificationAction action) {
		this.fightVerifAction = action;
	}

	public int getFightVerifDays() {
		return fightVerifDays;
	}

	public void setFightVerifDays(int fightVerifDays) {
		this.fightVerifDays = fightVerifDays;
	}

	public double getFightVerifPct() {
		return fightVerifPct;
	}

	public void setFightVerifPct(double fightVerifPct) {
		this.fightVerifPct = fightVerifPct;
	}

	public VerificationAction getFightVerifAction() {
		return fightVerifAction;
	}

	public void setFightVerifAction(VerificationAction fightVerifAction) {
		this.fightVerifAction = fightVerifAction;
	}

}
