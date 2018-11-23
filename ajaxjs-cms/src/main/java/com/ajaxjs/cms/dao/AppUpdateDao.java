package com.ajaxjs.cms.dao;

import com.ajaxjs.cms.model.AppUpdate;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;
import com.ajaxjs.orm.dao.IDao;
import com.ajaxjs.orm.dao.PageResult;

public interface AppUpdateDao extends IDao<AppUpdate, Long> {
	final static String tableName = "entity_app_update";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public AppUpdate findById(Long id);
	
	@Select(value = "SELECT * FROM " + tableName + " ORDER BY id DESC")
	@Override
	public PageResult<AppUpdate> findPagedList(int start, int limit);

	@Select("SELECT * FROM " + tableName + " WHERE appId = ? ORDER BY apkVersion DESC LIMIT 0, 1")
	public AppUpdate getLastAndroid(int appId);
	
	@Select("SELECT * FROM " + tableName + " WHERE appId = ? ORDER BY iosVersion DESC LIMIT 0, 1")
	public AppUpdate getLastiOS(int appId);

	@Select("SELECT * FROM " + tableName + " WHERE status = 1")
	public AppUpdate getCurrentUtility();
	
	@Insert(tableName = tableName)
	@Override
	public Long create(AppUpdate entry);

	@Update(tableName = tableName)
	@Override
	public int update(AppUpdate entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(AppUpdate entry);
}