package com.erep.dist.instances.frontend.tasks;

import java.net.URL;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.cache.CacheFacade;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.erep.eRepURL;
import com.erep.dist.erep.parser.CitizenParser;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class GetUserInfoTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Key			playerKey;

	public GetUserInfoTask(Player player) {
		this.playerKey = player.getKey();
	}

	@Override
	public void run() {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);

			Player dbPlayer = dao.find(new Player(playerKey));

			URLFetchService fetchService = URLFetchServiceFactory
					.getURLFetchService();
			HTTPResponse response = fetchService.fetch(new URL(eRepURL.CITIZEN
					.getUrl(dbPlayer)));

			if (response.getResponseCode() == 404) {
				dbPlayer.setDbStatus(PlayerStatus.ERROR);
				return;
			}

			String html = IOUtils.toString(response.getContent(), "UTF-8");

			Map<String, String> playerInfo = new CitizenParser().parse(html);

			if (playerInfo == null || !playerInfo.containsKey("name")
					|| !playerInfo.containsKey("avatar"))
				throw new IllegalStateException("Info not found! ):");

			DailyStats stats = new DailyStats(dbPlayer,
					eRepTimeCalculator.geteRepDay() - 1);
			stats.setAvatar(playerInfo.get("avatar").toString());
			stats.setName(playerInfo.get("name").toString());
			stats.setRank(Long.valueOf(playerInfo.get("rank").toString()));
			stats.setStrength(Double.valueOf(playerInfo.get("strength")
					.toString()));
			stats.setXp(Long.valueOf(playerInfo.get("xp").toString()));
			stats.setCollectedAt(new Date());
			stats.setGroups(dbPlayer.getGroups());

			dbPlayer.addDailyStats(stats);

			dbPlayer.setName(playerInfo.get("name"));
			dbPlayer.setAvatar(playerInfo.get("avatar"));
			dbPlayer.setDbStatus(PlayerStatus.ENABLED);

			CacheFacade
					.remove("com.erep.dist.DataAccessObject.findPlayerId(String="
							+ dbPlayer.getName() + ";)");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
