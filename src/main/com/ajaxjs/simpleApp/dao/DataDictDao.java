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

public interface DataDictDao extends IDao<Map<String, Object>, Integer> {
	final static String tableName = "general_data_dict";

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams param);
	
	@Select(value = "SELECT * FROM " + tableName)
	public PageResult<Map<String, Object>> findAdminPagedList(QueryParams param);
	
	@Select("SELECT * FROM " + tableName + " WHERE status = 1 AND id = ?")
	@Override
	public Map<String, Object> findById(Integer id);

	@Select(value = "SELECT * FROM " + tableName)
	public List<Map<String, Object>> findAll(QueryParams param);
	
	@Insert(tableName = tableName)
	@Override
	public Integer create(Map<String, Object> map);

	@Update(tableName = tableName)
	@Override
	public int update(Map<String, Object> map);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Map<String, Object> map);
}
