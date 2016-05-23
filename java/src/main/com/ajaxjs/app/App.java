package com.ajaxjs.app;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
//import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.Constant;
import com.ajaxjs.json.AbstractJsEngine;
import com.ajaxjs.json.IEngine;
import com.ajaxjs.json.JsonUtil;
import com.ajaxjs.json.Rhino;
import com.ajaxjs.json.ToJavaType;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.Util;
import com.ajaxjs.util.db.Helper;
import com.ajaxjs.web.PageUtil;

/**
 * 
 * 实现侦听器 子类要继承这个父类，并且加入注解，才能使用侦听器 import javax.servlet.annotation.WebListener;
 * 
 * @author frank
 *
 */
@WebListener
public class App implements ServletContextListener/*, WebApplicationInitializer*/ {
	private static final LogHelper LOGGER = LogHelper.getLog(App.class);
	
	public static boolean isDebug;							// 是否调试模式（开发模式）
	public static boolean isSite_stru_Loaded = false;		// 标识有否加载 Web 目录文件
	public static Map<String, Object> config; 				// 原始配置信息（包含所有的）
	public static final boolean isEnableJS = true;			// 是否启动 JavaScript 引擎
	public static final IEngine jsRuntime = new Rhino();// 主 JS runtime，其他 js 包都导进这里来
	public static final Configuration configuration;		// Configuration 需要设为 public 以便加入 mapper
	public static final SqlSessionFactory sqlSessionFactory;
	private static ServletContext context;
	private static final  String srcFolder = App.class.getClassLoader().getResource("").getPath();
															// 源码磁盘目录
	public static final boolean isMac = System.getProperty("os.name").toLowerCase().contains("mac");
															// 是否苹果系统
	
	static {
		LOGGER.info("启动 App……" + Constant.ConsoleDiver);
		osDectect();
		loadConfig();

		final DataSource ds = getDataSource();// 启动 MyBatis 数据源
		if(ds != null) {
			Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
			configuration = new Configuration(environment);
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
		} else {
			configuration = null;
			sqlSessionFactory = null;
		}
	}

	/**
	 * JVM 要求
	 */
	private static void osDectect() {
//		System.getProperties().list(System.out);
		if (System.getProperty("java.vm.vendor").indexOf("Oracle") == -1 || System.getProperty("java.vm.vendor").contains("openJDK")) {
			LOGGER.warning("本框架不支持 OpenJDK!如果你是 Linux 系统，请把自带的 OpenJDK 卸载，改用 Oracle JVM");
			System.exit(1);
		}

		// 版本检测
		if (System.getProperty("java.version").contains("1.7.") || System.getProperty("java.version").contains("1.8.")) {
		} else {
			LOGGER.warning("请升级你的 JRE/JDK版本 >= 1.7");
			System.exit(1);
		}

		/*
		 * 有两种模式：本地模式和远程模式（自动判断） 返回 true 表示是非 linux 环境，为开发调试的环境，即 isDebug = true； 返回
		 * false 表示在部署的 linux 环境下。 Linux 的为远程模式
		 */
		final String OS = System.getProperty("os.name").toLowerCase();
		isDebug = !(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0);
		// isDebug = false;
	}
	
	/**
	 * 加载配置
	 */
	private static void loadConfig() {
		if(isEnableJS) {
			jsRuntime.eval(AbstractJsEngine.baseJavaScriptCode);
			jsRuntime.load(new String[] {
				Util.getClassFolder_FilePath(App.class, "JSON_Tree.js"), 
				srcFolder + "site_config.js", // 加载配置文件
			});
			
			// 保存全局信息，无论 JSON 配置文件里面嵌套多少层，到这里都扁平化每一条配置
			config = ((ToJavaType)jsRuntime).eval_return_Map("JSON_Tree.util.flat(bf_Config);");
			
			LOGGER.info("加载配置信息如下：" + System.getProperty("line.separator")
					+ JsonUtil.format(com.ajaxjs.json.Json.stringify(config))
					+ Constant.ConsoleDiver);
		}
	}

	/**
	 * 创建数据源连接，这是 Web 框架运行时才能调用的方法。
	 * 
	 * @return 数据源对象
	 */
	public static DataSource getDataSource() {
		String str;
	
		boolean isUsingMySQL = false;// 是否使用 MySql
		if (config.containsKey("app_isUsingMySQL")) {
			isUsingMySQL = (boolean) config.get("app_isUsingMySQL"); 
		}
		if(isMac){
			return Helper.getDataSource("jdbc/sqlite_mac");
		}
		if (isUsingMySQL) {
			return Helper.getDataSource("jdbc/mysql_test");
		} else {
			if (isDebug)
				str = isMac ? "" : "jdbc/sqlite";
			else
				str = "jdbc/sqlite_deploy";

			return Helper.getDataSource(str);
		}
	}
	
	/**
	 * 创建一个数据库连接对象。
	 * 
	 * @return
	 */
	public static Connection getConnection() {
		try {
			return getDataSource().getConnection();
		} catch (SQLException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 修改了配置之后，更新。即时出现效果。
	 */
	public static void updateConfig() {
		config = ((ToJavaType)jsRuntime).eval_return_Map("JSON_Tree.util.flat(bf_Config);");
		context.setAttribute("global_config", config); // 重新保存 config 对象
	}
	
	/**
	 * 初始化系统的 JS 运行时
	 */
	@Override
	public void contextInitialized(ServletContextEvent event) {
		context = event.getServletContext();
		
		initLoggerFileHandler();
		initGlobalMember();
		loadSiteStru();

		LOGGER.info("当前环境：{0}，运行于虚拟目录：{1}", isDebug ? "调试环境" : "正式环境", context.getContextPath() + Constant.ConsoleDiver);
	}

	/**
	 * 把日志文件保存到 WEB-INF/ 下面
	 */
	private static void initLoggerFileHandler() {
		String loggerFile = context.getRealPath("/") + "META-INF" + Constant.file_pathSeparator + "logger" + Constant.file_pathSeparator + "log.txt";
		LogHelper.addHanlder(LogHelper.fileHandlerFactory(loggerFile));
	}

	/**
	 * 建立全局成员，供前端页面调用
	 */
	private static void initGlobalMember() {
		context.setAttribute("bigfoot", context.getContextPath() + "/asset/bigfoot");
		context.setAttribute("global_config", config); // 保存 config 对象
		context.setAttribute("viewUtils", new PageUtil());// 页面工具函数
	}
	
	/**
	 * 加载网站结构
	 */
	private void loadSiteStru() {
		if(isEnableJS) {
			jsRuntime.load(srcFolder + "site_stru.js"); // 加载 Web 目录文件
			jsRuntime.eval("bf.AppStru.init();");
			
			LOGGER.info("加载 site_stru.js 成功");
			isSite_stru_Loaded = true;
//			routeMap = jsRuntime.eval_return_MapArray("bf.AppStru.getRouteMap();");
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		LOGGER.info("Server Destroyed");
	}
	
	/**
	 * Spring 启动时的方法
	 * @param servletContext
	 * @throws ServletException
	 */
//    @Override 
    public void onStartup(ServletContext servletContext) throws ServletException {  
        AnnotationConfigWebApplicationContext webApplicationContext = new AnnotationConfigWebApplicationContext();  
//        webApplicationContext.register(AppConfig.class);  
  
        DispatcherServlet dispatcherServlet = new DispatcherServlet(webApplicationContext);  
        ServletRegistration.Dynamic dynamic = servletContext.addServlet("dispatcherServlet", dispatcherServlet);  
        dynamic.addMapping("/greeting");  
    }  
}
