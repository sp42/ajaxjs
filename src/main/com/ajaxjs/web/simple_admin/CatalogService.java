package com.ajaxjs.web.simple_admin;

import java.util.List;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.service.IService;

public interface CatalogService extends IService<Catalog, Long> {
	public List<Catalog> findAll(QueryParams param);
}
