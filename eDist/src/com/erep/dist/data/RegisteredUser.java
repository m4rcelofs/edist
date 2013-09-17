package com.erep.dist.data;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RegisteredUser {

	public enum Role
	{
		NONE, VIEWER, MODERATOR
	}

	@PrimaryKey
	@Persistent
	@RemoteProperty
	private String			gId;

	@Persistent
	@RemoteProperty
	private final String	gEmail;

	@Persistent
	@RemoteProperty
	private String			name;

	@Persistent
	@RemoteProperty
	private final Date		registeredAt;

	@Persistent
	@RemoteProperty
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
