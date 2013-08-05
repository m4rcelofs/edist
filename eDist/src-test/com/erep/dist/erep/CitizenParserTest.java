package com.erep.dist.erep;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

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
	}

}
