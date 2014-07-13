package com.erep.dist.instances.frontend.install;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.io.IOException;

import javax.jdo.Extent;
import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.AvailableStatistics;
import com.erep.dist.data.DailyFights;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerRoot;
import com.erep.dist.data.dao.PlayerStatistics;
import com.erep.dist.data.dao.Players;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class DataMigrationServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");

		String action = req.getParameter("action");
		if (action != null)
			switch (action) {
				case "playerRefresh":
					refreshPlayerCount(resp);
					return;
				case "statsRootRefresh":
					refreshStatsRoot(resp);
					return;
				case "workRootRefresh":
					refreshWorkRoot(resp);
					return;
				case "fightsRootRefresh":
					refreshFightsRoot(resp);
					return;
				case "createAvailableStats":
					createAvailableStats(resp);
					return;
			}

		writeMenu(resp);

	}

	private void writeMenu(HttpServletResponse resp) throws IOException {
		resp.getWriter().write("<h1>eDist - Data migration tool</h1>");

		resp.getWriter().write("<ul>");
		resp.getWriter()
				.write("<li><a href='?action=playerRefresh'>Refresh player count</a></li>");
		resp.getWriter()
				.write("<li><a href='?action=statsRootRefresh'>Refresh stats player root</a></li>");
		resp.getWriter()
				.write("<li><a href='?action=workRootRefresh'>Refresh work player root</a></li>");
		resp.getWriter()
				.write("<li><a href='?action=fightsRootRefresh'>Refresh fights player root</a></li>");
		resp.getWriter()
				.write("<li><a href='?action=createAvailableStats'>Create AvailableStatistics</a></li>");
		resp.getWriter().write("</ul>");
	}

	private void createAvailableStats(HttpServletResponse resp)
			throws IOException {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			try {
				new PlayerStatistics(pm).getAvailable();
				resp.getWriter().write(
						"AvailableStatistics already exists, aborting");
			} catch (JDOObjectNotFoundException e) {
				AvailableStatistics stats = InstallHelper
						.newAvailableStatistics();
				pm.makePersistent(stats);
				resp.getWriter().write("AvailableStatistics created");
			}
		}
	}

	private void refreshStatsRoot(HttpServletResponse resp) throws IOException {
		resp.getWriter().write("Starting...");
		Queue queue = QueueFactory.getDefaultQueue();
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			Extent<DailyStats> stats = pm.getExtent(DailyStats.class);
			for (DailyStats s : stats) {
				final Key key = s.getKey();
				DeferredTask task = new DeferredTask()
				{
					@Override
					public void run() {
						try (CloseablePM pm = PMF.getNewPersistenceManager()) {
							DataAccessObject dao = new DataAccessObject(pm);
							Players players = new Players(pm);
							DailyStats stat = dao.find(new DailyStats(key));
							if (stat.getPlayer() == null) {
								Key parentKey = stat.getKey().getParent();
								Player parent = players.find(new Player(parentKey));
								stat.setPlayer(parent);
							}
						}
					}
				};
				queue.add(withPayload(task));
			}
			stats.closeAll();
		}
		resp.getWriter().write("Added to queue.");
	}

	private void refreshWorkRoot(HttpServletResponse resp) throws IOException {
		resp.getWriter().write("Starting...");
		Queue queue = QueueFactory.getDefaultQueue();
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			Extent<DailyWork> stats = pm.getExtent(DailyWork.class);
			for (DailyWork s : stats) {
				final Key key = s.getKey();
				DeferredTask task = new DeferredTask()
				{
					@Override
					public void run() {
						try (CloseablePM pm = PMF.getNewPersistenceManager()) {
							DataAccessObject dao = new DataAccessObject(pm);
							Players players = new Players(pm);
							DailyWork stat = dao.find(new DailyWork(key));
							if (stat.getPlayer() == null) {
								Key parentKey = stat.getKey().getParent();
								Player parent = players.find(new Player(parentKey));
								stat.setPlayer(parent);
							}
						}
					}
				};
				queue.add(withPayload(task));
			}
			stats.closeAll();
		}
		resp.getWriter().write("Added to queue.");
	}

	private void refreshFightsRoot(HttpServletResponse resp) throws IOException {
		resp.getWriter().write("Starting...");
		Queue queue = QueueFactory.getDefaultQueue();
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			Extent<DailyFights> stats = pm.getExtent(DailyFights.class);
			for (DailyFights s : stats) {
				final Key key = s.getKey();
				DeferredTask task = new DeferredTask()
				{
					@Override
					public void run() {
						try (CloseablePM pm = PMF.getNewPersistenceManager()) {
							DataAccessObject dao = new DataAccessObject(pm);
							Players players = new Players(pm);
							DailyFights stat = dao.find(new DailyFights(key));
							if (stat.getPlayer() == null) {
								Key parentKey = stat.getKey().getParent();
								Player parent = players.find(new Player(parentKey));
								stat.setPlayer(parent);
							}
						}
					}
				};
				queue.add(withPayload(task));
			}
			stats.closeAll();
		}
		resp.getWriter().write("Added to queue.");
	}

	private void refreshPlayerCount(HttpServletResponse resp)
			throws IOException {
		resp.getWriter().write("Loading all players...");

		int count = 0;
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			Players players = new Players(pm);

			PlayerRoot root = players.root();
			Extent<Player> allPlayers = players.listAll();
			for (Player p : allPlayers) {
				root.addPlayer(p);
				count++;
			}

			allPlayers.closeAll();
		}

		resp.getWriter().write(count + " players loaded and added to root");
	}

}
