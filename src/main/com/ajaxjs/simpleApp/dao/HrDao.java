package com.ajaxjs.simpleApp.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.jdbc.PageResult;

public interface HrDao extends IDao<Map<String, Object>, Long> {
	final static String tableName = "job";

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams param);

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Map<String, Object> findById(Long id);

	@Select(value = "SELECT * FROM " + tableName)
	public List<Map<String, Object>> findAll(QueryParams param);

	@Insert(tableName = tableName)
	@Override
	public Long create(Map<String, Object> map);

	@Update(tableName = tableName)
	@Override
	public int update(Map<String, Object> map);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Map<String, Object> map);

	@Select("SELECT * FROM " + tableName + " LIMIT 0, 5")
	public List<Map<String, Object>> getTop5();
}
