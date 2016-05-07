package com.ajaxjs.app;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;

import com.ajaxjs.framework.dao.DAO;
import com.ajaxjs.framework.dao.SqlProvider;
import com.ajaxjs.framework.model.BaseModel;
import com.ajaxjs.util.db.Helper;

/**
 * 初始化数据库服务
 * 
 * @author frank
 *
 */
public class DBinit {

	static {
// 		如果要观察 MyBatis 动态生成的 SQL 语句，请打开控制台的输出。
//		org.apache.ibatis.logging.LogFactory.useStdOutLogging();
//		org.apache.ibatis.logging.LogFactory.useJdkLogging();
		
		// 加载通用的映射器
		if (!App.configuration.hasMapper(SqlProvider.class)) {
			 App.configuration.addMapper(SqlProvider.class);
		}
	}

	/**
	 * 必须手动关闭 SqlSession。
	 * 
	 * @param mapperClz 该参数可以为 null。
	 * @return
	 */
	public static SqlSession loadSession(Class<? extends DAO<? extends BaseModel>> mapperClz) {
		if (mapperClz != null && !App.configuration.hasMapper(mapperClz)) {
			App.configuration.addMapper(mapperClz);
		}
		
		return App.sqlSessionFactory.openSession();
	}

	/**
	 * 创建数据源连接，这是 Web 框架运行时才能调用的方法。
	 * 
	 * @return 数据源对象
	 */
	public static DataSource getDataSource() {
		String str;
	
		boolean isUsingMySQL = false;// 是否使用 MySql
		if (App.config.containsKey("app_isUsingMySQL")) {
			isUsingMySQL = (boolean) App.config.get("app_isUsingMySQL"); 
		}
		
		if (isUsingMySQL) {
			return Helper.getDataSource("jdbc/mysql");
		} else {
			if (App.isDebug)
				str = App.isMac ? "" : "jdbc/sqlite";
			else
				str = "jdbc/sqlite_deploy";

			return Helper.getDataSource(str);
		}
	}
}
