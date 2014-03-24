package com.erep.dist.data.dao;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.data.Request;

public class Requests extends GenericDAO {

	public Requests(PersistenceManager pm) {
		super(pm);
	}

	@SuppressWarnings("unchecked")
	public List<Request> listAllDaily(Integer day) {
		Query q = pm().newQuery(Request.class);
		q.setFilter("eRepDay == eRepDayParam");
		q.declareParameters(Integer.class.getName() + " eRepDayParam");
		return (List<Request>) q.execute(day);
	}

}
