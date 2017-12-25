package com.ajaxjs.simpleApp.service;

import java.util.List;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.simpleApp.model.Catalog;

public interface CatalogService extends IService<Catalog, Long> {
	public List<Catalog> findAll(QueryParams param);
}
