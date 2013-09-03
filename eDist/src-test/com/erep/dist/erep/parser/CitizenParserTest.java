package com.erep.dist.erep.parser;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.erep.dist.erep.parser.CitizenParser;

public class CitizenParserTest {

	@Test
	public void testParse() throws IOException {
		URL url = new URL("http://www.erepublik.com/br/citizen/profile/2808593");
		String html = IOUtils.toString(url.openConnection().getInputStream(),
				"UTF-8");

		Map<String, String> response = new CitizenParser().parse(html);
		System.out.println(response.toString());

		assertNotNull(response);
		assertNotNull(response.get("name"));
		assertNotNull(response.get("avatar"));
		assertNotNull(response.get("mu"));
		assertNotNull(response.get("xp"));
		assertNotNull(response.get("rank"));
		assertNotNull(response.get("strength"));
		
		assertNotNull(Long.valueOf(response.get("xp")));
		assertNotNull(Long.valueOf(response.get("rank")));
		assertNotNull(Double.valueOf(response.get("strength")));
	}

}
