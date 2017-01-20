package com.egdtv.crawler.service;

import com.ajaxjs.framework.service.BaseService;
import com.egdtv.crawler.dao.CatalogDao;
import com.egdtv.crawler.model.Catalog;

public class CatalogService extends BaseService<Catalog, CatalogDao> {
	public CatalogService() {
		setMapper(CatalogDao.class);
		setTableName("catalog");
		setMappingTableName("TOPIC_CATEGORY_INFO");
		
		getHidden_db_field_mapping().put("name", "categoryName");
		setUiName("分类");
	}
}
