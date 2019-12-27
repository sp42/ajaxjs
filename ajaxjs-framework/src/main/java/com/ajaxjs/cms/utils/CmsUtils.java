package com.ajaxjs.cms.utils;

import org.w3c.dom.NamedNodeMap;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.XMLHelper;

public class CmsUtils {
	/**
	 * 
	 * @param args
	 * @return
	 */
	public static Object[] page2start(Object[] args) {
		int pageStart = (int) args[0];
		int pageSize = (int) args[1];

		if (pageSize == 0)
			pageSize = 8;

		int start = 0;
		if (pageStart >= 1)
			start = (pageStart - 1) * pageSize;

		args[0] = start;

		return args;
	}

	/**
	 * 单测专用的初始化数据库连接方法
	 * 
	 * @param configFile JSON 配置文件路径
	 */
	public static void initTestConnection(String configFile) {
		ConfigService.load(configFile);
		JdbcConnection
				.setConnection(JdbcConnection.getMySqlConnection(ConfigService.getValueAsString("testServer.mysql.url"),
						ConfigService.getValueAsString("testServer.mysql.user"),
						ConfigService.getValueAsString("testServer.mysql.password")));

		DataBaseFilter.isAutoClose = false;
	}

	/**
	 * 
	 * @param configFile JSON 配置文件路径
	 * @param packages   一个或多个搜索的包名
	 */
	public static void initTestDbAndIoc(String configFile, String... packages) {
		initTestConnection(configFile);
		BeanContext.init(packages);
		BeanContext.injectBeans();
	}

	/**
	 * 
	 * @param configFile JSON 配置文件路径
	 * @param dbXmlCfg   数据库配置文件
	 * @param packages   一个或多个搜索的包名
	 */
	public static void init(String configFile, String dbXmlCfg, String... packages) {
		ConfigService.load(configFile);

		XMLHelper.xPath(dbXmlCfg, "//Resource[@name='" + ConfigService.getValueAsString("data.database_node") + "']",
				node -> {
					NamedNodeMap map = node.getAttributes();

					String url = map.getNamedItem("url").getNodeValue(),
							user = map.getNamedItem("username").getNodeValue(),
							password = map.getNamedItem("password").getNodeValue();

					JdbcConnection.setConnection(JdbcConnection.getMySqlConnection(url, user, password));
					DataBaseFilter.isAutoClose = false;
					BeanContext.init(packages);
					BeanContext.injectBeans();
				});
	}

	/**
	 * 方便写单测时用的初始化方法
	 * 
	 * @param projectFolder	项目目录，用于读取配置文件
	 * @param packages      一个或多个搜索的包名
	 */
	public static void init2(String projectFolder, String... packages) {
		init(projectFolder + "\\src\\main\\resources\\site_config.json",
				projectFolder + "\\WebContent\\META-INF\\context.xml", packages);
	}

	public static void loadSQLiteTest(String db) {
		JdbcConnection.setConnection(JdbcConnection.getSqliteConnection(db));
	}
}
