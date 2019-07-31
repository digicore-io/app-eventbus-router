package com.digipro.ebay.dao;

import com.digipro.ebay.model.EventLog;

public class EventLogDao extends BaseDao<EventLog> {
	public static final String TABLE = "eventbus-eventLog";

	public EventLogDao() {
		super(EventLog.class);
	}

}
