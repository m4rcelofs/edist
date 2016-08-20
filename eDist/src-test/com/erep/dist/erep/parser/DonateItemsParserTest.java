package com.erep.dist.erep.parser;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class DonateItemsParserTest {

	@Test
	public void testParse() throws IOException {
		File file = new File(
				"edist/src-test/com/erep/dist/erep/parser/donateItems.html");
	
		String html = FileUtils.readFileToString(file);

		Table<ResourceType, ResourceQuality, Integer> inventory = HashBasedTable
				.create();
		
		String formToken = new DonateItemsParser().parse(html, inventory);
		
//		assertEquals(Integer.valueOf(175), inventory.get(ResourceType.FOOD, ResourceQuality.Q1));
//		assertEquals(Integer.valueOf(93), inventory.get(ResourceType.FOOD, ResourceQuality.Q2));
		assertEquals(Integer.valueOf(3933), inventory.get(ResourceType.FOOD, ResourceQuality.Q3));
//		assertEquals(Integer.valueOf(87), inventory.get(ResourceType.FOOD, ResourceQuality.Q4));
//		assertEquals(Integer.valueOf(194), inventory.get(ResourceType.FOOD, ResourceQuality.Q5));
//		assertEquals(Integer.valueOf(1), inventory.get(ResourceType.FOOD, ResourceQuality.Q6));
//		assertEquals(Integer.valueOf(693), inventory.get(ResourceType.FOOD, ResourceQuality.Q7));
		
//		assertEquals(Integer.valueOf(90), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q1));
//		assertEquals(Integer.valueOf(91), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q2));
//		assertEquals(Integer.valueOf(90), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q3));
//		assertEquals(Integer.valueOf(90), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q4));
//		assertEquals(Integer.valueOf(90), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q5));
//		assertEquals(Integer.valueOf(90), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q6));
		assertEquals(Integer.valueOf(276), inventory.get(ResourceType.WEAPONS, ResourceQuality.Q7));
		
		assertEquals("5ce14946aa73e06e0d7a288fa09b2494", formToken);
		
	}

}
