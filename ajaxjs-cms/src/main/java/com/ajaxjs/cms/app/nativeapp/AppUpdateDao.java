package com.ajaxjs.cms.app.nativeapp;

import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "entity_article", beanClass = AppUpdate.class)
public interface AppUpdateDao extends IBaseDao<AppUpdate> {

	@Select("SELECT * FROM ${tableName} WHERE appId = ? ORDER BY apkVersion DESC LIMIT 0, 1")
	public AppUpdate getLastAndroid(int appId);

	@Select("SELECT * FROM ${tableName} WHERE appId = ? ORDER BY iosVersion DESC LIMIT 0, 1")
	public AppUpdate getLastiOS(int appId);

	@Select("SELECT * FROM ${tableName} WHERE status = 1")
	public AppUpdate getCurrentUtility();

}