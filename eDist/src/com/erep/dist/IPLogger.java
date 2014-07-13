package com.erep.dist;

import javax.servlet.http.HttpServletRequest;

import com.erep.dist.data.HasIPLog;

public class IPLogger {

	private final HttpServletRequest	req;

	public IPLogger(HttpServletRequest req) {
		this.req = req;
	}

	public void setIP(HasIPLog dbElement) {
		if (req != null) {
			dbElement.setIp(req.getRemoteAddr());
			dbElement
					.setXAppEngineCountry(req.getHeader("X-AppEngine-Country"));
			dbElement.setXAppEngineRegion(req.getHeader("X-AppEngine-Region"));
			dbElement.setXAppEngineCity(req.getHeader("X-AppEngine-City"));
		} else {
			dbElement.setIp("Automatic dist");
		}
	}

}
