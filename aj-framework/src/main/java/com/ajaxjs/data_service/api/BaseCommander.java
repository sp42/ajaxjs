package com.ajaxjs.data_service.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.ajaxjs.Version;
import com.ajaxjs.data_service.model.DataServiceConstant;
import com.ajaxjs.data_service.model.DataServiceDml;
import com.ajaxjs.data_service.mybatis.MybatisInterceptor;
import com.ajaxjs.data_service.mybatis.SqlMapper;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.JdbcUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 
 * @author Frank Cheung
 *
 */
public abstract class BaseCommander implements DataServiceConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseCommander.class);

	/**
	 * 手动创建连接池。这里使用了 Tomcat JDBC Pool
	 *
	 * @param driver
	 * @param url
	 * @param user
	 * @param psw
	 * @return 数据源
	 */
	public static DataSource setupJdbcPool(String driver, String url, String user, String psw) {
		PoolProperties p = new PoolProperties();
		p.setDriverClassName(driver);
		p.setUrl(url);
		p.setUsername(user);
		p.setPassword(psw);
		p.setMaxActive(100);
		p.setInitialSize(10);
		p.setMaxWait(10000);
		p.setMaxIdle(30);
		p.setMinIdle(5);
		p.setTestOnBorrow(true);
		p.setTestWhileIdle(true);
		p.setTestOnReturn(true);
		p.setValidationInterval(18800);
		p.setDefaultAutoCommit(true);
		org.apache.tomcat.jdbc.pool.DataSource ds = new org.apache.tomcat.jdbc.pool.DataSource();
		ds.setPoolProperties(p);

		return ds;
	}

	/**
	 * 根据数据源创建 Mybatis 会话
	 *
	 * @param ds 数据源
	 * @return SQL 会话
	 */
	static SqlSession getMyBatisSession(DataSource ds) {
		Objects.requireNonNull(ds, "数据源未准备好");

		Environment environment = new Environment("development", new JdbcTransactionFactory(), ds);
		Configuration configuration = new Configuration(environment);
		configuration.setUseGeneratedKeys(true);
		configuration.setCallSettersOnNulls(true);// 设置为 true 为 null 字段也会查询出来

		if (Version.isDebug)
			configuration.addInterceptor(new MybatisInterceptor()); // 打印 SQL

		SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(configuration);

		return factory.openSession();
	}

	/**
	 * 查询单个记录
	 *
	 * @param ds     数据源
	 * @param sql    SQL 语句
	 * @param params SQL 参数
	 * @return 单个记录的 Map 如果返回 null 找不到数据
	 */
	public static Map<String, Object> info(DataSource ds, String sql, Map<String, Object> params) {
		LOGGER.info("查询单个记录");

		try (SqlSession session = getMyBatisSession(ds)) {
			return new SqlMapper(session).selectOne(sql, params);
		}
	}

	/**
	 * 普通列表
	 *
	 * @param ds     数据源
	 * @param sql    SQL 语句
	 * @param params SQL 参数
	 * @return 列表的 JSON
	 */
	public static List<Map<String, Object>> list(DataSource ds, String sql, Map<String, Object> params) {
//		LOGGER.info("获取列表（不分页）");

		boolean isDynamicSQL = Commander.isDynamicSQL(sql);
		if (isDynamicSQL) {// 是否包含 mybatis 脚本控制标签，有的话特殊处理
			sql = "<script>" + sql + "</script>";
		}

		try (SqlSession session = getMyBatisSession(ds)) {
			return new SqlMapper(session).selectList(sql, params);
		}
	}

	/**
	 * 分页列表
	 *
	 * @param ds     数据源
	 * @param sql    SQL 语句
	 * @param params SQL 参数
	 * @return 分页列表的 JSON
	 */
	public static PageResult<Map<String, Object>> page(DataSource ds, String sql, Map<String, Object> params) {
		LOGGER.info("获取列表");

		if (params == null)
			params = new HashMap<>();

		/* 判断分页参数，兼容 MySQL or 页面两者。最后统一使用 start/limit */
		int limit;

		if (params.containsKey("pageSize"))
			limit = (int) params.get("pageSize");
		else if (params.containsKey("limit"))
			limit = (int) params.get("limit");
		else
			limit = PageResult.DEFAULT_PAGE_SIZE;

		int start;

		if (params.containsKey("pageNo")) {
			int pageNo = (int) params.get("pageNo");
			start = JdbcUtil.pageNo2start(pageNo, limit);
		} else if (params.containsKey("start"))
			start = (int) params.get("start");
		else
			start = 0;

		params.put("start", start);
		params.put("limit", limit);

		try (SqlSession session = getMyBatisSession(ds)) {
			SqlMapper sqlMapper = new SqlMapper(session); // 先获取记录总数，若大于零继续查询，获取分页数据

//            int index = sql.lastIndexOf(";"); // 有分号影响子查询，去掉
//            if (index > 0)
//                sql = sql.substring(0, index);

			String getTotalSql = "SELECT COUNT(*) AS count FROM (" + sql + ") AS t;";
			Map<String, Object> _params;
			boolean isDynamicSQL = isDynamicSQL(sql);

			if (isDynamicSQL) {// 是否包含 mybatis 脚本控制标签，有的话特殊处理
				getTotalSql = "<script>" + getTotalSql + "</script>";
				_params = new HashMap<>();
				_params.put("params", params);
			} else
				_params = params;

			Map<String, Object> t = sqlMapper.selectOne(getTotalSql, params);
			Long total = (Long) t.get("count");

			PageResult<Map<String, Object>> pageList = new PageResult<>();
			pageList.setTotalCount(total.intValue());

			if (total == 0) {
				pageList.setZero(true);
//                Map<String, Object> map = new HashMap<>();
//                map.put("result", null);
//                map.put("msg", "没有数据，查询结果为零");
//                map.put("total", 0);
			} else {
				sql += " LIMIT #{start}, #{limit}";

				if (isDynamicSQL)
					sql = "<script>" + sql + "</script>";

				List<Map<String, Object>> _list = sqlMapper.selectList(sql, params);
				pageList.addAll(_list);
			}

			return pageList;
		}
	}

	/**
	 * 物理删除一笔记录
	 *
	 * @param ds
	 * @param sql
	 * @param params
	 * @return true 表示删除成功
	 */
	public static boolean delete(DataSource ds, String sql, Map<String, Object> params) {
		try (SqlSession session = getMyBatisSession(ds)) {
			int rows = new SqlMapper(session).delete(sql, params);
			session.commit();

			return rows >= 1;
		}
	}

	/**
	 * URL 匹配命令。根据两个输入条件找到匹配的 DML 命令
	 * 
	 * @param uri    命令 URL
	 * @param states 状态 Map
	 * @return
	 */
	public static DataServiceDml exec(String uri, Map<String, DataServiceDml> states) {
		if (states.containsKey(uri)) {
			DataServiceDml node = states.get(uri);

			if (node.isEnable())
				return node;
			else
				throw new RuntimeException("该命令 [" + uri + "] 未启用");
		} else {
			System.out.println(states);
			throw new RuntimeException("不存在该路径 [" + uri + "] 之配置；或者未初始化数据服务");
		}
	}

	/**
	 * 是否包含 mybatis 脚本控制标签，有的话特殊处理
	 * <p>
	 * https://mybatis.org/mybatis-3/dynamic-sql.html
	 *
	 * @param sql
	 * @return
	 */
	static boolean isDynamicSQL(String sql) {
		return sql.contains("</foreach>") || sql.contains("<if test") || sql.contains("<choose>") || sql.contains("<set>");
	}
}
