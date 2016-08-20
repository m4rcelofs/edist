package com.erep.dist.erep.parser;

import java.util.logging.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.google.common.collect.Table;

public class DonateItemsParser {

	private static final Logger		logger	= Logger.getLogger(DonateItemsParser.class
			.getName());
	
	/**
	 * @param html
	 * @param inventory
	 *            Inventory table to be filled
	 * @return Form token
	 */
	public String parse(String html,
			Table<ResourceType, ResourceQuality, Integer> inventory) {
		logger.warning("main response");
		Document doc = Jsoup.parse(html);
		scan(doc, ResourceType.FOOD, "1", inventory);
		scan(doc, ResourceType.WEAPONS, "2", inventory);

		Element token = doc.getElementById("_token");
		return token.val();
	}

	private void scan(Document doc, ResourceType industry, String industryId,
			Table<ResourceType, ResourceQuality, Integer> inventory) {

		Elements availableResources = doc.getElementsByAttributeValue(
				"industry", industryId);

		for (Element element : availableResources) {
			ResourceQuality quality = ResourceQuality.valueOf("Q"
					+ element.attr("quality"));
			Integer quantity = Integer.valueOf(element.parent().parent()
					.child(1).text());
			inventory.put(industry, quality, quantity);
		}

	}
}
