package com.erep.dist.erep.dist;

import com.erep.dist.data.enums.ResourceQuality;
import com.erep.dist.data.enums.ResourceType;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class DistContext {

	private final DistStrategy									strategy;

	private final Table<ResourceType, ResourceQuality, Integer>	inventory;

	public DistContext(DistStrategy strategy) {
		this.strategy = strategy;
		this.inventory = HashBasedTable.create();
	}

	public boolean execute() {
		return strategy.execute();
	}

}
