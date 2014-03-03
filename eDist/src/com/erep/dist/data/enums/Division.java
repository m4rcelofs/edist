package com.erep.dist.data.enums;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.convert.EnumConverter;

@DataTransferObject(converter = EnumConverter.class)
public enum Division
{

	D1, D2, D3, D4;

	public static Division findDivision(long xp, int eRepDay) {
		if (eRepDay < 2282)
			return findOldDivisions(xp);
		else
			return findCurrentDivisions(xp);
	}

	/*
	 * http://www.erepublik.com/en/main/latest-updates/0/109
	 */
	private static Division findOldDivisions(long xp) {
		// lv 25
		if (xp < 5000)
			return D1;
		// lv 30
		if (xp < 25000)
			return D2;
		// lv 37
		if (xp < 60000)
			return D3;
		return D4;
	}

	private static Division findCurrentDivisions(long xp) {
		// lv 35
		if (xp < 50000)
			return D1;
		// lv 50
		if (xp < 125000)
			return D2;
		// lv 70
		if (xp < 225000)
			return D3;
		return D4;
	}

}
