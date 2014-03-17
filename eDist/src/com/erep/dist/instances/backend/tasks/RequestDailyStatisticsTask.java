package com.erep.dist.instances.backend.tasks;

import static com.erep.dist.data.enums.RequestType.ROCKET;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.erep.dist.CloseablePM;
import com.erep.dist.PMF;
import com.erep.dist.data.Player;
import com.erep.dist.data.Request;
import com.erep.dist.data.RequestStatsDaily;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.dao.Requests;
import com.erep.dist.data.dao.Statistics;
import com.erep.dist.data.enums.RequestType;
import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.erep.dist.data.enums.RocketQuality;
import com.google.appengine.api.taskqueue.DeferredTask;
import com.google.common.collect.HashBasedTable;

public class RequestDailyStatisticsTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;
	private static Logger		logger				= Logger.getLogger(RequestDailyStatisticsTask.class
															.getName());

	private final Integer		day;

	public RequestDailyStatisticsTask(Integer day) {
		this.day = day;
	}

	@Override
	public void run() {
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			Statistics stats = new Statistics(pm);
			Requests requests = new Requests(pm);

			List<Request> dailyRequests = requests.listAllDaily(day);
			RequestStatsDaily dailyStats = calculateStatistics(dailyRequests);

			stats.save(dailyStats);
		}
	}

	private RequestStatsDaily calculateStatistics(List<Request> dailyRequests) {
		int ff = 0;
		Set<Player> dailyPlayers = new HashSet<>();
		Map<RequestType, Integer> types = new HashMap<>();
		Map<RocketQuality, Integer> rockets = new HashMap<>();
		HashBasedTable<ResourceType, ResourceQuality, Integer> itemsSent = HashBasedTable
				.create();

		for (Request req : dailyRequests) {
			ff += req.getRequestedFF();
			dailyPlayers.add(req.getPlayer());
			add(types, req.getType());
			if (req.getType() == ROCKET)
				add(rockets, req.getRocketQuality());

			for (ResourceDist resource : req.getResources())
				add(itemsSent, resource);
		}

		RequestStatsDaily dailyStats = new RequestStatsDaily();
		dailyStats.setDay(day);
		dailyStats.setFfs(ff);
		dailyStats.setItemsSent(itemsSent);
		dailyStats.setPlayers(dailyPlayers);
		dailyStats.setRequests(types);
		dailyStats.setRockets(rockets);

		return dailyStats;
	}

	private void add(
			HashBasedTable<ResourceType, ResourceQuality, Integer> map,
			ResourceDist resource) {
		Integer current = map.get(resource.getType(), resource.getQuality());
		if (current == null)
			current = 0;
		map.put(resource.getType(), resource.getQuality(),
				current + resource.getQuantity());
	}

	private void add(Map<RequestType, Integer> map, RequestType type) {
		Integer current = map.get(type);
		if (current == null)
			current = 0;
		map.put(type, current + 1);
	}

	private void add(Map<RocketQuality, Integer> map, RocketQuality quality) {
		Integer current = map.get(quality);
		if (current == null)
			current = 0;
		map.put(quality, current + 1);
	}

}
