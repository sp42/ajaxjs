/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
 * 
 * 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。
 * 用户可从下列网址获得许可证副本：
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework;

import org.w3c.dom.NamedNodeMap;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.util.XMLHelper;

/**
 * 测试辅助类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MockTest {
	/**
	 * 单测专用的初始化数据库连接方法
	 * 
	 * @param configFile JSON 配置文件路径
	 */
	@Deprecated
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
	@Deprecated
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
	 * @param projectFolder 项目目录，用于读取配置文件
	 * @param packages      一个或多个搜索的包名
	 */
	public static void init2(String projectFolder, String... packages) {
		init(projectFolder + "\\WebContent\\META-INF\\site_config.json",
				projectFolder + "\\WebContent\\META-INF\\context.xml", packages);
	}

	/**
	 * 加载 SQLite 数据库
	 * 
	 * @param db
	 */
	public static void loadSQLiteTest(String db) {
		JdbcConnection.setConnection(JdbcConnection.getSqliteConnection(db));
	}
}
