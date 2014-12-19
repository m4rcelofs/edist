package com.erep.dist.data.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.enums.Division;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Stats extends GenericDAO {

	public Stats(PersistenceManager pm) {
		super(pm);
	}

	public DailyStats find(DailyStats dailyStats) {
		return pm().getObjectById(DailyStats.class, dailyStats.getKey());
	}

	public DailyStats find(Player dbPlayer, Integer day) {
		return pm().getObjectById(DailyStats.class, createKey(dbPlayer, day));
	}

	public DailyStats findLastKnown(Player player) {
		List<DailyStats> allKnown = list(player, null);
		return allKnown.get(0);
	}

	private Key createKey(Player dbPlayer, Integer day) {
		return KeyFactory.createKey(dbPlayer.getKey(),
				DailyStats.class.getSimpleName(), day);
	}

	@SuppressWarnings("unchecked")
	public List<DailyStats> list(Player player, String encodedCursor) {
		Query q = queryWithCursor(DailyStats.class, "player == playerParam",
				encodedCursor);
		q.declareParameters(Key.class.getName() + " playerParam");
		q.setOrdering("day desc");
		return (List<DailyStats>) q.execute(player.getKey());
	}

	@SuppressWarnings("unchecked")
	public List<DailyStats> list(Integer day, PlayerGroup group,
			String encodedCursor) {
		Query q = queryWithCursor(DailyStats.class,
				"groups.contains(groupParam) && day == dayParam", encodedCursor);
		q.declareParameters(Key.class.getName() + " groupParam, "
				+ Integer.class.getName() + " dayParam");
		return (List<DailyStats>) q.execute(group.getKey(), day);
	}

	@SuppressWarnings("unchecked")
	public List<DailyStats> list(Integer day, Division div, String encodedCursor) {
		Query q = queryWithCursor(DailyStats.class,
				"div == divParam && day == dayParam", encodedCursor);
		q.declareParameters(Division.class.getName() + " divParam, "
				+ Integer.class.getName() + " dayParam");
		return (List<DailyStats>) q.execute(div, day);
	}

	@SuppressWarnings("unchecked")
	public List<DailyStats> list(Integer day, String encodedCursor) {
		Query q = queryWithCursor(DailyStats.class, "day == dayParam",
				encodedCursor);
		q.declareParameters(Integer.class.getName() + " dayParam");
		q.setOrdering("day desc");
		return (List<DailyStats>) q.execute(day);
	}

}
