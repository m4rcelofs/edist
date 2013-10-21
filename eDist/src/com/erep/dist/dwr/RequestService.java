package com.erep.dist.dwr;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Transaction;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.ConfigHelper;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.RequestsHelper;
import com.erep.dist.auth.LicensingHelper;
import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.dwr.dto.RequestAttempt;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

@RemoteProxy
public class RequestService {

	private static Logger	logger			= Logger.getLogger(RequestService.class
													.getName());

	private static Pattern	profilePattern	= Pattern
													.compile("(http://|https://)?(www\\.)?erepublik\\.com/\\D{2}/citizen/profile/(?<id>\\d+)");

	@RemoteMethod
	public boolean sendRequest(Player player, Integer requestedFF,
			RequestType type) {

		if (player == null || player.getKey() == null || requestedFF == null
				|| requestedFF <= 0 || type == null)
			return false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			boolean response = false;

			DataAccessObject dao = new DataAccessObject(pm);

			if (!new LicensingHelper(dao).isValidLicence())
				return false;

			Transaction tx = pm.currentTransaction();
			try {

				RequestsHelper helper = new RequestsHelper(dao);

				Player dbPlayer = dao.find(player);

				tx.begin();
				Request request = helper.createRequest(dbPlayer, requestedFF,
						type);

				if (request != null) {
					dbPlayer.addRequest(request);
					dao.save(request);
					helper.addToQueue(request);
					response = true;
				}
				tx.commit();

			} catch (JDOObjectNotFoundException e) {
				return false;
			} finally {
				if (tx.isActive()) {
					tx.rollback();
					response = false;
					logger.log(Level.SEVERE, "Request for player {0} failed",
							player.getName());
				}
			}
			return response;
		}
	}

	@RemoteMethod
	public RequestAttempt getCurrentStatus(String idNameOrLink) {

		RequestAttempt dto = new RequestAttempt();

		if (idNameOrLink == null || idNameOrLink.trim().isEmpty())
			return dto;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("requestAttempt");

			DataAccessObject dao = new DataAccessObject(pm);
			RequestsHelper requests = new RequestsHelper(dao);
			ConfigHelper config = new ConfigHelper(dao);

			Transaction tx = pm.currentTransaction();
			try {
				Player dbPlayer = identifyRequestString(dao,
						idNameOrLink.trim());
				dto.setAllowedType(config.getConfig(dbPlayer).getAllowedType());
				dto.setAllowedQuality(config.getConfig(dbPlayer).getAllowedWeaponQuality());

				tx.begin();
				dto.setRocketAllowed(requests.verifyRocketAllowed(dbPlayer));
				dto.setAvailableFF(requests.verifyMaxFF(dbPlayer));
				tx.commit();

				dto.setRechargeTime(eRepTimeCalculator.secondsToDayChange());
				dto.setPlayer(dbPlayer);

			} catch (JDOObjectNotFoundException e) {
				// no player, ignore
			} finally {
				if (tx.isActive()) {
					tx.rollback();
					logger.log(Level.SEVERE, "getStatus for player {0} failed",
							idNameOrLink);
				}
			}

			return dto;
		}
	}

	private Player identifyRequestString(DataAccessObject dao,
			String idNameOrLink) {
		Player p = new Player();
		try {
			p.setKey(keyFromId(idNameOrLink));
		} catch (NumberFormatException e) {
			Matcher matcher = profilePattern.matcher(idNameOrLink);
			if (matcher.matches())
				p.setKey(keyFromId(matcher.group("id")));
			else
				// assume name
				p.setKey(dao.findPlayerKey(idNameOrLink));
		}
		return dao.find(p);
	}

	private Key keyFromId(String id) {
		Integer keyValue = Integer.parseInt(id);
		return KeyFactory.createKey(Player.class.getSimpleName(), keyValue);
	}

}
