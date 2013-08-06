package com.erep.dist.dwr;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RocketQuality;
import com.erep.dist.dwr.dto.RequestAttempt;

@RemoteProxy
public class RequestService {

	@RemoteMethod
	public RequestAttempt getCurrentStatus(String idNameOrLink) {
		RequestAttempt dto = new RequestAttempt();
		//TODO
		dto.setAvailableFF(160);
		dto.setRocketEnabled(true);

		Player player = new Player();
		player.setAvatar("http://static.erepublik.net/uploads/avatars/Citizens/2011/11/11/a77319cdddeccec56e795892984edec9.jpg");
		player.setId("5313713");
		player.setName("nW0lf");
		player.setRocketEnabled(RocketQuality.Q5);
		player.setStatus(PlayerStatus.ENABLED);
		dto.setPlayer(player);

		return dto;
	}
}
