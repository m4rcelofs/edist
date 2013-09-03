package com.erep.dist.dwr;

import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.Transaction;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.RequestsHelper;
import com.erep.dist.auth.LicensingHelper;
import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.dwr.dto.RequestAttempt;
import com.erep.dist.erep.eRepTimeCalculator;

@RemoteProxy
public class RequestService {

	private static Logger	logger			= Logger.getLogger(RequestService.class
													.getName());

	private static Pattern	profilePattern	= Pattern
													.compile("(http://|https://)?(www\\.)?erepublik\\.com/\\D{2}/citizen/profile/(?<id>\\d+)");

	@RemoteMethod
	public boolean sendRequest(Player player, Integer requestedFF,
			RequestType type) {

		if (player == null || player.getId() == null || requestedFF == null
				|| requestedFF <= 0 || type == null)
			return false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			boolean response = true;

			DataAccessObject dao = new DataAccessObject(pm);

			if (!new LicensingHelper(dao).isValidLicence())
				return false;

			Transaction tx = pm.currentTransaction();
			try {

				RequestsHelper helper = new RequestsHelper(dao);

				Player dbPlayer = dao.find(player);
				if (dbPlayer == null)
					return false;

				Request request = helper.createRequest(dbPlayer, requestedFF,
						type);

				tx.begin();
				player.addRequest(request);
				dao.save(request);
				helper.addToQueue(request);
				tx.commit();

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
			RequestsHelper helper = new RequestsHelper(dao);

			Player dbPlayer = identifyRequestString(dao, idNameOrLink.trim());

			if (dbPlayer != null) {
				dto.setPlayer(dbPlayer);
				dto.setRocketAllowed(helper.verifyRocketAllowed(dbPlayer));
				dto.setAvailableFF(helper.verifyMaxFF(dbPlayer));
				dto.setRechargeTime(eRepTimeCalculator.secondsToDayChange());
			}

			return dto;
		}
	}

	private Player identifyRequestString(DataAccessObject dao,
			String idNameOrLink) {
		Player p = new Player();
		try {
			p.setId(idNameOrLink);
		} catch (NumberFormatException e) {
			Matcher matcher = profilePattern.matcher(idNameOrLink);
			if (matcher.matches())
				p.setId(matcher.group("id"));
			else
				p.setKey(dao.findPlayerKey(idNameOrLink));
		}
		return dao.find(p);
	}

}
