package com.ajaxjs.cms.dao;

import com.ajaxjs.cms.model.Portal;
import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface PortalDao extends IDao<Portal, Long> {
	final static String tableName = "portal";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Portal findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value="SELECT * FROM " + tableName)
	@Override
	public PageResult<Portal> findPagedList(QueryParams parame);
	
	@Insert(tableName=tableName)
	@Override
	public Long create(Portal bean);

	@Update(tableName=tableName)
	@Override
	public int update(Portal bean);

	@Delete(tableName=tableName)
	@Override
	public boolean delete(Portal bean);
}
