package com.erep.dist;

import static com.erep.dist.dwr.dto.TransferHelper.fromDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.data.DistRequest;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.RocketRequest;
import com.erep.dist.dwr.dto.PlayerDTO;
import com.erep.dist.dwr.dto.PlayerGroupDTO;
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

	public boolean add(PlayerDTO newPlayer) {
		Key playerKey = createKey(newPlayer);

		try {
			pm.getObjectById(Player.class, playerKey);
			edit(newPlayer);
			return false;
		} catch (JDOObjectNotFoundException e) {

			Player player = new Player();
			player.setId(playerKey);
			player.setErId(newPlayer.getErId());
			player.setRocketEnabled(newPlayer.getRocketEnabled());
			player.setStatus(newPlayer.getStatus());
			player.setFfRequests(new ArrayList<DistRequest>());
			player.setRocketRequests(new ArrayList<RocketRequest>());

			player.setConfig(fromDTO(newPlayer.getConfig()));

			PlayerGroup group = findGroup(newPlayer.getGroup());
			player.setGroup(group);
			group.addPlayer(player);

			pm.makePersistent(player);

			return true;
		}
	}

	public boolean edit(PlayerDTO newPlayer) {

		Key playerKey = createKey(newPlayer);
		Player oldPlayer = pm.getObjectById(Player.class, playerKey);

		oldPlayer.getConfig().setAutoSend(newPlayer.getConfig().getAutoSend());
		oldPlayer.getConfig().setCurrentMaxDailyFF(
				newPlayer.getConfig().getCurrentMaxDailyFF());

		oldPlayer.setRocketEnabled(newPlayer.getRocketEnabled());
		oldPlayer.setStatus(newPlayer.getStatus());

		PlayerGroup oldGroup = oldPlayer.getGroup();
		PlayerGroup newGroup = findGroup(newPlayer.getGroup());

		oldPlayer.setGroup(newGroup);
		newGroup.addPlayer(oldPlayer);
		oldGroup.removePlayer(oldPlayer);

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

	public PlayerGroup findGroup(PlayerGroupDTO dto) {
		try {
			if (dto == null || dto.getId() == null
					|| dto.getId().trim().isEmpty())
				return defaultGroup();

			return pm.getObjectById(PlayerGroup.class, createKey(dto));
		} catch (JDOObjectNotFoundException e) {
			return defaultGroup();
		}
	}

	private PlayerGroup defaultGroup() {
		return pm.getObjectById(PlayerGroup.class, KeyFactory.createKey(
				PlayerGroup.class.getSimpleName(), ServerConstants.DEFAULT));
	}

	private Key createKey(PlayerGroupDTO group) {
		return KeyFactory.createKey(PlayerGroup.class.getSimpleName(),
				group.getId());
	}

	private Key createKey(PlayerDTO player) {
		return KeyFactory.createKey(Player.class.getSimpleName(),
				player.getErId());
	}

}
