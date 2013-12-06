package com.erep.dist.erep.dist;

import java.util.logging.Level;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.ErepScrapper;

public class WeaponStrategy extends ManufacturedStrategy {
	
	

	public WeaponStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper) throws Exception {
		super(resource, player, scrapper);
	}

	@Override
	protected boolean validateInventory() throws InventoryValidationException {

		ResourceQuality quality = getResource().getQuality();
		Integer needed = getResource().getQuantity();
		Integer actual = getInventory().get(ResourceType.WEAPONS, quality);

		logger.log(Level.WARNING, "Needed weapons: " + needed);
		logger.log(Level.WARNING, "Actual weapons: " + actual);
		logger.log(Level.WARNING, "Current inventory: " + getInventory().toString());
		
		if (actual < needed)
			throw new InventoryValidationException("Only " + actual + " w"
					+ quality.name() + " in inventory, needs at least "
					+ needed);

		return true;
	}

}
