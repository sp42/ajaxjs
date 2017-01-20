package com.egdtv.crawler.service;

import com.ajaxjs.framework.service.BaseService;
import com.egdtv.crawler.dao.PortalDao;
import com.egdtv.crawler.model.Portal;

public class PortalService extends BaseService<Portal, PortalDao> {
	public PortalService() {
		setMapper(PortalDao.class);
		setTableName("REPTILE_PORTAL");
		setUiName("门户");
	}
}
