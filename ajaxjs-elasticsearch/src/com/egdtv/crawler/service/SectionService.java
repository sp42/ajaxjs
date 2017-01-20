package com.egdtv.crawler.service;

import com.ajaxjs.framework.service.BaseService;
import com.egdtv.crawler.dao.SectionDao;
import com.egdtv.crawler.model.Section;

public class SectionService extends BaseService<Section, SectionDao> {
	public SectionService() {
		setMapper(SectionDao.class);
		setTableName("section");
		setUiName("板块");
	}
}
