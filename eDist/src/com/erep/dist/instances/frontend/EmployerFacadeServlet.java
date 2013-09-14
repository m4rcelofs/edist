package com.erep.dist.instances.frontend;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOObjectNotFoundException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class EmployerFacadeServlet extends HttpServlet {

	private static final long	serialVersionUID	= 1L;

	private static Logger		logger				= Logger.getLogger(EmployerFacadeServlet.class
															.getName());

	@Override
	protected void doPut(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO

		String param = req.getParameter("json");
		if (!checkParams(param)) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}

		logger.log(Level.WARNING, "Received update from employer: " + param);

		try {
			JSONParser parser = new JSONParser();
			JSONObject jsObj = (JSONObject) parser.parse(param);
			// TODO
			
		} catch (ParseException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		} catch (JDOObjectNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

	}

	private boolean checkParams(String... params) {
		for (String param : params)
			if (param == null || param.isEmpty())
				return false;
		return true;
	}

}
