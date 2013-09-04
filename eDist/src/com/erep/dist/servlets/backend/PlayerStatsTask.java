package com.erep.dist.servlets.backend;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.google.appengine.api.backends.BackendServiceFactory;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class PlayerStatsTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String dayParam = req.getParameter("day");
		String playerErId = req.getParameter("playerErId");
		String playerKey = req.getParameter("playerKey");

		validateParams(dayParam, playerErId, playerKey);

		Future<HTTPResponse> futureResponse = fetchPage(playerErId.trim());

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);
			Player dbPlayer = findDbPlayer(dao, playerKey.trim());

			HTTPResponse response = futureResponse.get();
			if (response.getResponseCode() == 404) {
				dbPlayer.setStatus(PlayerStatus.ERROR);
				return;
			}

			Integer day = Integer.parseInt(dayParam.trim());
			DailyStats stats = parseResponse(response);
			calculateHit(stats);

			stats.setDay(day);
			stats.setKey(KeyFactory.createKey(dbPlayer.getKey(),
					DailyStats.class.getSimpleName(), day));

			dbPlayer.addDailyStats(stats);
			startFightCalculationTask(day, dbPlayer);

		} catch (InterruptedException | ExecutionException e) {
			throw new IOException(e);
		}

	}

	private void calculateHit(DailyStats stats) {
		stats.setMaxHit(new HitCalculator().calculateHit(stats,
				ResourceQuality.maxQuality()));
	}

	private boolean validateParams(String... params)
			throws IllegalArgumentException {
		for (String param : params)
			if (param == null || param.trim().isEmpty())
				throw new IllegalArgumentException("Paramater is null or empty");
		return true;
	}

	private Future<HTTPResponse> fetchPage(String playerErId)
			throws MalformedURLException {
		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();
		return fetchService.fetchAsync(new URL(eRepURL.CITIZEN
				.getUrl(playerErId)));
	}

	private Player findDbPlayer(DataAccessObject dao, String playerKey) {
		Player p = new Player();
		p.setId(playerKey);
		return dao.find(p);
	}

	private DailyStats parseResponse(HTTPResponse response) throws IOException {
		String html = IOUtils.toString(response.getContent(), "UTF-8");

		Map<String, String> playerInfo = new CitizenParser().parse(html);

		DailyStats stats = new DailyStats();
		stats.setRank(Long.valueOf(playerInfo.get("rank").toString()));
		stats.setStrength(Double.valueOf(playerInfo.get("strength").toString()));
		stats.setXp(Long.valueOf(playerInfo.get("xp").toString()));

		return stats;
	}

	private void startFightCalculationTask(Integer day, Player dbPlayer) {
		Queue queue = QueueFactory.getQueue("erepublik-not-logged-in");

		TaskOptions taskOptions = TaskOptions.Builder
				.withUrl("/admin/tasks/dailyFights")
				.header("Host",
						BackendServiceFactory.getBackendService()
								.getBackendAddress("edist-default-backend"))
				.param("day", day.toString())
				.param("playerKey", KeyFactory.keyToString(dbPlayer.getKey()));
		queue.add(taskOptions);
	}
}
