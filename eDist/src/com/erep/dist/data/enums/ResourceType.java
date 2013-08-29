package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum ResourceType
{
	WEAPONS(2), FOOD(1), CURRENCY, GOLD;
	
	public int industryId;
	
	private ResourceType(){}
	
	private ResourceType(int industryId){
		this.industryId = industryId;
	}
}
