package com.ajaxjs.cms.service;

import com.ajaxjs.framework.EntityMap;
import com.ajaxjs.framework.IBaseService;
import com.ajaxjs.orm.dao.PageResult;

public interface HrService extends IBaseService<EntityMap> {
	public PageResult<EntityMap> findPagedListByCatalogId(int catalogId, int start, int limit);
}