package com.ajaxjs.cms.dao;

import java.util.Map;

import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

public interface GlobalLogDao extends IDao<Map<String, Object>, Integer> {
	final static String tableName = "general_log";

	@Select(value = "SELECT * FROM " + tableName + " ORDER BY id DESC")
	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams param, int start, int limit);

	@Select(value = "SELECT * FROM " + tableName + " ORDER BY id DESC")
	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit);

	@Insert(tableName = tableName)
	@Override
	public Integer create(Map<String, Object> map);
}
