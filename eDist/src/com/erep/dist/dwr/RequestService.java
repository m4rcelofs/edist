package com.erep.dist.dwr;

import static com.erep.dist.data.enums.RequestType.ROCKET;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Transaction;

import org.directwebremoting.WebContext;
import org.directwebremoting.WebContextFactory;
import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.PlayerIdentificator;
import com.erep.dist.RequestsHelper;
import com.erep.dist.auth.LicensingHelper;
import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.dwr.dto.RequestAttempt;
import com.erep.dist.erep.eRepTimeCalculator;

@RemoteProxy
public class RequestService {

	private static Logger	logger	= Logger.getLogger(RequestService.class
											.getName());

	@RemoteMethod
	public boolean sendRequest(Player player, PlayerGroup group,
			Integer requestedFF, RequestType type) {

		if (player == null || player.getKey() == null || requestedFF == null
				|| (requestedFF <= 0 && type != ROCKET) || type == null)
			return false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			boolean response = false;

			DataAccessObject dao = new DataAccessObject(pm);

			if (!new LicensingHelper(dao).isValidLicence())
				return false;

			Transaction tx = pm.currentTransaction();
			try {

				WebContext ctx = WebContextFactory.get();
				RequestsHelper helper = new RequestsHelper(dao,
						ctx.getHttpServletRequest());

				Player dbPlayer = dao.find(player);
				PlayerGroup dbGroup = dao.find(group);

				tx.begin();
				Request request = helper.createRequest(dbPlayer, dbGroup,
						requestedFF, type);

				if (request != null) {
					dbPlayer.addRequest(request);
					dao.save(request);
				}

				tx.commit();

				// transactional queue limit is 5 tasks
				// rocket requests create 6 tasks
				if (request != null) {
					helper.addToQueue(request);
					response = true;
				}

			} catch (JDOObjectNotFoundException e) {
				return false;
			} finally {
				if (tx.isActive()) {
					tx.rollback();
					response = false;
					logger.log(Level.SEVERE, "Request for player {0} failed",
							player.getErId());
				}
			}
			return response;
		}
	}

	@RemoteMethod
	public Player identify(String idNameOrLink) {
		if (idNameOrLink == null || idNameOrLink.trim().isEmpty())
			return new Player();

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("requestIdentify");
			DataAccessObject dao = new DataAccessObject(pm);
			Player p = new PlayerIdentificator(dao)
					.identifyRequestString(idNameOrLink);

			{
				// trying to force load to prevent form block
				p.getGroupIds();
			}

			return p;
		} catch (JDOObjectNotFoundException e) {
			// no player, ignore
		}
		return new Player();
	}

	@RemoteMethod
	public RequestAttempt getCurrentStatus(Player player, PlayerGroup group) {

		RequestAttempt dto = new RequestAttempt();
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("requestAttempt");

			DataAccessObject dao = new DataAccessObject(pm);
			RequestsHelper requests = new RequestsHelper(dao);
			ConfigHelper config = new ConfigHelper(dao);

			Transaction tx = pm.currentTransaction();
			try {
				Player dbPlayer = dao.find(player);
				PlayerGroup dbGroup = dao.find(group);

				DistConfig distConfig = config.getConfig(dbPlayer, dbGroup);

				dto.setAllowedType(distConfig.getAllowedType());
				dto.setAllowedQuality(distConfig.getAllowedWeaponQuality());

				tx.begin();
				dto.setRocketAllowed(requests.verifyRocketAllowed(dbPlayer,
						dbGroup));
				dto.setAvailableFF(requests.verifyMaxFF(dbPlayer, dbGroup));
				tx.commit();

				dto.setRechargeTime(eRepTimeCalculator.secondsToDayChange());
				dto.setGroup(dbGroup);

			} catch (JDOObjectNotFoundException e) {
				// no player, ignore
			} finally {
				if (tx.isActive()) {
					tx.rollback();
					logger.log(Level.SEVERE, "getStatus for player {0} failed",
							player.getErId());
				}
			}

			return dto;
		}
	}

}
