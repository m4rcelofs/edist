package com.erep.dist.instances.frontend.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.esxx.js.protocol.GAEConnectionManager;
import org.json.simple.JSONObject;

import com.erep.dist.data.Request;
import com.google.appengine.api.taskqueue.DeferredTask;

public class FindFakesTask implements DeferredTask {

	private static final long serialVersionUID = 1L;
	private static final Logger logger = Logger.getLogger(FindFakesTask.class
			.getName());

	private final Request request;

	public FindFakesTask(Request request) {
		this.request = request;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		logger.log(Level.WARNING, "Starting FindFakesTask");

		JSONObject json= new JSONObject();
		json.put("nick", request.getPlayer().getName());
		
		JSONObject time = new JSONObject();
		{
			time.put(".sv", "timestamp");
		}
		json.put("time", time);
		
		
		String ip = request.getIp().replace('.', '_');
		String id = request.getPlayer().getErId();
		String url = "https://madruga.firebaseio.com/ips/" + ip + "/" + id + ".json";

		try {
			HttpPut put = new HttpPut(url);
			put.setEntity(new StringEntity(json.toString()));
			
			DefaultHttpClient client = new DefaultHttpClient(
					new GAEConnectionManager());
			HttpResponse response = client.execute(put);
			EntityUtils.consume(response.getEntity());
		} catch (Exception e) {
			logger.log(Level.WARNING, "Could not log IP", e);
		}
	}
}
