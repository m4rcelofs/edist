package com.erep.dist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.data.Player;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class DataAccessObject {

	private static final Integer	PAGE_LIMIT	= 20;

	private PersistenceManager		pm;

	public DataAccessObject(PersistenceManager pm) {
		this.pm = pm;
	}

	public boolean add(Player newPlayer) {
		Key playerKey = createKey(newPlayer);

		try {
			pm.getObjectById(Player.class, playerKey);
			edit(newPlayer);
			return false;
		} catch (JDOObjectNotFoundException e) {
			newPlayer.setId(playerKey);
			pm.makePersistent(newPlayer);
			return true;
		}
	}

	public boolean edit(Player newPlayer) {

		Key playerKey = createKey(newPlayer);
		Player oldPlayer = pm.getObjectById(Player.class, playerKey);

		oldPlayer.setAutoSend(newPlayer.getAutoSend());
		oldPlayer.setCurrentMaxDailyFF(newPlayer.getCurrentMaxDailyFF());
		oldPlayer.setRocketEnabled(newPlayer.getRocketEnabled());
		oldPlayer.setStatus(newPlayer.getStatus());

		return true;
	}

	public Player getPlayer(Key key) {
		return pm.getObjectById(Player.class, key);
	}

	@SuppressWarnings("unchecked")
	public List<Player> listAllPlayers(String encodedCursor) {
		Query q = pm.newQuery(Player.class);

		if (encodedCursor != null && !encodedCursor.trim().isEmpty()) {
			Cursor cursor = Cursor.fromWebSafeString(encodedCursor);
			Map<String, Object> extensionMap = new HashMap<>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			q.setExtensions(extensionMap);
		}

		q.setRange(0, PAGE_LIMIT);

		return (List<Player>) q.execute();
	}

	private Key createKey(Player player) {
		return KeyFactory.createKey(Player.class.getSimpleName(),
				player.getErId());
	}

}
