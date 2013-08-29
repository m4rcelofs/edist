package com.erep.dist.erep.dist;

import java.util.Map;
import java.util.Map.Entry;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.ErepScrapper;

public class FoodStrategy extends ManufacturedStrategy {

	private final Integer	requestedFF;

	public FoodStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper, Integer requestedFF) throws Exception {
		super(resource, player, scrapper);
		this.requestedFF = requestedFF;
	}

	@Override
	protected boolean validateInventory() throws InventoryValidationException {


		ResourceDist resource = getResource();

		Map<ResourceQuality, Integer> foodInventory = getInventory().row(
				ResourceType.FOOD);
		
		for (Entry<ResourceQuality, Integer> availableFood : foodInventory
				.entrySet()) {
			
			if(isFFMatch(availableFood.getKey()))
				; 		// TODO Auto-generated method stub
				
		}

		return false;
	}

	private boolean isFFMatch(ResourceQuality quality) {
		return requestedFF % quality.healthRecovered == 0;
	}

}
