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

		return citizen;

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
		Elements expDOM = doc.select(".citizen_experience");

		String xp = expDOM.get(0).getElementsByTag("p").text();
		xp = xp.substring(0, xp.indexOf(" ")).replace(",", "");
		citizen.put("xp", xp);
	}

	private void parseMU(Document doc, Map<String, String> citizen) {
		Elements infoDOM = doc.select(".place");
		String militaryUnit = infoDOM.get(1).select("span").text();

		citizen.put("mu", militaryUnit);
	}

	private void parseStrRank(Document doc, Map<String, String> citizen) {
		Elements militaryDOM = doc.select(".citizen_military");

		String str = militaryDOM.get(0).select("h4").text().replace(",", "");
		citizen.put("strength", str);

		String rank = militaryDOM.get(1).getElementsByTag("strong").get(1)
				.text();
		rank = rank.substring(0, rank.indexOf(" ")).replace(",", "");
		citizen.put("rank", rank);
	}

}
