package com.ajaxjs.cms.utils;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.orm.JdbcConnection;

public class CmsUtils {
	public static Object[] page2start(Object[] args) {
		int pageStart = (int) args[0];
		int pageSize = (int) args[1];
		
		if (pageSize == 0)
			pageSize = 8;
		
		int start = 0;
		if (pageStart >= 1) {
			start = (pageStart - 1) * pageSize;
		}
		
		args[0] = start;
		return args;
	}

	/**
	 * 单测专用的初始化数据库连接方法
	 * 
	 * @param configFile JSON 配置文件
	 */
	public static void initTestConnection(String configFile) {
		ConfigService.load(configFile);
		JdbcConnection.setConnection(JdbcConnection.getMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"), ConfigService.getValueAsString("testServer.mysql.user"),
				ConfigService.getValueAsString("testServer.mysql.password")));
	
		DataBaseFilter.isAutoClose = false;
	}

	/**
	 * 
	 * @param configFile
	 * @param string2
	 */
	public static void initTestDbAndIoc(String configFile, String... string2) {
		initTestConnection(configFile);
		BeanContext.init(string2);
		BeanContext.injectBeans();
	}
	
	public static void loadSQLiteTest(String db) {
		JdbcConnection.setConnection(JdbcConnection.getSqliteConnection(db));
	}
}
