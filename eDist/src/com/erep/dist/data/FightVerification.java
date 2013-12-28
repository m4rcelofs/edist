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

@DataTransferObject(converter = BeanConverter.class, params = @Param(name = "javascript", value = "FightVerification"))
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class FightVerification implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key					key;

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

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
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
