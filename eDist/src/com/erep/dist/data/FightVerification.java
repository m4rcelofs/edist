package com.erep.dist.data;

import javax.jdo.annotations.EmbeddedOnly;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.erep.dist.data.enums.VerificationAction;

/**
 * Check last X days for the use of at least Y% of the requested supplies
 */
@EmbeddedOnly
@PersistenceCapable
public class FightVerification {

	@Persistent
	private int					fightVerifDays;

	@Persistent
	private double				fightVerifPct;

	@Persistent
	private VerificationAction	fightVerifAction;

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
