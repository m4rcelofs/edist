package com.erep.dist.dwr;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.ServerConstants;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.enums.DistType;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.erep.dist.dwr.dto.RequestAttempt;

@RemoteProxy
public class RequestService {

	@RemoteMethod
	public boolean sendRequest(Player player, Integer requestedFF, DistType type) {
		return false;
	}

	@RemoteMethod
	public RequestAttempt getCurrentStatus(String idNameOrLink) {
		RequestAttempt dto = new RequestAttempt();

		Player p = identifyRequestString(idNameOrLink);

		// TODO
		dto.setAvailableFF(160);
		dto.setRocketEnabled(true);

		Player player = new Player();
		player.setAvatar("http://static.erepublik.net/uploads/avatars/Citizens/2011/11/11/a77319cdddeccec56e795892984edec9.jpg");
		player.setId("5313713");
		player.setName("nW0lf");
		player.setRocketEnabled(RocketQuality.Q5);
		player.setStatus(PlayerStatus.ENABLED);
		PlayerGroup group = new PlayerGroup();
		group.setId(ServerConstants.DEFAULT);
		group.setGroupName("default");
		player.setGroup(group);
		dto.setPlayer(player);
		//
		return dto;
	}

	private Player identifyRequestString(String idNameOrLink) {
		// TODO Auto-generated method stub
		return null;
	}
}
