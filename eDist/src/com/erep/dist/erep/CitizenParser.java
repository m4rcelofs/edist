package com.erep.dist.erep;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
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

	public static void main(String[] args) {
		try {
			URL url = new URL(
					"http://www.erepublik.com/br/citizen/profile/2808593");
			String html = IOUtils.toString(url.openConnection()
					.getInputStream(), "UTF-8");

			Map<String, String> response = new CitizenParser().parse(html);
			System.out.println(response.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
