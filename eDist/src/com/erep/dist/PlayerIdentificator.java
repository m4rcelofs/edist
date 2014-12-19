package com.erep.dist;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;

import com.erep.dist.data.Player;
import com.erep.dist.data.dao.Players;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class PlayerIdentificator {

	private static Pattern	profilePattern	= Pattern
													.compile("(http://|https://)?(www\\.)?erepublik\\.com/\\D{2}/citizen/profile/(?<id>\\d+)");

	private final Players	players;

	public PlayerIdentificator(PersistenceManager pm) {
		this.players = new Players(pm);
	}

	public Player identifyRequestString(String idNameOrLink) {

		Player p = new Player();
		if (idNameOrLink == null || idNameOrLink.isEmpty())
			return null;

		idNameOrLink = idNameOrLink.trim();
		try {
			p.setKey(keyFromId(idNameOrLink));
		} catch (NumberFormatException e) {
			Matcher matcher = profilePattern.matcher(idNameOrLink);
			if (matcher.matches())
				p.setKey(keyFromId(matcher.group("id")));
			else
				// assume name
				p.setKey(players.findPlayerKey(idNameOrLink));
		} catch (IllegalArgumentException e) {
			throw new JDOObjectNotFoundException(e.getMessage(), e);
		}
		return players.find(p);
	}

	private Key keyFromId(String id) {
		Integer keyValue = Integer.parseInt(id);
		return KeyFactory.createKey(Players.rootKey(),
				Player.class.getSimpleName(), keyValue);
	}
}
