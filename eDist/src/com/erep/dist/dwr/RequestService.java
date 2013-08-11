package com.erep.dist.dwr;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.ServerConstants;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.enums.DistType;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.erep.dist.dwr.dto.RequestAttempt;

@RemoteProxy
public class RequestService {

	private static Pattern	profilePattern	= Pattern
													.compile("(http://|https://)?(www\\.)?erepublik\\.com/\\D{2,3}/citizen/profile/(?<id>\\d+)");

	@RemoteMethod
	public boolean sendRequest(Player player, Integer requestedFF, DistType type) {
		// TODO
		return false;
	}

	@RemoteMethod
	public RequestAttempt getCurrentStatus(String idNameOrLink) {

		RequestAttempt dto = new RequestAttempt();

		if (idNameOrLink == null || idNameOrLink.trim().isEmpty())
			return dto;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			Player p = identifyRequestString(dao, idNameOrLink.trim());
			// TODO
		}

		//
		dto.setAvailableFF(160);
		dto.setRocketEnabled(true);

		Player player = new Player();
		player.setAvatar("http://static.erepublik.net/uploads/avatars/Citizens/2011/11/11/a77319cdddeccec56e795892984edec9.jpg");
		player.setId(5313713l);
		player.setName("nW0lf");
		player.setRocketEnabled(RocketQuality.Q5);
		player.setStatus(PlayerStatus.ENABLED);
		PlayerGroup group = new PlayerGroup();
		group.setId(ServerConstants.DEFAULT_ID);
		group.setGroupName("default");
		player.setGroup(group);
		dto.setPlayer(player);
		//
		return dto;
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
