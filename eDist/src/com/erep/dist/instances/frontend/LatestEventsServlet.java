package com.erep.dist.instances.frontend;

import java.io.IOException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.urlfetch.HTTPRequest;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class LatestEventsServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("application/json");
		
		String lang = req.getParameter("lang");
		if (lang == null || lang.trim().isEmpty()) {
			lang = "en";
		}

		String api = "https://api.import.io/store/data/5cc33740-4a29-42b8-8d6b-09d7072139d5/_query?input/webpage/url=http%3A%2F%2Fwww.erepublik.com%2F"
				+ lang
				+ "%2Fmain%2Fnews%2Fmilitary%2Fall%2F1&_user=6e2528a7-5b02-4299-8aec-a53b5dd0c038&_apikey=kBDrJmaSS%2B1VBgSOCDFN2fBhrQxc4tAM4AoIlRW2OQbll8b9%2BOgod8RjxMm7lnkx3pNM0Ky2eYWMSdbaSLqOzQ%3D%3D";

		resp.getWriter().write(fetch(new URL(api)));

	}

	private String fetch(URL link) throws IOException {
		URLFetchService fetchService = URLFetchServiceFactory
				.getURLFetchService();
		HTTPRequest request = new HTTPRequest(link);
		request.getFetchOptions().setDeadline(300d);
		return new String(fetchService.fetch(request).getContent(), "UTF-8");
	}

}
