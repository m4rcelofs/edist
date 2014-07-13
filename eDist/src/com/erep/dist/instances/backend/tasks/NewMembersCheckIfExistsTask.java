package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.PlayerStatus.DISABLED;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.DailyStats;
import com.erep.dist.data.Player;
import com.erep.dist.data.dao.Players;
import com.erep.dist.data.dao.Stats;
import com.google.appengine.api.taskqueue.DeferredTask;

public class NewMembersCheckIfExistsTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(NewMembersCheckIfExistsTask.class
															.getName());
	private final String		erId;

	public NewMembersCheckIfExistsTask(String id) {
		this.erId = id;
	}

	@Override
	public void run() {
		logger.log(Level.WARNING,
				"Starting NewMembersCheckIfExistsTask for member " + erId);

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			Players players = new Players(pm);
			Player player = new Player(players.createKey(erId));

			try {

				Player dbPlayer = players.find(player);
				if (dbPlayer.getDbStatus() == DISABLED) {

					DailyStats playerStats = new Stats(pm)
							.findLastKnown(dbPlayer);

					if (!playerStats.getIsDead()
							&& !playerStats.getIsPermaBan()) {
						players.enable(dbPlayer);
						logger.log(Level.WARNING, "Player '" + erId
								+ "' auto enabled for being in the MU");
					}
				}

			} catch (JDOObjectNotFoundException e) {
				players.save(player, true);
				logger.log(Level.WARNING, "Player '" + erId
						+ "' auto added for being in the MU");
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
