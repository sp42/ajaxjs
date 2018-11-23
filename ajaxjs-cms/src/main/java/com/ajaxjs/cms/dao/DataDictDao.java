package com.ajaxjs.cms.dao;

import java.util.List;
import java.util.Map;

import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface DataDictDao extends IDao<Map<String, Object>, Integer> {
	final static String tableName = "general_data_dict";

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<Map<String, Object>> findPagedList( int start, int limit);
	
	@Select("SELECT * FROM " + tableName + " WHERE status = 1 AND id = ?")
	@Override
	public Map<String, Object> findById(Integer id);

	@Select(value = "SELECT * FROM " + tableName)
	public List<Map<String, Object>> findAll();
	
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
