package com.erep.dist.erep.parser;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class MilitaryUnitParser {

	/**
	 * @param html
	 *            html of main MU members page
	 *            (http://www.erepublik.com/br/main/group-list/members/muId)
	 * @return Array of regiment IDs for this MU
	 */
	public String[] parseGroups(String html) {

		Document doc = Jsoup.parse(html);

		Element select = doc.select("select#regiments_lists").iterator().next();

		List<String> regiments = new ArrayList<>();
		for (Node option : select.childNodes()) {
			if (option.hasAttr("value"))
				regiments.add(option.attr("value"));
		}

		return regiments.toArray(new String[regiments.size()]);
	}

	/**
	 * @param html
	 *            html of regiment page
	 *            (http://www.erepublik.com/br/main/group-list
	 *            /members/muId/regimentId)
	 * @return Array of member IDs for this regiment
	 */
	public String[] parseMembers(String html) {

		Document doc = Jsoup.parse(html);

		Elements members = doc.select("tr.members");

		List<String> memberIds = new ArrayList<>();
		for (Element tr : members) {
			if (tr.hasAttr("memberid"))
				memberIds.add(tr.attr("memberid"));
		}

		return memberIds.toArray(new String[memberIds.size()]);
	}
}
