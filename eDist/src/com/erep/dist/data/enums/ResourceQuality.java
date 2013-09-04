package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum ResourceQuality
{
	Q1(2, 1, 20), Q2(4, 2, 40), Q3(6, 3, 60), Q4(8, 4, 80), Q5(10, 5, 100), Q6(12, 6, 120), Q7(20, 10, 200);

	public final int	healthRecovered;
	public final int	hits;
	public final int	firepower;

	private ResourceQuality(int healthRecovered, int hits, int firepower) {
		this.healthRecovered = healthRecovered;
		this.hits = hits;
		this.firepower = firepower;
	}

	public String formValue() {
		return this.name().replace("Q", "");
	}
}
