package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum RocketQuality
{
	NONE(0), Q1(10), Q2(20), Q3(60), Q4(120), Q5(90);

	public final int	weaponsNeeded;

	private RocketQuality(int weaponsNeeded) {
		this.weaponsNeeded = weaponsNeeded;
	}
}
