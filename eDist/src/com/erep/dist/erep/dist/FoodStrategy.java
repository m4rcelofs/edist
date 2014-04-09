package com.erep.dist.erep.dist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.parser.DonateItemsParser;

public class FoodStrategy extends ManufacturedStrategy {

	private final Integer	requestedFF;

	public FoodStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper, Integer requestedFF) throws Exception {
		super(resource, player, scrapper);
		this.requestedFF = requestedFF;
	}

	protected FoodStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper, DonateItemsParser parser, Integer requestedFF)
			throws Exception {
		super(resource, player, scrapper, parser);
		this.requestedFF = requestedFF;
	}

	@Override
	protected boolean validateInventory() throws InventoryValidationException {

		if (hasCaptcha)
			throw new InventoryValidationException(
					"Distributor has captcha enabled, aborting");

		logger.log(Level.WARNING, "Current inventory: "
				+ getInventory().toString());
		
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
				resource.setQuantity(toHealth(requestedFF)
						/ quality.healthRecovered);
				if (isFFMatch(quality))
					break;
			}

		if (resource.getQuality() == null)
			throw new InventoryValidationException(
					"Could not find enough food to supply " + requestedFF
							+ " FF");

		return true;
	}

	private boolean isFFMatch(ResourceQuality quality) {
		return toHealth(requestedFF) % quality.healthRecovered == 0;
	}

	private boolean hasEnough(Map<ResourceQuality, Integer> foodInventory,
			ResourceQuality quality) {
		return foodInventory.get(quality) >= toHealth(requestedFF)
				/ quality.healthRecovered;
	}

	private int toHealth(int ff) {
		return ff * 10;
	}

}
