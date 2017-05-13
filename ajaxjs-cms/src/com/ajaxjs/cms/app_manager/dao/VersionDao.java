package com.ajaxjs.cms.app_manager.dao;

import com.ajaxjs.cms.app_manager.model.Version;
import com.ajaxjs.framework.dao.IDao;
import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.dao.annotation.Delete;
import com.ajaxjs.framework.dao.annotation.Insert;
import com.ajaxjs.framework.dao.annotation.Select;
import com.ajaxjs.framework.dao.annotation.Update;
import com.ajaxjs.framework.model.PageResult;

public interface VersionDao extends IDao<Version, Long> {
	final static String tableName = "version";
	final static String select = 
			  "SELECT version.*, channel.name AS channelName, portal.name AS portalName FROM " + tableName  + " INNER JOIN channel ON version.channelId = channel.channel "
			+ "INNER JOIN portal ON version.portalId = portal.id";
	
	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public Version findById(Long id);
	
	/**
	 * 获取最新版本
	 * @return
	 */
	@Select(select + " WHERE portalId = ? AND channelId = ? ORDER BY version DESC LIMIT 1 ")
	public Version selectTopVersion(int portalId, String channelId);
	
	@Select("SELECT COUNT(*) AS Total FROM " + tableName)
	@Override
	public int count();
	
	@Select(value=select)
	public PageResult<Version> findPagedList(QueryParams parame);
	
	@Insert(tableName=tableName)
	@Override
	public Long create(Version bean);

	@Update(tableName=tableName)
	@Override
	public int update(Version bean);

	@Delete(tableName=tableName)
	@Override
	public boolean delete(Version bean);
}
