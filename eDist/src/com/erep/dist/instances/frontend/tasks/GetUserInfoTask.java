package com.erep.dist.instances.frontend.tasks;

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
import com.erep.dist.cache.CacheFacade;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.dao.Server;
import com.erep.dist.data.enums.CountryCode;
import com.erep.dist.data.enums.Division;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.erep.HitCalculator;
import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.erep.eRepURL;
import com.erep.dist.erep.parser.CitizenParser;
import com.erep.dist.instances.backend.tasks.AvatarFetchTask;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskAlreadyExistsException;
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

			Future<HTTPResponse> futureResponse = fetchPage(playerKey.getId());

			DataAccessObject dao = new DataAccessObject(pm);
			Player dbPlayer = dao.find(new Player(playerKey));

			HTTPResponse response = futureResponse.get();
			if (response.getResponseCode() == 404) {
				dbPlayer.setDbStatus(PlayerStatus.ERROR);
				return;
			}

			DailyStats stats = new DailyStats(dbPlayer,
					eRepTimeCalculator.geteRepDay() - 1);

			stats = parseResponse(response, stats);

			calculateHit(stats);

			stats.getGroups().addAll(dbPlayer.getGroups());
			stats.setCollectedAt(new Date());

			pm.makePersistent(stats);

			dbPlayer.setName(stats.getName());
			dbPlayer.setAvatar(stats.getAvatar());

			if (stats.getIsDead() || stats.getIsPermaBan())
				dbPlayer.setDbStatus(PlayerStatus.DISABLED);
			else
				dbPlayer.setDbStatus(PlayerStatus.ENABLED);

			if (new Server(pm).getConfig().isAvatarHistory())
				startAvatarFetchTask(stats);

			CacheFacade
					.remove("com.erep.dist.DataAccessObject.findPlayerId(String="
							+ dbPlayer.getName() + ";)");
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

		stats.setAvatar(playerInfo.get("avatar"));
		stats.setName(playerInfo.get("name"));
		stats.setRank(Long.valueOf(playerInfo.get("rank")));
		stats.setStrength(Double.valueOf(playerInfo.get("strength")));
		stats.setXp(Long.valueOf(playerInfo.get("xp")));
		stats.setDiv(Division.findDivision(Long.valueOf(playerInfo.get("xp")),
				eRepTimeCalculator.geteRepDay()));
		stats.setIsDead(Boolean.valueOf(playerInfo.get("dead")));
		stats.setIsPermaBan(Boolean.valueOf(playerInfo.get("permaBanned")));
		stats.setIsTempBan(Boolean.valueOf(playerInfo.get("tempBanned")));
		stats.setBanTip(playerInfo.get("banTip"));
		stats.setMilitaryUnit(playerInfo.get("mu"));
		stats.setCitizenship(CountryCode.getCode(playerInfo.get("citizenship")));
		stats.setGuerrillaWon(Integer.valueOf(playerInfo.get("guerrillaWon")));
		stats.setGuerrillaLost(Integer.valueOf(playerInfo.get("guerrillaLost")));
		stats.setSmallBombsUsed(Integer.valueOf(playerInfo.get("smallBombs")));
		stats.setBigBombsUsed(Integer.valueOf(playerInfo.get("bigBombs")));

		return stats;
	}

	private void startAvatarFetchTask(DailyStats stats) {
		DeferredTask task = new AvatarFetchTask(stats, stats.getAvatar());
		Queue queue = QueueFactory.getDefaultQueue();
		try {
			queue.add(withPayload(task)
					.taskName(
							"AvatarFetchTask_" + stats.geterId() + "_"
									+ stats.getDay()));
		} catch (TaskAlreadyExistsException e) {
			// tombstoned, already run
		}
	}
}
