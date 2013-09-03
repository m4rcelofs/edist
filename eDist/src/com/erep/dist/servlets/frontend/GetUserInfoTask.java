package com.erep.dist.servlets.frontend;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.cache.CacheFacade;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.erep.eRepURL;
import com.erep.dist.erep.parser.CitizenParser;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class GetUserInfoTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String erId = req.getParameter("key");

		if (erId == null || erId.trim().isEmpty())
			return;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);

			Player p = new Player();
			p.setId(erId);

			Player dbPlayer = dao.find(p);

			URLFetchService fetchService = URLFetchServiceFactory
					.getURLFetchService();
			HTTPResponse response = fetchService.fetch(new URL(eRepURL.CITIZEN
					.getUrl(dbPlayer)));

			if (response.getResponseCode() == 404) {
				dbPlayer.setStatus(PlayerStatus.ERROR);
				return;
			}

			String html = IOUtils.toString(response.getContent(), "UTF-8");

			Map<String, String> playerInfo = new CitizenParser().parse(html);

			if (playerInfo == null || !playerInfo.containsKey("name")
					|| !playerInfo.containsKey("avatar"))
				throw new IllegalStateException("Info not found! ):");

			dbPlayer.setName(playerInfo.get("name"));
			dbPlayer.setAvatar(playerInfo.get("avatar"));
			dbPlayer.setStatus(PlayerStatus.ENABLED);

			CacheFacade
					.remove("com.erep.dist.DataAccessObject.findPlayerId(String="
							+ dbPlayer.getName() + ";)");
		}

	}
}
