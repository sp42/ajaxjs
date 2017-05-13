package com.ajaxjs.cms.app_manager.dao;

import java.util.Map;

import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface ChannelDao extends IDao<Map<String, Object>, Long> {
	final static String tableName = "channel";
	
	@Select("SELECT * FROM " + tableName + " id = ?")
	@Override
	public Map<String, Object> findById(Long id);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value="SELECT * FROM " + tableName)
	@Override
	public PageResult<Map<String, Object>> findPagedList(QueryParams parame);

	@Insert(tableName=tableName)
	@Override
	public Long create(Map<String, Object> bean);

	@Update(tableName=tableName)
	@Override
	public int update(Map<String, Object> bean);

	@Delete(tableName=tableName)
	@Override
	public boolean delete(Map<String, Object> bean);
}
