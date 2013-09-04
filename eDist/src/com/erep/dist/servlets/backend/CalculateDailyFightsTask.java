package com.erep.dist.servlets.backend;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.erep.HitCalculator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class CalculateDailyFightsTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String dayParam = req.getParameter("day");
		String playerKeyParam = req.getParameter("playerKey");

		validateParams(dayParam, playerKeyParam);

		Integer today = Integer.valueOf(dayParam);
		Player p = new Player(KeyFactory.stringToKey(playerKeyParam));

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			ConfigHelper config = new ConfigHelper(dao);

			DailyStats previousDayStats = dao.getStats(p, today - 1);
			DailyStats todayStats = dao.getStats(p, today);
			Player dbPlayer = dao.find(p);

			ResourceQuality quality = config.getAllowedWeaponQuality(dbPlayer);

			DailyFights fights = calculateFights(today, dbPlayer,
					previousDayStats, todayStats, quality);

			dbPlayer.addDailyFights(fights);

		} catch (JDOObjectNotFoundException e) {
			// first scrapping day, ignore
		}

	}

	private DailyFights calculateFights(Integer today, Player dbPlayer,
			DailyStats previousDayStats, DailyStats todayStats,
			ResourceQuality quality) {

		DailyFights fights = new DailyFights(createKey(dbPlayer, today));

		fights.setDay(today);
		fights.setCalculatedWithWeaponQuality(quality);

		int calculatedFights = calculateFights(previousDayStats, todayStats,
				quality);

		fights.setCalculatedFfFights(calculatedFights);

		return fights;
	}

	private int calculateFights(DailyStats previousDayStats,
			DailyStats todayStats, ResourceQuality quality) {

		int hit = todayStats.getMaxHit();
		if (quality != ResourceQuality.maxQuality())
			hit = new HitCalculator().calculateHit(todayStats, quality);

		long rankChange = todayStats.getRank() - previousDayStats.getRank();
		long dailyInfluence = rankChange * 10;

		return (int) (dailyInfluence / hit);
	}

	private Key createKey(Player dbPlayer, Integer today) {
		return KeyFactory.createKey(dbPlayer.getKey(),
				DailyFights.class.getSimpleName(), today);
	}

	private boolean validateParams(String... params)
			throws IllegalArgumentException {
		for (String param : params)
			if (param == null || param.trim().isEmpty())
				throw new IllegalArgumentException("Paramater is null or empty");
		return true;
	}

}
