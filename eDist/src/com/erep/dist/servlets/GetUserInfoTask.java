package com.erep.dist.servlets;

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
import com.erep.dist.erep.CitizenParser;
import com.erep.dist.erep.eRepURL;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.urlfetch.HTTPResponse;
import com.google.appengine.api.urlfetch.URLFetchService;
import com.google.appengine.api.urlfetch.URLFetchServiceFactory;

public class GetUserInfoTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String erId = req.getParameter("id");

		if (erId == null || erId.trim().isEmpty())
			return;

		Key playerId = KeyFactory.createKey(Player.class.getSimpleName(), erId);

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);
			Player player = dao.getPlayer(playerId);

			URLFetchService fetchService = URLFetchServiceFactory
					.getURLFetchService();
			HTTPResponse response = fetchService.fetch(new URL(eRepURL.CITIZEN
					.getUrl(player.getId())));

			if (response.getResponseCode() == 404) {
				player.setStatus(PlayerStatus.ERROR);
				return;
			}

			String html = IOUtils.toString(response.getContent(), "UTF-8");

			Map<String, String> playerInfo = new CitizenParser().parse(html);

			if (playerInfo == null || !playerInfo.containsKey("name")
					|| !playerInfo.containsKey("avatar"))
				throw new IllegalStateException("Info not found! ):");

			player.setName(playerInfo.get("name"));
			player.setAvatar(playerInfo.get("avatar"));
			player.setStatus(PlayerStatus.ENABLED);

			CacheFacade
					.remove("com.erep.dist.DataAccessObject.findPlayerId(String="
							+ player.getName() + ";)");
		}

	}
}
