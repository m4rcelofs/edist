package com.erep.dist.data.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.data.DailyFights;
import com.erep.dist.data.Player;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class Fights extends GenericDAO {

	public Fights(PersistenceManager pm) {
		super(pm);
	}

	@SuppressWarnings("unchecked")
	public List<DailyFights> getAllDaily(Integer day) {
		Query q = pm().newQuery(DailyFights.class);
		q.setFilter("day == dayParam");
		q.declareParameters(Integer.class.getName() + " dayParam");
		return (List<DailyFights>) q.execute(day);
	}

	public DailyFights find(Player dbPlayer, Integer day) {
		return pm().getObjectById(DailyFights.class, createKey(dbPlayer, day));
	}

	private Key createKey(Player dbPlayer, Integer day) {
		return KeyFactory.createKey(dbPlayer.getKey(),
				DailyFights.class.getSimpleName(), day);
	}

}
