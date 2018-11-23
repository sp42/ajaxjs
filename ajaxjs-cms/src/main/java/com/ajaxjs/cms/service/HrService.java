package com.ajaxjs.cms.service;

import java.util.Map;

import com.ajaxjs.framework.service.IService;
import com.ajaxjs.orm.dao.PageResult;

public interface HrService extends IService<Map<String, Object>, Long> {
	public PageResult<Map<String, Object>> findPagedListByCatalogId(int catalogId, int start, int limit);
}