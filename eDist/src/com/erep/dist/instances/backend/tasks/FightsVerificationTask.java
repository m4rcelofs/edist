package com.erep.dist.instances.backend.tasks;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.taskqueue.DeferredTask;

public class FightsVerificationTask implements DeferredTask {

	private static final long	serialVersionUID	= 1L;

	private final Key			playerKey;

	public FightsVerificationTask(Key playerKey) {
		this.playerKey = playerKey;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

}
