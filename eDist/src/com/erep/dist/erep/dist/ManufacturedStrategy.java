package com.erep.dist.erep.dist;

import java.io.IOException;
import java.util.logging.Logger;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.eRepURL;
import com.erep.dist.erep.parser.DonateItemsParser;
import com.google.common.collect.HashBasedTable;

public abstract class ManufacturedStrategy implements DistStrategy {

	private final ResourceDist												resource;
	private final Player													player;
	private final ErepScrapper												scrapper;
	private final DonateItemsParser											parser;

	private final HashBasedTable<ResourceType, ResourceQuality, Integer>	inventory;
	protected String														formToken;

	protected static Logger													logger	= Logger.getLogger(ManufacturedStrategy.class
																							.getName());

	protected ManufacturedStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper) throws Exception {
		this(resource, player, scrapper, new DonateItemsParser());
	}

	protected ManufacturedStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper, DonateItemsParser parser) {
		this.resource = resource;
		this.player = player;
		this.scrapper = scrapper;
		this.inventory = HashBasedTable.create();
		this.parser = parser;
	}

	@Override
	public void execute() throws Exception {
		scanInventory();
		if (!validateInventory())
			throw new InventoryValidationException();
		sendResource();
	}

	protected String scanInventory() throws IOException {
		String donateHtml = scrapper
				.scrape(eRepURL.DONATE_ITEMS.getUrl(player));

		formToken = parser.parse(donateHtml, inventory);
		return formToken;
	}

	protected abstract boolean validateInventory()
			throws InventoryValidationException;

	private boolean sendResource() throws IOException,
			ResourcesSendingException {
		return scrapper.sendItem(player, resource, formToken);
	}

	public HashBasedTable<ResourceType, ResourceQuality, Integer> getInventory() {
		return inventory;
	}

	protected ResourceDist getResource() {
		return resource;
	}

	protected ErepScrapper getScrapper() {
		return scrapper;
	}

	protected Player getPlayer() {
		return player;
	}

}
