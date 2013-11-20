package com.erep.dist;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.PlayerDistStatus;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.erep.eRepTimeCalculator;
import com.erep.dist.instances.frontend.tasks.SendResourceTask;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;

public class RequestsHelper {

	private final DataAccessObject		dao;
	private final ConfigHelper			config;
	private final HttpServletRequest	req;

	public RequestsHelper(DataAccessObject dao) {
		this.dao = dao;
		this.config = new ConfigHelper(dao);
		this.req = null;
	}

	public RequestsHelper(DataAccessObject dao, HttpServletRequest req) {
		this.dao = dao;
		this.config = new ConfigHelper(dao);
		this.req = req;
	}

	public boolean verifyRocketAllowed(Player player) {
		boolean rocketAllowed = config.getConfig(player).getRocketAllowed();
		boolean requestedToday = dao.requestedRocket(player,
				eRepTimeCalculator.geteRepDay());
		return rocketAllowed && !requestedToday;
	}

	public int verifyMaxFF(Player player) {
		if (player.getDbStatus() != PlayerStatus.ENABLED
				|| player.getFightStatus() == PlayerDistStatus.TEMP_BANNED
				|| player.getWorkStatus() == PlayerDistStatus.TEMP_BANNED)
			return 0;
		int maxFF = config.getConfig(player).getCurrentMaxDailyFF();
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
			if (!config.getConfig(player).getRocketAllowed())
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

		request.setDistributor(config.getConfig(player).getDistributor());
		if (request.getDistributor().getPassword() == null)
			return null;

		request.setRequestedAt(new Date());
		request.seteRepDay(eRepTimeCalculator.geteRepDay());

		request.setIp(req.getRemoteAddr());
		request.setX_AppEngine_Country(req.getHeader("X-AppEngine-Country"));
		request.setX_AppEngine_Region(req.getHeader("X-AppEngine-Region"));
		request.setX_AppEngine_City(req.getHeader("X-AppEngine-City"));

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
			ResourceQuality quality = config.getConfig(request.getPlayer())
					.getAllowedWeaponQuality();

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

		RequestType allowed = config.getConfig(player).getAllowedType();

		if (allowed == RequestType.WEAPONS_FOOD)
			return requested;

		return allowed;

	}

	public void addToQueue(Request request) {
		DeferredTask task = new SendResourceTask(request);
		Queue queue = QueueFactory.getQueue("erepublik-logged-in");
		queue.add(withPayload(task));
	}

}
