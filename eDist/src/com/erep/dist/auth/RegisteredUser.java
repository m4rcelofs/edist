package com.erep.dist.auth;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RegisteredUser {

	enum Role
	{
		NONE, MODERATOR
	}

	@PrimaryKey
	@Persistent
	private String			gId;

	@Persistent
	private final String	gEmail;

	@Persistent
	private String			name;

	@Persistent
	private final Date		registeredAt;

	@Persistent
	private Role			role;

	public RegisteredUser(String gId, String gEmail) {
		this.gId = gId;
		this.gEmail = getgEmail();
		this.role = Role.NONE;
		this.registeredAt = new Date();
	}

	public String getgId() {
		return gId;
	}

	public String getgEmail() {
		return gEmail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getRegisteredAt() {
		return registeredAt;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
