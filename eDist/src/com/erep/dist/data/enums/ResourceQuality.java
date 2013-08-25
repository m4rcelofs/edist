package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum ResourceQuality
{
	Q1(2, 1), Q2(4, 2), Q3(6, 3), Q4(8, 4), Q5(10, 5), Q6(12, 6), Q7(20, 10);

	public int	healthRecovered;
	public int	hits;

	private ResourceQuality(int healthRecovered, int hits) {
		this.healthRecovered = healthRecovered;
		this.hits = hits;
	}
}
