package com.ajaxjs.cms.dao;

import java.util.List;

import com.ajaxjs.cms.model.Catalog;
import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface CatalogDao extends IDao<Catalog, Long> {
	final static String tableName = "catalog";
	
	@Select("SELECT * FROM " + tableName + " WHERE status = 1 AND id = ?")
	@Override
	public Catalog findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value="SELECT * FROM catalog")
	public PageResult<Catalog> findPagedList(QueryParams parame);
	
	@Select(value="SELECT * FROM catalog WHERE parentId = ?")
	public List<Catalog> getListByParentId(int parentId);
	
	
	@Insert(tableName=tableName)
	@Override
	public Long create(Catalog bean);

	@Update(tableName=tableName)
	@Override
	public int update(Catalog bean);

	@Delete(tableName=tableName)
	@Override
	public boolean delete(Catalog bean);
}
