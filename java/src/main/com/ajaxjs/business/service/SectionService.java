package com.ajaxjs.business.service;

import com.ajaxjs.business.dao.SectionDao;
import com.ajaxjs.business.model.Section;
import com.ajaxjs.framework.service.BaseService;

public class SectionService extends BaseService<Section, SectionDao> {
	public SectionService() {
		setMapper(SectionDao.class);
		setTableName("section");
		setUiName("栏目");
	}
}
