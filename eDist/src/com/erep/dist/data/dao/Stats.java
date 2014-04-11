package com.erep.dist.data.dao;

import javax.jdo.PersistenceManager;

import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Stats extends GenericDAO {

	public Stats(PersistenceManager pm) {
		super(pm);
	}

	public DailyStats find(Player dbPlayer, Integer day) {
		return pm().getObjectById(DailyStats.class, createKey(dbPlayer, day));
	}

	private Key createKey(Player dbPlayer, Integer day) {
		return KeyFactory.createKey(dbPlayer.getKey(),
				DailyStats.class.getSimpleName(), day);
	}

}
