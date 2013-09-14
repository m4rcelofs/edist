package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.PlayerDistStatus;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.datanucleus.annotations.Unowned;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class DailyWork {

	/**
	 * Player + day
	 */
	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent
	@RemoteProperty
	private Player				player;

	@Unowned
	@Persistent
	private PlayerGroup			group;

	@Unowned
	@Persistent
	@RemoteProperty
	private Employer			employer;

	@Persistent
	@RemoteProperty
	private Integer				day;

	/**
	 * true, false or unknown
	 */
	@Persistent
	private Boolean				worked;

	@Persistent
	private PlayerDistStatus	verificationStatus;

	public DailyWork() {}

	public DailyWork(Key key) {
		this.setKey(key);
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public PlayerGroup getGroup() {
		return group;
	}

	public void setGroup(PlayerGroup group) {
		this.group = group;
	}

	public Employer getEmployer() {
		return employer;
	}

	public void setEmployer(Employer employer) {
		this.employer = employer;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Boolean getWorked() {
		return worked;
	}

	public void setWorked(Boolean worked) {
		this.worked = worked;
	}

	public PlayerDistStatus getVerificationStatus() {
		return verificationStatus;
	}

	public void setVerificationStatus(PlayerDistStatus verificationStatus) {
		this.verificationStatus = verificationStatus;
	}

}
