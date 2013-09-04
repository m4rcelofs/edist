package com.erep.dist;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.eRepTimeCalculator;
import com.google.appengine.api.datastore.KeyFactory;
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
		boolean rocketAllowed = config.getRocketAllowed(player);
		boolean requestedToday = dao.requestedRocket(player,
				eRepTimeCalculator.geteRepDay());
		return rocketAllowed && !requestedToday;
	}

	public int verifyMaxFF(Player player) {
		if (player.getStatus() != PlayerStatus.ENABLED)
			return 0;
		int maxFF = config.getMaxFF(player);
		int alreadyRequested = dao.getDailyRequestedFF(player,
				eRepTimeCalculator.geteRepDay());
		return (maxFF - alreadyRequested);
	}

	public Request createRequest(Player player) {
		return createRequest(player, Integer.MAX_VALUE,
				RequestType.WEAPONS_FOOD);
	}

	public Request createRequest(Player player, Integer requestedFF,
			RequestType type) {
		return newRequest(player, requestedFF, type);
	}

	private Request newRequest(Player player, Integer requestedFF,
			RequestType requestedType) {

		Request request = new Request();
		request.setPlayer(player);
		request.setGroup(player.getGroup());

		if (requestedType == RequestType.ROCKET) {
			if (!config.getRocketAllowed(player))
				return null;
			request.setRocketQuality(player.getRocketEnabled());
			request.setResources(getRocketResources(request));
		} else {

			RequestType allowedType = decideDistType(player, requestedType);
			request.setType(allowedType);
			int maxFF = verifyMaxFF(player);

			if (maxFF <= 0)
				return null;

			request.setRequestedFF(requestedFF < maxFF ? requestedFF : maxFF);
			request.setResources(getFFResources(request));
		}

		request.setDistributor(config.getDistributor(player));

		request.setRequestedAt(new Date());
		request.seteRepDay(eRepTimeCalculator.geteRepDay());
		request.setStatus(RequestStatus.REQUESTED);

		return request;
	}

	private List<ResourceDist> getFFResources(Request request) {
		List<ResourceDist> resources = new ArrayList<>();

		RequestType type = request.getType();
		boolean sendFood = type == RequestType.FOOD
				|| type == RequestType.WEAPONS_FOOD;
		boolean sendWeapons = type == RequestType.WEAPONS
				|| type == RequestType.WEAPONS_FOOD;

		if (sendFood) {
			ResourceDist resource = new ResourceDist(ResourceType.FOOD, null,
					null);
			resources.add(resource);
		}

		if (sendWeapons) {
			ResourceQuality quality = config.getAllowedWeaponQuality(request
					.getPlayer());

			// will always return lower end of division
			ResourceDist resource = new ResourceDist(ResourceType.WEAPONS,
					quality, request.getRequestedFF() / quality.hits);
			resources.add(resource);
		}

		return resources;
	}

	private List<ResourceDist> getRocketResources(Request request) {
		List<ResourceDist> resources = new ArrayList<>();

		for (ResourceQuality quality : ResourceQuality.values()) {
			ResourceDist resource = new ResourceDist(ResourceType.WEAPONS,
					quality, request.getRocketQuality().weaponsNeeded);
			resources.add(resource);
		}

		return resources;
	}

	private RequestType decideDistType(Player player, RequestType requested) {
		if (requested == null)
			return null;

		RequestType allowed = config.getDistType(player);

		if (allowed == RequestType.WEAPONS_FOOD)
			return requested;

		return allowed;

	}

	public void addToQueue(Request request) {
		Queue queue = QueueFactory.getQueue("erepublik-logged-in");
		queue.add(withUrl("/admin/tasks/sendResource").param("key",
				KeyFactory.keyToString(request.getKey())));

	}

}
