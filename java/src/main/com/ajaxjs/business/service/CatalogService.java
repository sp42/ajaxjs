package com.ajaxjs.business.service;

import com.ajaxjs.business.dao.CatalogDao;
import com.ajaxjs.business.model.Catalog;
import com.ajaxjs.framework.service.BaseService;

public class CatalogService extends BaseService<Catalog, CatalogDao> {
	public CatalogService() {
		setMapper(CatalogDao.class);
		setTableName("catalog");
		setMappingTableName("category");
		setUiName("分类");
	}
}
