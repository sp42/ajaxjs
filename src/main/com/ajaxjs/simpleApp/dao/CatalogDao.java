package com.ajaxjs.simpleApp.dao;

import java.util.List;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.simpleApp.model.Catalog;

public interface CatalogDao extends IDao<Catalog, Long> {
	final static String tableName = "category";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Catalog findById(Long id);

	@Select(value = "SELECT * FROM " + tableName)
	public List<Catalog> findAll(QueryParams param);
	
	@Insert(tableName = tableName)
	@Override
	public Long create(Catalog map);

	@Update(tableName = tableName)
	@Override
	public int update(Catalog map);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(Catalog map);
}
