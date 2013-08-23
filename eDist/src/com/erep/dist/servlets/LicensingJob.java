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

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			LicensingHelper licensing = new LicensingHelper(dao);
			licensing.forceLicenceRefresh();

			licensing.validate(); // TODO email if not valid
		}

	}

}
