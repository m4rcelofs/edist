package com.erep.dist.dwr;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.directwebremoting.annotations.RemoteMethod;
import org.directwebremoting.annotations.RemoteProxy;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.enums.DistType;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.dwr.dto.RequestAttempt;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

@RemoteProxy
public class RequestService {

	private static Pattern	profilePattern	= Pattern
													.compile("(http://|https://)?(www\\.)?erepublik\\.com/\\D{2,3}/citizen/profile/(?<id>\\d+)");

	@RemoteMethod
	public boolean sendRequest(Player player, Integer requestedFF, DistType type) {

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			Player dbPlayer = dao.find(player);
			Request request = newRequest(dbPlayer, requestedFF, type);

			addToQueue(request);

			return true;
		}
	}

	private Request newRequest(Player player, Integer requestedFF, DistType type) {
		Request request = new Request();
		request.setRequestedAt(new Date());
		request.setType(type);
		request.setStatus(RequestStatus.REQUESTED);

		if (type == DistType.ROCKET) {
			// TODO
			// request.setQuality(quality)
		} else {
			// TODO verify
			request.setRequestedFF(requestedFF);
		}

		request.setPlayer(player);
		player.addRequest(request);

		return request;
	}

	private void addToQueue(Request request) {
		Queue queue = QueueFactory.getQueue("erepublik-logged-in");
		queue.add(withUrl("/admin/tasks/processRequest").param("id",
				request.getKey().getId() + ""));
	}

	@RemoteMethod
	public RequestAttempt getCurrentStatus(String idNameOrLink) {

		RequestAttempt dto = new RequestAttempt();

		if (idNameOrLink == null || idNameOrLink.trim().isEmpty())
			return dto;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			pm.getFetchPlan().addGroup("requestAttempt");

			DataAccessObject dao = new DataAccessObject(pm);

			Player p = identifyRequestString(dao, idNameOrLink.trim());
			dto.setPlayer(p);
			// TODO
		}

		//
		dto.setAvailableFF(160);
		dto.setRocketAllowed(false);
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
