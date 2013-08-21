package com.erep.dist;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.Date;

import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.enums.DistType;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class RequestsHelper {

	private final DataAccessObject	dao;
	private final ConfigHelper		config;

	public RequestsHelper(DataAccessObject dao) {
		this.dao = dao;
		this.config = new ConfigHelper(dao);
	}

	public boolean verifyRocketAllowed(Player player) {
		boolean rocketAllowed = config.getConfigRocketAllowed(player);
		// TODO check dists of the day
		return rocketAllowed;
	}

	public int verifyMaxFF(Player player) {
		int maxFF = config.getConfigMaxFF(player);
		// TODO check dists of the day
		return maxFF;
	}

	public boolean createRequest(Player player) {
		return createRequest(player, Integer.MAX_VALUE, DistType.WEAPONS_FOOD);
	}

	public boolean createRequest(Player player, Integer requestedFF,
			DistType type) {

		Request request = newRequest(player, requestedFF, type);

		if (request == null)
			return false;

		dao.save(request);
		addToQueue(request);
		return true;
	}

	private Request newRequest(Player player, Integer requestedFF,
			DistType requestedType) {
		Request request = new Request();
		request.setRequestedAt(new Date());
		request.seteRepDay(eRepTimeCalculator.geteRepDay());
		request.setStatus(RequestStatus.REQUESTED);

		if (requestedType == DistType.ROCKET) {
			if (!config.getConfigRocketAllowed(player))
				return null;
			request.setRocketQuality(player.getRocketEnabled());
		} else {

			DistType allowedType = decideDistType(player, requestedType);
			request.setType(allowedType);
			int maxFF = verifyMaxFF(player);

			if (maxFF <= 0)
				return null;

			request.setRequestedFF(requestedFF < maxFF ? requestedFF : maxFF);
		}

		request.setPlayer(player);
		request.setGroup(player.getGroup());

		return request;
	}

	private DistType decideDistType(Player player, DistType requested) {
		if (requested == null)
			return null;

		DistType allowed = config.getConfigDistType(player);

		if (allowed == DistType.WEAPONS_FOOD)
			return requested;

		return allowed;

	}

	private void addToQueue(Request request) {
		Queue queue = QueueFactory.getQueue("erepublik-logged-in");
		queue.add(withUrl("/admin/tasks/processRequest").param("id",
				request.getKey().getId() + ""));
	}

}
