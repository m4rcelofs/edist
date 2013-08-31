package com.erep.dist.erep.dist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.parser.DonateItemsParser;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class FoodStrategyTest {

	FoodStrategy	strategy;

	@Test
	public void testQ5InvEvenFF() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 180));

		when(strategy.getInventory()).thenReturn(q5Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(180), result.getQuantity());

	}

	@Test
	public void testQ5InvOddFF() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 182));

		when(strategy.getInventory()).thenReturn(q5Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(182), result.getQuantity());

	}

	@Test
	public void testQ5Q7InvEnoughFood() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 10));

		when(strategy.getInventory()).thenReturn(q5q7Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q7, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}

	@Test
	public void testQ5Q7InvNotEnoughFoodEven() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 120));

		when(strategy.getInventory()).thenReturn(q5q7Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(120), result.getQuantity());

	}

	@Test
	public void testQ5Q7InvNotEnoughFoodOdd() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 122));

		when(strategy.getInventory()).thenReturn(q5q7Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(122), result.getQuantity());

	}

	@Test
	public void testMixedInvQ6() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 6));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q6, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}

	@Test
	public void testMixedInvQ4() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 4));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q4, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}

	@Test
	public void testMixedInvQ3() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 3));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q3, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}

	@Test
	public void testMixedInvQ2() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 2));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q2, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}

	@Test
	public void testMixedInvQ1() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 1));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q1, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}

	@Test
	public void testEmptyInvValidation() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), null, null, 1));

		when(strategy.getInventory()).thenReturn(emptyInventory());

		assertFalse(strategy.validateInventory());
	}

	@Test(expected = InventoryValidationException.class)
	public void testEmptyInvException() throws Exception {

		Player player = mock(Player.class);
		ErepScrapper scrapper = mock(ErepScrapper.class);
		DonateItemsParser parser = mock(DonateItemsParser.class);

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				parser, 1));

		when(strategy.scanInventory()).thenReturn("");
		when(strategy.getInventory()).thenReturn(emptyInventory());

		strategy.execute();
	}

	private Table<ResourceType, ResourceQuality, Integer> emptyInventory() {
		return HashBasedTable.create();
	}

	private Table<ResourceType, ResourceQuality, Integer> q5Inventory() {
		Table<ResourceType, ResourceQuality, Integer> inv = HashBasedTable
				.create();

		inv.put(ResourceType.FOOD, ResourceQuality.Q5, 1000);

		return inv;
	}

	private Table<ResourceType, ResourceQuality, Integer> q5q7Inventory() {
		Table<ResourceType, ResourceQuality, Integer> inv = HashBasedTable
				.create();

		inv.put(ResourceType.FOOD, ResourceQuality.Q5, 1000);
		inv.put(ResourceType.FOOD, ResourceQuality.Q7, 5);

		return inv;
	}

	private Table<ResourceType, ResourceQuality, Integer> mixedInventory() {
		Table<ResourceType, ResourceQuality, Integer> inv = HashBasedTable
				.create();

		inv.put(ResourceType.FOOD, ResourceQuality.Q1, 100);
		inv.put(ResourceType.FOOD, ResourceQuality.Q2, 100);
		inv.put(ResourceType.FOOD, ResourceQuality.Q3, 100);
		inv.put(ResourceType.FOOD, ResourceQuality.Q4, 100);
		inv.put(ResourceType.FOOD, ResourceQuality.Q6, 100);

		return inv;
	}

}
