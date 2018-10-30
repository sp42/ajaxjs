package com.ajaxjs.orm;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ajaxjs.jdbc.JdbcConnection;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 
 * @author Frank Cheung
 *
 */
public class JdbcHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcHelper.class);

	/**
	 * 
	 * @param connection
	 * @param sql
	 * @param hasZeoResult
	 * @param processor
	 * @param params
	 * @return
	 */
	public static <T> T baseSelect(Connection connection, String sql, HasZeoResult hasZeoResult, ResultSetProcessor<T> processor, Object... params) {
		LOGGER.infoYellow("The SQL is---->" + printRealSql(sql, params));

		try (PreparedStatement ps = connection.prepareStatement(sql)) {
			int i = 0;
			for (Object param : params)
				ps.setObject(++i, param);

			try (ResultSet rs = ps.executeQuery()) {
				if (hasZeoResult != null) {
					if (!hasZeoResult.test(connection, rs, sql)) {
						return null;
					}
				}

				return processor.process(rs);
			}
		} catch (SQLException e) {
			LOGGER.warning(e);

			return null;
		}
	}

	/**
	 * 查询单个结果，保存为 Map&lt;String, Object&gt; 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param connection 数据库连接对象
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return Map&lt;String, Object&gt; 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static Map<String, Object> queryAsMap(Connection connection, String sql, Object... params) {
		return baseSelect(connection, sql, JdbcHelper::hasZeoResult, JdbcHelper::getResultMap, params);
	}

	public static <T> T queryAsBean(Class<T> beanClz, Connection connection, String sql, Object... params) {
		return baseSelect(connection, sql, JdbcHelper::hasZeoResult, getResultBean(beanClz), params);
	}

	/**
	 * 记录集合转换为 Map
	 * 
	 * @param rs 记录集合
	 * @return Map 结果
	 * @throws SQLException 转换时的 SQL 异常
	 */
	public static Map<String, Object> getResultMap(ResultSet rs) throws SQLException {
		Map<String, Object> map = new LinkedHashMap<>(); // LinkedHashMap 是 HashMap 的一个子类，保存了记录的插入顺序
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
			String key = rsmd.getColumnLabel(i);
			Object value = rs.getObject(i);

			map.put(key, value);
		}

		return map;
	}

	/**
	 * 记录集合转换为 Map
	 * 
	 * @param rs 记录集合
	 * @return Map 结果
	 * @throws SQLException 转换时的 SQL 异常
	 */
	public static <T> ResultSetProcessor<T> getResultBean(Class<T> beanClz) {
		T bean = ReflectUtil.newInstance(beanClz);

		return rs -> {
			ResultSetMetaData rsmd = rs.getMetaData();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
				String key = rsmd.getColumnLabel(i);
				Object value = rs.getObject(i);

				try {
					PropertyDescriptor propDesc = new PropertyDescriptor(key, beanClz);
					Method method = propDesc.getWriteMethod();
					method.invoke(bean, value);
					// System.out.println("set userName:" + bean.getUserName());
				} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			return bean;
		};
	}

	public static <T> T rs2bean(Class<T> beanClz, ResultSet rs) {
		T bean = ReflectUtil.newInstance(beanClz);

		try {
			ResultSetMetaData rsmd = rs.getMetaData();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
				String key = rsmd.getColumnLabel(i);
				Object value = rs.getObject(i);

				try {
					PropertyDescriptor propDesc = new PropertyDescriptor(key, beanClz);
					Method method = propDesc.getWriteMethod();
					method.invoke(bean, value);
					// System.out.println("set userName:" + bean.getUserName());
				} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			LOGGER.warning(e);
		}

		return bean;

	}

	/**
	 * 查询一组结果，保存为 List<Map<String, Object>> 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return Map<String, Object> 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static List<Map<String, Object>> queryAsMapList(Connection conn, String sql, Object... params) {
		return baseSelect(conn, sql, null, (ResultSet rs) -> {
			List<Map<String, Object>> list = new ArrayList<>();

			while (rs.next())
				list.add(getResultMap(rs));

			return list.size() > 0 ? list : null; // 找不到记录返回 null，不返回空的 list
		}, params);
	}

	/**
	 * 查询一组结果，保存为 List<Map<String, Object>> 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return Map<String, Object> 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static <T> List<T> queryAsBeanList(Class<T> beanClz, Connection conn, String sql, Object... params) {
		return baseSelect(conn, sql, null, (ResultSet rs) -> {
			List<T> list = new ArrayList<>();

			while (rs.next())
				list.add(rs2bean(beanClz, rs));

			return list.size() > 0 ? list : null; // 找不到记录返回 null，不返回空的 list
		}, params);
	}

	@FunctionalInterface
	public static interface HasZeoResult {
		public boolean test(Connection connection, ResultSet rs, String sql) throws SQLException;
	}

	static boolean hasZeoResult(Connection connection, ResultSet rs, String sql) throws SQLException {
		if (isMySql(connection) ? rs.next() : rs.isBeforeFirst()) {
			return true;
		} else {
			LOGGER.info("查询 SQL：{0} 没有符合的记录！", sql);
			return false;
		}
	}

	/**
	 * 是否 mysql 数据库
	 * 
	 * @param conn 数据库连接对象
	 * @return true 表示为 Mysql 数据库
	 */
	private static boolean isMySql(Connection conn) {
		String connStr = conn.toString();
		return connStr.indexOf("MySQL") != -1 || connStr.indexOf("mysql") != -1;
	}

	/**
	 * 有且只有一行记录，并只返回第一列的字段。可指定字段的数据类型
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param clz 期望的结果类型
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 数据库里面的值作为 T 出现
	 */
	@SuppressWarnings("unchecked")
	public static <T> T queryOne(Connection conn, String sql, Class<T> clz, Object... params) {
		Map<String, Object> map = queryAsMap(conn, sql, params);

		if (map != null)
			for (String key : map.keySet()) {// 有且只有一个记录
				Object obj = map.get(key);
				return obj == null ? null : (T) obj;
			}

		return null;
	}

	@FunctionalInterface
	public static interface InitPs {
		public PreparedStatement init(Connection conn, String sql) throws SQLException;
	}

	@FunctionalInterface
	public static interface ExecutePs<T> {
		public T execute(PreparedStatement ps) throws SQLException;
	}

	static <T> T initAndExe(InitPs p, ExecutePs<T> exe, Connection conn, String sql, Object... params) {
		String _sql = printRealSql(sql, params);
		JdbcConnection.addSql(_sql); // 用来保存日志
		LOGGER.infoYellow("The SQL is---->" + _sql);

		try (PreparedStatement ps = p.init(conn, sql);) {
			for (int i = 0; i < params.length; i++)
				ps.setObject(i + 1, params[i]);

			return exe.execute(ps);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 新建记录
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
	 */
	public static Serializable create(Connection conn, String sql, Object... params) {
		Object newlyId = initAndExe((_conn, _sql) -> conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), ps -> {
			ps.executeUpdate();

			// 当保存之后会自动获得数据库返回的主键
			try (ResultSet rs = ps.getGeneratedKeys();) {
				if (rs.next())
					return rs.getObject(1);
			}

			return null;
		}, conn, sql, params);

		if (!(newlyId instanceof Serializable)) {
			LOGGER.warning(String.format("返回 id :{0} 类型:{1}", newlyId, newlyId.getClass().getName()));
			throw new RuntimeException("返回 id 类型不是 Serializable");
		}

		return (Serializable) newlyId;
	}

	/**
	 * 执行 SQL UPDATE 更新。
	 * 
	 * @param conn 数据库连接对象
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 成功修改的行数
	 * @throws SQLException
	 */
	public static int update(Connection conn, String sql, Object... params) {
		return initAndExe((_conn, _sql) -> conn.prepareStatement(sql), ps -> ps.executeUpdate(), conn, sql, params);
	}

	/**
	 * 删除一条记录。写死 id 字段
	 * 
	 * @param conn 数据库连接对象
	 * @param tableName 表格名称
	 * @param id ID
	 * @return 是否删除成功
	 */
	public static boolean deleteById(Connection conn, String tableName, Serializable id) {
		return update(conn, "DELETE FROM " + tableName + " WHERE id = ?", id) == 1;
	}

	/**
	 * 是否关闭日志打印以便提高性能
	 */
	public static boolean isClosePrintRealSql = false;

	/**
	 * 在开发过程，SQL语句有可能写错，如果能把运行时出错的 SQL 语句直接打印出来，那对排错非常方便，因为其可以直接拷贝到数据库客户端进行调试。
	 * 
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 实际 sql 语句
	 */
	public static String printRealSql(String sql, Object[] params) {
		if (isClosePrintRealSql)
			return null;

		if (params == null || params.length == 0) // 完整的 SQL 无须填充
			return sql;

		if (!match(sql, params)) {
			LOGGER.info("SQL 语句中的占位符与值参数（个数上）不匹配。SQL：{0}，\nparams:{1}", sql, Arrays.toString(params));
		}

		if (sql.endsWith("?"))
			sql += " ";

		String[] arr = sql.split("\\?");

		for (int i = 0; i < arr.length - 1; i++) {
			Object value = params[i];

			String inSql;
			if (value instanceof Date) {
				inSql = "'" + value + "'";
			} else if (value instanceof String) {
				inSql = "'" + value + "'";
			} else if (value instanceof Boolean) {
				inSql = (Boolean) value ? "1" : "0";
			} else {
				// number
				inSql = value.toString();
			}

			arr[i] = arr[i] + inSql;
		}

		return String.join(" ", arr).trim();

//		int cols = params.length;
//		Object[] values = new Object[cols];
//		System.arraycopy(params, 0, values, 0, cols);
	}

	/**
	 * ? 和参数的实际个数是否匹配
	 * 
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return true 表示为 ? 和参数的实际个数匹配
	 */
	private static boolean match(String sql, Object[] params) {
		if (params == null || params.length == 0)
			return true; // 没有参数，完整输出

		Matcher m = Pattern.compile("(\\?)").matcher(sql);
		int count = 0;
		while (m.find())
			count++;

		return count == params.length;
	}

	/**
	 * 简单格式化 SQL，当前对 SELECT 语句有效
	 * 
	 * @param sql SELECT 语句
	 * @return 美化后的 SQL
	 */
	public static String formatSql(String sql) {
		String separator = System.getProperty("line.separator");
		sql = '\t' + sql;
		sql = sql.replaceAll("(?i)SELECT\\s+", "SELECT "); // 统一大写
		sql = sql.replaceAll("\\s+(?i)FROM", separator + "\tFROM");
		sql = sql.replaceAll("\\s+(?i)WHERE", separator + "\tWHERE");
		sql = sql.replaceAll("\\s+(?i)GROUP BY", separator + "\tGROUP BY");
		sql = sql.replaceAll("\\s+(?i)ORDER BY", separator + "\tORDER BY");
		sql = sql.replaceAll("\\s+(?i)LIMIT", separator + "\tLIMIT");
		sql = sql.replaceAll("\\s+(?i)DESC", " DESC");
		sql = sql.replaceAll("\\s+(?i)ASC", " ASC");

		return sql;
	}
}
