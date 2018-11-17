package com.ajaxjs.cms.service;

import java.util.Map;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

public interface HrService extends IService<Map<String, Object>, Long> {
	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, QueryParams param, int start, int limit);
}