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
import com.ajaxjs.simpleApp.Constant;
import com.ajaxjs.simpleApp.model.Catalog;

public interface NewsDao extends IDao<Map<String, Object>, Long> {
	final static String tableName = "news";

	@Select(value = "SELECT * FROM " + tableName + " WHERE status = 1")
	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams param);
	
	@Select(value = "SELECT * FROM " + tableName)
	public PageResult<Map<String, Object>> findAdminPagedList(QueryParams param);
	
	@Select("SELECT * FROM " + tableName + " WHERE status = 1 AND id = ?")
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
	
	@Select("SELECT * FROM " + CatalogDao.tableName + " WHERE parentId = " + Constant.NewsCatalogId)
	public List<Catalog> getHrCatalog();
}
