package com.erep.dist.servlets;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.erep.dist.CloseablePM;
import com.erep.dist.DataAccessObject;
import com.erep.dist.PMF;
import com.erep.dist.data.Request;
import com.erep.dist.data.ResourceDist;
import com.erep.dist.data.enums.RequestStatus;
import com.erep.dist.data.enums.ResourceDistStatus;
import com.erep.dist.erep.dist.DistContext;
import com.erep.dist.erep.dist.DistStrategy;

public class SendResourceTask extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String requestId = req.getParameter("id");

		if (requestId == null || requestId.trim().isEmpty())
			return;

		try (CloseablePM pm = PMF.getNewPersistenceManager()) {
			DataAccessObject dao = new DataAccessObject(pm);
			// TODO check licence

			Request request = dao.getRequest(Long.valueOf(requestId));

			if (request == null || request.getStatus() == RequestStatus.SENT)
				return;

			execute(request);

			request.setStatus(RequestStatus.SENT);
			request.setCompletedAt(new Date());
		}
	}

	private void execute(Request request) {
		for (ResourceDist resource : request.getResources())
			if (resource.getStatus() != ResourceDistStatus.SENT)
				try {
					DistContext context = new DistContext(getStrategy(resource));
					context.execute();
				} catch (Exception e) {
					resource.setStatus(ResourceDistStatus.ERROR);
					request.setStatus(RequestStatus.SENT_PARTIAL);
					throw e;
				}

	}

	private DistStrategy getStrategy(ResourceDist resource) {
		// TODO
		return null;
	}
}
