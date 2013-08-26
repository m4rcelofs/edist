package com.erep.dist.erep.parser;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class CitizenParser {

	public Map<String, String> parse(String html) {

		Map<String, String> citizen = new HashMap<>();

		Document doc = Jsoup.parse(html);

		String name = doc.select("div .citizen_profile_header > h2").text()
				.trim();
		citizen.put("name", name);

		Elements avatarDOM = doc.select(".citizen_avatar");
		String avatar = avatarDOM.first().attr("style")
				.replace("background-image: url(", "").replace(");", "");

		citizen.put("avatar", avatar);

		Elements info = doc.select(".place");
		String militaryUnit = info.get(1).select("span").text();

		citizen.put("mu", militaryUnit);

		return citizen;

	}
}
