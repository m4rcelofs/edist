package com.erep.dist.instances.frontend.install;

import java.io.IOException;

import javax.jdo.Extent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Player;
import com.erep.dist.data.PlayerRoot;

public class PlayerRefreshServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setCharacterEncoding("UTF-8");
		resp.setContentType("text/html");

		resp.getWriter().write("Loading all players...");

		int count = 0;
		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);

			PlayerRoot root = dao.playerRoot();
			Extent<Player> players = pm.getExtent(Player.class);
			for (Player p : players) {
				root.addPlayer(p);
				count++;
			}

			players.closeAll();
		}

		resp.getWriter().write(count + " players loaded and added to root");
	}
}
