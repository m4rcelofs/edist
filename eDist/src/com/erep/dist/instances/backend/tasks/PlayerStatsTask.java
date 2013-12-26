package com.erep.dist.instances.backend.tasks;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.commons.io.IOUtils;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.erep.HitCalculator;
import com.erep.dist.erep.eRepURL;
import com.erep.dist.erep.parser.CitizenParser;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class PlayerStatsTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Integer		day;
	private final Key			playerKey;

	public PlayerStatsTask(Integer day, Player player) {
		this.day = day;
		this.playerKey = player.getKey();
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			Future<HTTPResponse> futureResponse = fetchPage(playerKey.getId());

			DataAccessObject dao = new DataAccessObject(pm);
			Player dbPlayer = dao.find(new Player(playerKey));

			HTTPResponse response = futureResponse.get();
			if (response.getResponseCode() == 404) {
				dbPlayer.setDbStatus(PlayerStatus.ERROR);
				return;
			}

			DailyStats stats = new DailyStats(dbPlayer, day);

			stats = parseResponse(response, stats);

			calculateHit(stats);

			stats.setCollectedAt(new Date());

			dbPlayer.addDailyStats(stats);
			startFightCalculationTask(day, dbPlayer);

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void calculateHit(DailyStats stats) {
		stats.setMaxHit(new HitCalculator().calculateHit(stats,
				ResourceQuality.maxQuality()));
	}

	private Future<HTTPResponse> fetchPage(long playerErId)
			throws MalformedURLException {
		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();
		return fetchService.fetchAsync(new URL(eRepURL.CITIZEN
				.getUrl(playerErId + "")));
	}

	private DailyStats parseResponse(HTTPResponse response, DailyStats stats)
			throws IOException {
		String html = IOUtils.toString(response.getContent(), "UTF-8");

		Map<String, String> playerInfo = new CitizenParser().parse(html);

		stats.setAvatar(playerInfo.get("avatar").toString());
		stats.setName(playerInfo.get("name").toString());
		stats.setRank(Long.valueOf(playerInfo.get("rank").toString()));
		stats.setStrength(Double.valueOf(playerInfo.get("strength").toString()));
		stats.setXp(Long.valueOf(playerInfo.get("xp").toString()));

		return stats;
	}

	private void startFightCalculationTask(Integer day, Player dbPlayer) {
		DeferredTask task = new CalculateDailyFightsTask(day, dbPlayer);
		Queue queue = QueueFactory.getDefaultQueue();
		queue.add(withPayload(task).taskName(
				"CalculateDailyFightsTask_" + dbPlayer.getErId() + "_" + day));
	}

}
