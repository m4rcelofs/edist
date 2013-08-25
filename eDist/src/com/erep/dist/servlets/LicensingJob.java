package com.erep.dist.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.auth.LicensingHelper;

public class LicensingJob extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");

		boolean sendEmail = req.getParameter("sendEmail") != null ? Boolean
				.valueOf(req.getParameter("sendEmail")) : false;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			LicensingHelper licensing = new LicensingHelper(dao);

			boolean refreshed = false;
			do {
				resp.getWriter().println("Starting licence refresh...");
				refreshed = licensing.forceLicenceRefresh();
			} while (!refreshed);

			licensing.validate(); // TODO email if not valid

			resp.getWriter().println("Finished licence refresh");
			resp.getWriter().println(
					"Your licence is valid until eRepublik day "
							+ licensing.getLicenceExpiration());
		}

	}

}
