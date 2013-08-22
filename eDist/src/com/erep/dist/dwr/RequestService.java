package com.erep.dist.dwr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.RequestsHelper;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.DistType;
import com.erep.dist.dwr.dto.RequestAttempt;

@RemoteProxy
public class RequestService {

	private static Pattern	profilePattern	= Pattern
													.compile("(http://|https://)?(www\\.)?erepublik\\.com/\\D{2,3}/citizen/profile/(?<id>\\d+)");

	@RemoteMethod
	public boolean sendRequest(Player player, Integer requestedFF, DistType type) {

		if (player == null || player.getId() == null || requestedFF == null
				|| requestedFF <= 0 || type == null)
			return false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			RequestsHelper helper = new RequestsHelper(dao);

			Player dbPlayer = dao.find(player);
			if (dbPlayer == null)
				return false;

			return helper.createRequest(dbPlayer, requestedFF, type);

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
			}

			return dto;
		}
	}

	private Player identifyRequestString(DataAccessObject dao,
			String idNameOrLink) {
		Player p = new Player();
		try {
			p.setId(Long.valueOf(idNameOrLink));
		} catch (NumberFormatException e) {
			Matcher matcher = profilePattern.matcher(idNameOrLink);
			if (matcher.matches())
				p.setId(Long.valueOf(matcher.group("id")));
			else
				p.setKey(dao.findPlayerKey(idNameOrLink));
		}
		return dao.find(p);
	}

}
