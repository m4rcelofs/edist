package com.erep.dist.erep;

import static org.junit.Assert.*;

import org.junit.Test;

import com.erep.dist.data.enums.ResourceQuality;

public class HitCalculatorTest {

	@Test
	public void testnW0lf() {
		HitCalculator calc = new HitCalculator();
		int hit = calc.calculateHit(40415.5, 207010631l, ResourceQuality.Q7);
		assertEquals(42244, hit);
	}

	@Test
	public void testGuilpas() {
		HitCalculator calc = new HitCalculator();
		int hit = calc.calculateHit(53703.35, 342413464l, ResourceQuality.Q7);
		assertEquals(55995, hit);
	}

	@Test
	public void testMadGhost() {
		HitCalculator calc = new HitCalculator();
		int hit = calc.calculateHit(8005d, 463325l, ResourceQuality.Q7);
		assertEquals(5169, hit);
	}
}
