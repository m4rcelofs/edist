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
import com.google.common.collect.Table;
import com.google.common.collect.Tables;

public abstract class ManufacturedStrategy implements DistStrategy {

	private static final Logger									logger	= Logger.getLogger(ManufacturedStrategy.class
																				.getName());

	private final ResourceDist									resource;
	private final Player										player;
	private final ErepScrapper									scrapper;

	private final Table<ResourceType, ResourceQuality, Integer>	inventory;
	protected String											formToken;

	protected ManufacturedStrategy(ResourceDist resource, Player player,
			ErepScrapper scrapper) throws Exception {
		this.resource = resource;
		this.player = player;
		this.scrapper = scrapper;

		this.inventory = HashBasedTable.create();

	}

	@Override
	public void execute() throws Exception {
		scanInventory();
		if (!validateInventory())
			throw new InventoryValidationException();

		sendResource();
	}

	protected void scanInventory() throws IOException {
		String donateHtml = scrapper
				.scrape(eRepURL.DONATE_ITEMS.getUrl(player));
		formToken = new DonateItemsParser().parse(donateHtml, inventory);
	}

	protected abstract boolean validateInventory()
			throws InventoryValidationException;

	private void sendResource() {
		// TODO Auto-generated method stub

	}

	public Table<ResourceType, ResourceQuality, Integer> getInventory() {
		return Tables.unmodifiableTable(inventory);
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
