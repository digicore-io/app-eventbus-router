package com.digipro.ebay.dao;

import java.util.List;

import com.digipro.ebay.model.CompanyAppEvent;

public class CompanyAppEventDao extends BaseDao<CompanyAppEvent> {
	public static final String TABLE = "eventbus-companyAppEvent";

	public CompanyAppEventDao() {
		super(CompanyAppEvent.class);
	}

	public CompanyAppEvent find(String companyId, String event) {

		List<CompanyAppEvent> eventList = findByHashAndRange(TABLE, "companyId-event-index", "companyId", companyId, "event", event);

		if (eventList.size() == 0)
			return null;

		for (CompanyAppEvent appEvent : eventList) {
			if (appEvent.getEvent().equals(event))
				return appEvent;
		}

		return null;
	}

}
