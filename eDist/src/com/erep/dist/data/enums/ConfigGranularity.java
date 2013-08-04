package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum ConfigGranularity
{
	MU, GROUP, PLAYER
}
