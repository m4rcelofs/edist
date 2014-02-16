package com.erep.dist.dwr.dto;

import java.util.ArrayList;
import java.util.List;

import org.directwebremoting.annotations.DataTransferObject;
import org.directwebremoting.annotations.RemoteProperty;

import com.erep.dist.data.DailyWork;
import com.erep.dist.data.Player;

@DataTransferObject
public class WorkWeek {

	@RemoteProperty
	private Player			player;

	@RemoteProperty
	// converted to a Map<day, dailywork> client side
	private List<DailyWork>	results	= new ArrayList<>();

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public List<DailyWork> getResults() {
		return results;
	}

	public void setResults(List<DailyWork> results) {
		this.results = results;
	}

}
