package com.erep.dist.data;

import java.util.Date;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.enums.RegisteredUserRole;
import com.google.appengine.api.users.User;

@DataTransferObject
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RegisteredUser {

	@PrimaryKey
	@Persistent
	@RemoteProperty
	private String				gId;

	@Persistent
	@RemoteProperty
	private String				gEmail;

	@Persistent
	@RemoteProperty
	private String				name;

	@Persistent
	@RemoteProperty
	private Date				registeredAt;

	@Persistent
	@RemoteProperty
	private RegisteredUserRole	role;

	public RegisteredUser() {}

	public RegisteredUser(User user) {
		this.gId = user.getUserId();
		this.gEmail = user.getEmail();
		this.name = user.getNickname();
		this.role = RegisteredUserRole.NONE;
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

	public RegisteredUserRole getRole() {
		return role;
	}

	public void setRole(RegisteredUserRole role) {
		this.role = role;
	}

	public void setgId(String gId) {
		this.gId = gId;
	}

	public void setgEmail(String gEmail) {
		this.gEmail = gEmail;
	}

	public void setRegisteredAt(Date registeredAt) {
		this.registeredAt = registeredAt;
	}

}
