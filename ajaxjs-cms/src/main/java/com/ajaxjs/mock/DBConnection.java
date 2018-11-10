package com.ajaxjs.mock;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.orm.JdbcConnection;

public class DBConnection {

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

	public static void initTestDbAndIoc(String configFile, String... string2) {
		initTestConnection(configFile);
		BeanContext.init(string2);
	}
}
