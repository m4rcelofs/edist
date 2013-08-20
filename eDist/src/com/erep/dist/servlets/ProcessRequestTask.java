package com.erep.dist.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Request;

public class ProcessRequestTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String requestId = req.getParameter("id");

		if (requestId == null || requestId.trim().isEmpty())
			return;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {

			DataAccessObject dao = new DataAccessObject(pm);
			Request request = dao.getRequest(Long.valueOf(requestId));

			// TODO
			request.setDistributor(dao.listDistributors().get(0)); // test
		}

	}
}
