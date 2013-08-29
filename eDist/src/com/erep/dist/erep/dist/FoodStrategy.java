package com.erep.dist.erep.dist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

		List<ResourceQuality> availableQualities = new ArrayList<>(
				foodInventory.keySet());
		Collections.sort(availableQualities);
		Collections.reverse(availableQualities);

		for (ResourceQuality quality : availableQualities)
			if (hasEnough(foodInventory, quality)) {
				resource.setQuality(quality);
				resource.setQuantity(requestedFF / quality.healthRecovered);
				if (isFFMatch(quality))
					break;
			}

		return resource.getQuality() != null;
	}

	private boolean isFFMatch(ResourceQuality quality) {
		return requestedFF % quality.healthRecovered == 0;
	}

	private boolean hasEnough(Map<ResourceQuality, Integer> foodInventory,
			ResourceQuality quality) {
		return foodInventory.get(quality) >= requestedFF
				/ quality.healthRecovered;
	}

}
