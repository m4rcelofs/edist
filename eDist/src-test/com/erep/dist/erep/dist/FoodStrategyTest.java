package com.erep.dist.erep.dist;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.erep.dist.data.Player;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.ErepScrapper;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class FoodStrategyTest {

	@Mock
	Player			player;
	@Mock
	ErepScrapper	scrapper;

	FoodStrategy	strategy;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this.getClass());
	}

	@Test
	public void testQ5InvEvenFF() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				180));

		when(strategy.getInventory()).thenReturn(q5Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(18), result.getQuantity());

	}

	@Test
	public void testQ5InvOddFF() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				18));

		when(strategy.getInventory()).thenReturn(q5Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(1), result.getQuantity());

	}
	
	@Test
	public void testQ5Q7InvEnoughFood() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				100));

		when(strategy.getInventory()).thenReturn(q5q7Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q7, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}
	
	@Test
	public void testQ5Q7InvNotEnoughFoodEven() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				120));

		when(strategy.getInventory()).thenReturn(q5q7Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(12), result.getQuantity());

	}
	
	@Test
	public void testQ5Q7InvNotEnoughFoodOdd() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				122));

		when(strategy.getInventory()).thenReturn(q5q7Inventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(12), result.getQuantity());

	}
	
	@Test
	public void testMixedInvEvenFF() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				180));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q7, result.getQuality());
		assertEquals(Integer.valueOf(9), result.getQuantity());

	}
	
	@Test
	public void testMixedInvQ6() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				48));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q6, result.getQuality());
		assertEquals(Integer.valueOf(4), result.getQuantity());

	}
	
	@Test
	public void testMixedInvQ5() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				50));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q5, result.getQuality());
		assertEquals(Integer.valueOf(5), result.getQuantity());

	}
	
	@Test
	public void testMixedInvQ4() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				32));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q4, result.getQuality());
		assertEquals(Integer.valueOf(4), result.getQuantity());

	}
	
	@Test
	public void testMixedInvQ3() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				6));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q3, result.getQuality());
		assertEquals(Integer.valueOf(1), result.getQuantity());

	}
	
	@Test
	public void testMixedInvQ2() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				4));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q2, result.getQuality());
		assertEquals(Integer.valueOf(1), result.getQuantity());

	}
	
	@Test
	public void testMixedInvQ1() throws Exception {

		strategy = spy(new FoodStrategy(new ResourceDist(), player, scrapper,
				122));

		when(strategy.getInventory()).thenReturn(mixedInventory());

		strategy.validateInventory();

		ResourceDist result = strategy.getResource();
		assertEquals(ResourceQuality.Q1, result.getQuality());
		assertEquals(Integer.valueOf(61), result.getQuantity());

	}

	private Table<ResourceType, ResourceQuality, Integer> q5Inventory() {
		Table<ResourceType, ResourceQuality, Integer> inv = HashBasedTable
				.create();

		inv.put(ResourceType.FOOD, ResourceQuality.Q5, 100);

		return inv;
	}
	
	private Table<ResourceType, ResourceQuality, Integer> q5q7Inventory() {
		Table<ResourceType, ResourceQuality, Integer> inv = HashBasedTable
				.create();

		inv.put(ResourceType.FOOD, ResourceQuality.Q5, 100);
		inv.put(ResourceType.FOOD, ResourceQuality.Q7, 5);

		return inv;
	}

	private Table<ResourceType, ResourceQuality, Integer> mixedInventory() {
		Table<ResourceType, ResourceQuality, Integer> inv = HashBasedTable
				.create();

		for (ResourceQuality quality : ResourceQuality.values())
			inv.put(ResourceType.FOOD, quality, 100);

		return inv;
	}

}
