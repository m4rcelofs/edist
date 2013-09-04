package com.erep.dist.erep;

import com.erep.dist.data.DailyStats;
import com.erep.dist.data.enums.ResourceQuality;

public class HitCalculator {

	public Integer calculateHit(DailyStats stats, ResourceQuality quality) {
		return calculateHit(stats.getStrength(), stats.getRank(), quality);
	}

	public Integer calculateHit(Double strength, Long rankValue,
			ResourceQuality quality) {
		return calculateHit(strength,
				new RankCalculator().rankMultiplier(rankValue),
				quality.firepower);
	}

	/**
	 * http://wiki.erepublik.com/index.php/Military_formulas
	 * 
	 * @return I = 10 × (1 +S/400) × (1 +R/5) × (1 +FirePower/100) <br />
	 *         $dano = (10 + $r * $forca/200 + 2 * $r + $forca/40) * (1 +
	 *         weapon_damage/100);
	 */
	public Integer calculateHit(Double strength, Integer rankMultiplier,
			Integer firepower) {
//		 return (int) (10 * (1 + strength / 400) * (1 + rankMultiplier / 5) *
//		 (1 + firepower / 100));
		double hit = (10 + rankMultiplier * strength / 200 + 2 * rankMultiplier + strength / 40)
				* (1 + firepower / 100);
		return (int) hit;
	}
}
