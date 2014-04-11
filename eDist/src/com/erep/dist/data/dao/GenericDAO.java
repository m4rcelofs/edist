package com.erep.dist.data.dao;

import java.util.HashMap;
import java.util.Map;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.erep.dist.ServerConstants;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.datanucleus.query.JDOCursorHelper;

public class GenericDAO {

	private PersistenceManager	pm;

	protected GenericDAO(PersistenceManager pm) {
		this.pm = pm;
	}

	protected PersistenceManager pm() {
		return this.pm;
	}

	protected <T> Query queryWithCursor(Class<T> clazz, String filter,
			String encodedCursor) {
		Query q = pm.newQuery(clazz);
		if (filter != null && !filter.trim().isEmpty())
			q.setFilter(filter);
		setCursor(q, encodedCursor);
		q.setRange(0, ServerConstants.QUERY_PAGE_LIMIT);
		return q;
	}

	private void setCursor(Query query, String encodedCursor) {
		if (encodedCursor != null && !encodedCursor.trim().isEmpty()) {
			Cursor cursor = Cursor.fromWebSafeString(encodedCursor);
			Map<String, Object> extensionMap = new HashMap<>();
			extensionMap.put(JDOCursorHelper.CURSOR_EXTENSION, cursor);
			query.setExtensions(extensionMap);
		}
	}
}
