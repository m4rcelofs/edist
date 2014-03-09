package com.erep.dist.data;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

/**
 * Vanilla entity to force strong consistency on Employers <br />
 * All Employers should have this entity as ancestor
 * 
 * @author Marcelo
 */
@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class EmployerRoot {

	@PrimaryKey
	@Persistent
	private Key					key;

	@Persistent(mappedBy="root")
	@Extension(vendorName = "datanucleus", key = "gae.unindexed", value = "true")
	public List<Employer>	employers;

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public List<Employer> getEmployers() {
		return employers;
	}

	public void setEmployers(List<Employer> employers) {
		this.employers = employers;
	}

	public void addEmployers(Employer employer) {
		if (getEmployers() == null)
			setEmployers(new ArrayList<Employer>());
		getEmployers().add(employer);
	}

}
