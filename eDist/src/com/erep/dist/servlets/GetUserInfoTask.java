package com.erep.dist.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.CloseablePM;
import com.erep.dist.data.Player;
import com.erep.dist.data.enums.PlayerStatus;
import com.erep.dist.erep.CitizenParser;
import com.erep.dist.erep.ErepScrapper;
import com.erep.dist.erep.eRepURL;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

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

			try (ErepScrapper scrapper = new ErepScrapper()) {

				String html = scrapper.scrape(eRepURL.CITIZEN.getUrl(player
						.getErId()));

				Map<String, String> playerInfo = new CitizenParser()
						.parse(html);

				if (playerInfo == null || !playerInfo.containsKey("name")
						|| !playerInfo.containsKey("avatar"))
					throw new IllegalStateException("Info not found! ):");

				player.setName(playerInfo.get("name"));
				player.setAvatar(playerInfo.get("avatar"));
				player.setStatus(PlayerStatus.ENABLED);

			} catch (Exception e) {
				player.setStatus(PlayerStatus.ERROR);
			}
		}

	}

}
