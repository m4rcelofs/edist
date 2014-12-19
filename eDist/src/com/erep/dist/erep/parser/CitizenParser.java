package com.erep.dist.erep.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class CitizenParser {

	public Map<String, String> parse(String html) {

		Map<String, String> citizen = new HashMap<>();

		Document doc = Jsoup.parse(html);

		parseName(doc, citizen);
		parseAvatar(doc, citizen);
		parseXP(doc, citizen);
		parseMU(doc, citizen);
		parseStrRank(doc, citizen);
		parseCitizenship(doc, citizen);
		parseDeadBanned(doc, citizen);
		parseGuerrilla(doc, citizen);
		parseBombs(doc, citizen);

		return citizen;

	}

	private void parseBombs(Document doc, Map<String, String> citizen) {
		Elements elements = doc.select("div .citizen_mass_destruction > strong > b");
		String sb = "0";
		String bb = "0";
		if(elements.size() == 2) {
			sb = elements.get(0).text();
			bb = elements.get(1).text();
		}
		
		citizen.put("smallBombs", sb);
		citizen.put("bigBombs", bb);
	}

	private void parseGuerrilla(Document doc, Map<String, String> citizen) {
		Elements elements = doc.select("div .guerilla_fights_history");
		citizen.put("guerrillaWon", elements.select(".won").text());
		citizen.put("guerrillaLost", elements.select(".lost").text());
	}

	private void parseDeadBanned(Document doc, Map<String, String> citizen) {

		Elements elements = doc.select("div .citizen_state > .is");

		boolean isTempBan = false;
		boolean isPermaBan = false;
		boolean isDead = false;
		String banReason = null;

		for (Element e : elements) {
			if (!isDead)
				isDead = e.text().equalsIgnoreCase("Dead citizen");
			if (!isPermaBan)
				isPermaBan = e.text().equalsIgnoreCase("Permanent banned");
			if (!isTempBan)
				isTempBan = e.text().equalsIgnoreCase("Temporarily banned");

			if (isPermaBan || isTempBan)
				banReason = e.attr("title");
		}

		citizen.put("dead", String.valueOf(isDead));
		citizen.put("tempBanned", String.valueOf(isTempBan));
		citizen.put("permaBanned", String.valueOf(isPermaBan));
		citizen.put("banTip", banReason);

	}

	private void parseCitizenship(Document doc, Map<String, String> citizen) {
		List<TextNode> elements = doc.select("div .citizen_info > a").get(2)
				.textNodes();
		for (TextNode node : elements)
			if (node.text() != null && !node.text().trim().isEmpty())
				citizen.put("citizenship", node.text().trim());
	}

	private void parseName(Document doc, Map<String, String> citizen) {
		List<TextNode> elements = doc
				.select("div .citizen_profile_header > h2").get(0).textNodes();
		for (TextNode node : elements)
			if (node.text() != null && !node.text().trim().isEmpty())
				citizen.put("name", node.text().trim());
	}

	private void parseAvatar(Document doc, Map<String, String> citizen) {
		Elements avatarDOM = doc.select(".citizen_avatar");
		String avatar = avatarDOM.first().attr("style")
				.replace("background-image: url(", "").replace(");", "");

		citizen.put("avatar", avatar);
	}

	private void parseXP(Document doc, Map<String, String> citizen) {
		Elements expDOM = doc.select(".citizen_level");
		
		String xp = expDOM.attr("title");
		xp = xp.substring(xp.lastIndexOf('>')+1, xp.lastIndexOf('/')).replace(",", "");
	
		citizen.put("xp", xp.trim());
	}

	private void parseMU(Document doc, Map<String, String> citizen) {
		Elements infoDOM = doc.select(".place");
		String militaryUnit = infoDOM.get(1).select("span").text();

		citizen.put("mu", militaryUnit);
	}

	private void parseStrRank(Document doc, Map<String, String> citizen) {
		
		Elements strEl = doc.select(".mb_bottom");
		String str = strEl.get(0).text().replace(",", "");
		citizen.put("strength", str.trim());

		String rank = doc.select(".rank_numbers").text();
		rank = rank.substring(0, rank.indexOf('/')).replace(",", "");
		citizen.put("rank", rank.trim());
	}

}
