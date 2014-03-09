package com.erep.dist.erep.parser;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class CitizenParserTest {

	@Test
	public void testParse() throws IOException {
		URL url = new URL("http://www.erepublik.com/en/citizen/profile/1432558");
		String html = IOUtils.toString(url.openConnection().getInputStream(),
				"UTF-8");

		Map<String, String> response = new CitizenParser().parse(html);
		System.out.println(response.toString());

		assertNotNull(response);
		assertNotNull(response.get("name"));
		assertNotNull(response.get("avatar"));
		assertTrue(response.get("avatar").contains("142x142"));
		assertNotNull(response.get("mu"));
		assertNotNull(response.get("xp"));
		assertNotNull(response.get("rank"));
		assertNotNull(response.get("strength"));
		
		assertNotNull(Long.valueOf(response.get("xp")));
		assertNotNull(Long.valueOf(response.get("rank")));
		assertNotNull(Double.valueOf(response.get("strength")));
		
		assertNotNull(Long.valueOf(response.get("guerrillaWon")));
		assertNotNull(Long.valueOf(response.get("guerrillaLost")));
		
		assertNotNull(Long.valueOf(response.get("smallBombs")));
		assertNotNull(Long.valueOf(response.get("bigBombs")));
	}
	
	@Test
	public void testDeadBanned() throws IOException {
		URL url = new URL("http://www.erepublik.com/en/citizen/profile/3905264");
		String html = IOUtils.toString(url.openConnection().getInputStream(),
				"UTF-8");

		Map<String, String> response = new CitizenParser().parse(html);
		System.out.println(response.toString());
		
		assertFalse(Boolean.valueOf(response.get("tempBanned")));
		assertTrue(Boolean.valueOf(response.get("permaBanned")));
		assertTrue(Boolean.valueOf(response.get("dead")));
		assertNotNull(response.get("banTip"));
		assertEquals("Brazil", response.get("citizenship"));
		
		assertNotNull(Long.valueOf(response.get("guerrillaWon")));
		assertNotNull(Long.valueOf(response.get("guerrillaLost")));
		
		assertEquals("0", response.get("smallBombs"));
		assertEquals("0", response.get("bigBombs"));
	}

}
