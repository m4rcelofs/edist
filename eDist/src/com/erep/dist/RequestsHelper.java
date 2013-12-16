package com.erep.dist;

import static com.erep.dist.data.enums.PlayerDistStatus.TEMP_BANNED;
import static com.erep.dist.data.enums.PlayerStatus.ENABLED;
import static com.erep.dist.data.enums.RequestStatus.REQUESTED;
import static com.erep.dist.data.enums.RequestType.FOOD;
import static com.erep.dist.data.enums.RequestType.ROCKET;
import static com.erep.dist.data.enums.RequestType.WEAPONS;
import static com.erep.dist.data.enums.RequestType.WEAPONS_FOOD;
import static com.erep.dist.data.enums.ResourceQuality.Q7;
import static com.erep.dist.data.enums.RocketQuality.NONE;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withPayload;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.erep.dist.data.DistConfig;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerGroup;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
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

	public boolean verifyRocketAllowed(Player player, PlayerGroup group) {
		if (player.getRocketEnabled() != NONE) {
			if (config.getConfig(player, group).getRocketAllowed()) {
				return !dao.requestedRocket(player, group,
						eRepTimeCalculator.geteRepDay());
			}
		}
		return false;
	}

	public int verifyMaxFF(Player player, PlayerGroup group) {
		if (player.getDbStatus() != ENABLED
				|| player.getFightStatus() == TEMP_BANNED
				|| player.getWorkStatus() == TEMP_BANNED)
			return 0;
		int maxFF = config.getConfig(player, group).getCurrentMaxDailyFF();
		int alreadyRequested = dao.getDailyRequestedFF(player, group,
				eRepTimeCalculator.geteRepDay());
		return (maxFF - alreadyRequested);
	}

	public Request createRequest(Player player, PlayerGroup group) {
		return createRequest(player, group, Integer.MAX_VALUE, WEAPONS_FOOD);
	}

	public Request createRequest(Player player, PlayerGroup group,
			Integer requestedFF, RequestType type) {
		return newRequest(player, group, requestedFF, type);
	}

	private Request newRequest(Player player, PlayerGroup group,
			Integer requestedFF, RequestType requestedType) {

		if (!player.isInGroup(group))
			return null;

		Request request = new Request();
		request.setPlayer(player);
		request.setGroup(group);

		DistConfig distConfig = config.getConfig(player, group);

		if (requestedType == ROCKET) {
			if (!verifyRocketAllowed(player, group))
				return null;
			request.setRequestedFF(0);
			request.setRocketQuality(player.getRocketEnabled());
			request.setResources(getRocketResources(request));
			request.setType(ROCKET);
		} else {

			RequestType allowedType = decideDistType(player, requestedType,
					distConfig);
			request.setType(allowedType);
			int maxFF = verifyMaxFF(player, group);

			if (maxFF <= 0)
				return null;

			request.setRequestedFF(requestedFF < maxFF ? requestedFF : maxFF);
			request.setResources(getFFResources(request, distConfig));
		}

		request.setDistributor(distConfig.getDistributor());
		if (request.getDistributor().getPassword() == null)
			return null;

		request.setRequestedAt(new Date());
		request.seteRepDay(eRepTimeCalculator.geteRepDay());

		request.setIp(req.getRemoteAddr());
		request.setXAppEngineCountry(req.getHeader("X-AppEngine-Country"));
		request.setXAppEngineRegion(req.getHeader("X-AppEngine-Region"));
		request.setXAppEngineCity(req.getHeader("X-AppEngine-City"));

		request.setStatus(REQUESTED);

		return request;
	}

	private List<ResourceDist> getFFResources(Request request,
			DistConfig distConfig) {
		List<ResourceDist> resources = new ArrayList<>();

		RequestType type = request.getType();
		boolean sendFood = type == FOOD || type == WEAPONS_FOOD;
		boolean sendWeapons = type == WEAPONS || type == WEAPONS_FOOD;

		if (sendFood) {
			ResourceDist resource = new ResourceDist(ResourceType.FOOD, null,
					null);
			resources.add(resource);
		}

		if (sendWeapons) {
			ResourceQuality quality = distConfig.getAllowedWeaponQuality();

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
			if (quality != Q7) {
				ResourceDist resource = new ResourceDist(ResourceType.WEAPONS,
						quality, request.getRocketQuality().weaponsNeeded);
				resources.add(resource);
			}
		}

		return resources;
	}

	private RequestType decideDistType(Player player, RequestType requested,
			DistConfig distConfig) {
		if (requested == null)
			return null;

		RequestType allowed = distConfig.getAllowedType();

		if (allowed == WEAPONS_FOOD)
			return requested;

		return allowed;

	}

	public void addToQueue(Request request) {
		DeferredTask task = new SendResourceTask(request);
		Queue queue = QueueFactory.getQueue("erepublik-logged-in");
		queue.add(withPayload(task));
	}

}
