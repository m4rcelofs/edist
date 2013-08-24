package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum ResourceQuality
{
	Q1, Q2, Q3, Q4, Q5, Q6, Q7
}
