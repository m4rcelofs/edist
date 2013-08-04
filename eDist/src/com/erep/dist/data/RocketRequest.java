package com.erep.dist.data;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;

import com.erep.dist.data.enums.RocketQuality;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class RocketRequest extends Request {

	@Persistent
	private RocketQuality	quality;

	public RocketQuality getQuality() {
		return quality;
	}

	public void setQuality(RocketQuality quality) {
		this.quality = quality;
	}

}
