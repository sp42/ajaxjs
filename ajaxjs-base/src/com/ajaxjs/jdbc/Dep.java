package com.ajaxjs.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;

import com.ajaxjs.Constant;
import com.ajaxjs.framework.dao.BaseDao;
import com.ajaxjs.framework.dao.SqlProvider;

public class Dep {
	
	// https://docs.oracle.com/javase/tutorial/jdbc/basics/cachedrowset.html
	// https://db.apache.org/derby/docs/10.9/ref/rrefjdbc4_1summary.html
	// http://download.oracle.com/otn-pub/jcp/jdbc-4_1-mrel-spec/jdbc4.1-fr-spec.pdf?AuthParam=1465096926_08a5ed33264923a8a52d98cd8df37692
	public static void create() {

		try (JdbcRowSet jdbcRs = RowSetProvider.newFactory().createJdbcRowSet();) {

			// 创建一个 JdbcRowSet 对象，配置数据库连接属性
			jdbcRs.setUrl("jdbc:derby:helloDB;");
			// jdbcRs.setUsername(username);
			// jdbcRs.setPassword(password);
			jdbcRs.setCommand("select name from hellotable");

			jdbcRs.execute();
			while (jdbcRs.next()) {
				System.out.println(jdbcRs.getString(1));
			}
			// delete the table
			// s.execute("drop table hellotable");
			// System.out.println("Dropped table hellotable");

			// rs.close();

			// DriverManager.getConnection("jdbc:derby:;shutdown=true");
		} catch (SQLException e) {
			// 通过应用 JDBC 4.0, 您现在不需太多代码即可以获取及遍历异常链在以往的版本中, 您在遍历异常链时，必须手工的调用
			// getNextException 方法才能得到相同的效果
			for (Throwable ex : e) {
				System.err.println("Error encountered: " + ex);
			}
		}
	}
	
	/**
	 * 查询结果作为 String 返回 查询的 SQL 语句，如果查询成功有这笔记录，返回 true，否则返回 false（检查有无记录）
	 * 
	 * @param rs
	 *            结果集
	 * @param classz
	 *            期望的类型
	 * @return 数据库里面的值作为 T 出现
	 */
	@SuppressWarnings("unchecked")
	public static <T> T queryAs(ResultSet rs, Class<T> classz) {
		try {
			if (String.class == classz) {
				return rs.isBeforeFirst() ? (T) rs.getString(1) : null;
			} else if (Integer.class == classz) {
				// if (jdbcConnStr.indexOf("MySQL") != -1 || jdbcConnStr.indexOf("mysql") != -1) {
				//     result = rs.next() ? rs.getInt(1) : null;
				// } else {// sqlite
				//      result = rs.isBeforeFirst() ? rs.getInt(1) : null;
				// }
				return rs.isBeforeFirst() ? (T) (Integer) rs.getInt(1) : null;
			} else if (Boolean.class == classz) {
				return (T) (Boolean) rs.isBeforeFirst();
			}
		} catch (SQLException e) {
		}
		
		return null;
	}
	
	/**
	 * Configuration 需要设为 public 以便加入 mapper
	 */
	public static Configuration configuration;

	/**
	 * 一般用法 sqlSessionFactory.openSession()。注意，返回的 SqlSession 必须关闭
	 */
	public static SqlSessionFactory sqlSessionFactory;
	
	/**
	 * 初始化 MyBatis 数据链接服务
	 * 
	 * @param ds
	 */
	public static void init(DataSource ds) {
		Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
		configuration = new Configuration(environment);
		sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);

		// 打印数据库连接路径，以便了解
		try (Connection conn = environment.getDataSource().getConnection();){
			System.out.println("数据库连接字符串：" + conn + Constant.ConsoleDiver);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
		// 如果要观察 MyBatis 动态生成的 SQL 语句，请打开控制台的输出。
		// org.apache.ibatis.logging.LogFactory.useStdOutLogging();
		// org.apache.ibatis.logging.LogFactory.useJdkLogging();

		// 加载通用的映射器
		if (!configuration.hasMapper(SqlProvider.class))
			configuration.addMapper(SqlProvider.class);

		System.out.println("数据库初始化成功！" + Constant.ConsoleDiver);
	}
	
	/**
	 * 添加一个 Mapper（DAO）
	 * @param clz
	 */
	public static void addDao(Class<? extends BaseDao<?, ?>> clz){
		if (clz != null && !configuration.hasMapper(clz))
			configuration.addMapper(clz);
	}
	
	public static String perRecordSql  = "SELECT %s, name FROM %s WHERE createDate < %s ORDER BY createDate DESC LIMIT 1";
	public static String nextRecordSql = "SELECT %s, name FROM %s WHERE createDate > %s ORDER BY createDate ASC LIMIT 1";

	/**
	 * 记录集合转换为 Map
	 * @param conn
	 * @param sql
	 * @return Map 结果
	 */
	public static Map<String, Object> queryMap(Connection conn, String sql) {
		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			if (rs.isBeforeFirst()) {
				return Helper.getResultMap(rs);
			} else {
				System.err.println("查询 SQL：" + sql + " 没有符合的记录！");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
    
	/**
	 * 记录集合列表转换为 List
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param sql
	 *            查询的 SQL 语句
	 * @return List 结果
	 */
	public static List<Map<String, Object>> queryList(Connection conn, String sql) {
		List<Map<String, Object>> list = new ArrayList<>();
		
		try (Statement statement = conn.createStatement(); ResultSet rs = statement.executeQuery(sql);) {
			while (rs.next()) {
				list.add(Helper.getResultMap(rs));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		
		return list;
	}
}
