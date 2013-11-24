package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum PlayerDistStatus
{
	OK(0), WARNING(5), TEMP_BANNED(10);

	public int	priority;

	private PlayerDistStatus(int priority) {
		this.priority = priority;
	}
}
