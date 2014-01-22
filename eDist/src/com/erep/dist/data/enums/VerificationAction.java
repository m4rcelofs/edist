package com.erep.dist.data.enums;

import static com.erep.dist.data.enums.PlayerDistStatus.OK;
import static com.erep.dist.data.enums.PlayerDistStatus.TEMP_BANNED;
import static com.erep.dist.data.enums.PlayerDistStatus.WARNING;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum VerificationAction
{
	NONE(OK), WARN(WARNING), TEMP_BAN(TEMP_BANNED);
	
	public final PlayerDistStatus statusToSet;
	
	private VerificationAction(PlayerDistStatus statusToSet) { 
		this.statusToSet = statusToSet;
	}
}
