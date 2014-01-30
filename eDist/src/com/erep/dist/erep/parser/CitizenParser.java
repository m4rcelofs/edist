package com.erep.dist.erep.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class CitizenParser {

	public Map<String, String> parse(String html) {

		Map<String, String> citizen = new HashMap<>();

		Document doc = Jsoup.parse(html);

		{
			List<TextNode> elements = doc.select("div .citizen_profile_header > h2").get(0).textNodes();
			for(TextNode node : elements) 
				if(node.text() != null && !node.text().trim().isEmpty())
					citizen.put("name", node.text().trim());
		}

		{
			Elements avatarDOM = doc.select(".citizen_avatar");
			String avatar = avatarDOM.first().attr("style")
					.replace("background-image: url(", "").replace(");", "");

			citizen.put("avatar", avatar);
		}

		{
			Elements expDOM = doc.select(".citizen_experience");

			String xp = expDOM.get(0).getElementsByTag("p").text();
			xp = xp.substring(0, xp.indexOf(" ")).replace(",", "");
			citizen.put("xp", xp);
		}

		{
			Elements infoDOM = doc.select(".place");
			String militaryUnit = infoDOM.get(1).select("span").text();

			citizen.put("mu", militaryUnit);
		}

		{
			Elements militaryDOM = doc.select(".citizen_military");

			String str = militaryDOM.get(0).select("h4").text()
					.replace(",", "");
			citizen.put("strength", str);

			String rank = militaryDOM.get(1).getElementsByTag("strong").get(1)
					.text();
			rank = rank.substring(0, rank.indexOf(" ")).replace(",", "");
			citizen.put("rank", rank);
		}

		return citizen;

	}
}
