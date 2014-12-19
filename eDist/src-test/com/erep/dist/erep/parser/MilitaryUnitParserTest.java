package com.erep.dist.erep.parser;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class MilitaryUnitParserTest {

	@Test
	public void testParseGroups() throws IOException {
		File file = new File(
				"edist/src-test/com/erep/dist/erep/parser/group-list.html");

		String html = FileUtils.readFileToString(file);

		String[] regiments = new MilitaryUnitParser().parseGroups(html);

		assertArrayEquals(new String[] { "12295", "12392", "12441", "12513",
				"12852", "12906", "13147", "13235", "13295", "14675", "14728",
				"14738", "14802", "14837", "15078", "15380", "15410", "15538",
				"15576", "15617", "15703", "15752", "15769", "15785", "15808",
				"15960", "15978", "15993", "16003", "16021", "16031", "16096",
				"16147", "16184", "16568", "17123", "17166" }, regiments);
	}

	@Test
	public void testParseMembers() throws IOException {
		File file = new File(
				"edist/src-test/com/erep/dist/erep/parser/regiment-list.html");

		String html = FileUtils.readFileToString(file);

		String[] members = new MilitaryUnitParser().parseMembers(html);

		assertTrue(members.length == 29);
		assertTrue(Arrays.asList(members).contains("666732"));
		assertTrue(Arrays.asList(members).contains("1111112"));
		assertTrue(Arrays.asList(members).contains("5313713"));
	}

}
