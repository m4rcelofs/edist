package com.erep.dist.erep.dist;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.erep.ErepScrapper;

public class FoodStrategy extends ManufacturedStrategy {

	private final Integer	requestedFF;

	public FoodStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper, Integer requestedFF) throws Exception {
		super(resource, player, scrapper);
		this.requestedFF = requestedFF;
	}

}
