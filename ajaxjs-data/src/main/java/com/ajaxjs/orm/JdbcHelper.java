/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.orm;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import com.ajaxjs.orm.JdbcHelperLambda.HasZeoResult;
import com.ajaxjs.orm.JdbcHelperLambda.ResultSetProcessor;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 完成 SQL 到 Java 的转换
 * 
 * @author Frank Cheung
 *
 */
public class JdbcHelper {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcHelper.class);

	/**
	 * Statement 工厂
	 * 
	 * @param conn 		数据库连接对象
	 * @param handle	控制器
	 */
	public static void stmt(Connection conn, Consumer<Statement> handle) {
		try (Statement stmt = conn.createStatement();) {
			handle.accept(stmt);
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * ResultSet 处理器
	 * 
	 * @param stmt		Statement 对象
	 * @param sql		SQL 语句
	 * @param handle	控制器
	 */
	public static void rsHandle(Statement stmt, String sql, Consumer<ResultSet> handle) {
		try (ResultSet rs = stmt.executeQuery(sql);) {
			handle.accept(rs);
		} catch (SQLException e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * stmt + rsHandle
	 * 
	 * @param conn 		数据库连接对象
	 * @param sql 		SQL 语句
	 * @param handle	控制器
	 */
	public static void query(Connection conn, String sql, Consumer<ResultSet> handle) {
		stmt(conn, stmt -> rsHandle(stmt, sql, handle));
	}

	/**
	 * 执行查询
	 * 
	 * @param conn         	数据库连接对象 数据库连接对象
	 * @param sql          	SQL 语句，可以带有 ? 的占位符
	 * @param hasZeoResult	SQL 查询是否有数据返回，没有返回 true
	 * @param processor	   	如何转换RS到目标结果的处理器
	 * @param params       	插入到 SQL 中的参数，可单个可多个可不填
	 * @return	RS 转换后的目标结果
	 */
	public static <T> T select(Connection conn, String sql, HasZeoResult hasZeoResult, ResultSetProcessor<T> processor,
			Object... params) {

		LOGGER.infoYellow("The SQL is---->" + JdbcUtil.printRealSql(sql, params));

		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			if (params != null && params.length > 0) {
				// LogHelper.p(params);
				int i = 0;
				for (Object param : params) {
					ps.setObject(++i, param);
				}
			}

			try (ResultSet rs = ps.executeQuery()) {
				if (hasZeoResult != null) {
					if (!hasZeoResult.test(conn, rs, sql)) {
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
	 * 查询单行记录(单个结果)，保存为  Map<String, Object> 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param conn   	数据库连接对象
	 * @param sql    	SQL 语句，可以带有 ? 的占位符
	 * @param params 	插入到 SQL 中的参数，可单个可多个可不填
	 * @return Map<String, Object> 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static Map<String, Object> queryAsMap(Connection conn, String sql, Object... params) {
		return select(conn, sql, JdbcHelper::hasZeoResult, JdbcHelper::getResultMap, params);
	}

	/**
	 * 查询单行记录(单个结果)，保存为 Bean。如果查询不到任何数据返回 null。
	 * 
	 * @param beanClz		Bean 实体的类
	 * @param connection	数据库连接对象
	 * @param sql			SQL 语句，可以带有 ? 的占位符
	 * @param params		插入到 SQL 中的参数，可单个可多个可不填
	 * @return 查询结果。如果查询不到任何数据返回 null。
	 */
	public static <T> T queryAsBean(Class<T> beanClz, Connection connection, String sql, Object... params) {
		return select(connection, sql, JdbcHelper::hasZeoResult, getResultBean(beanClz), params);
	}

	/**
	 * 记录集合转换为 Map
	 * 
	 * @param rs 记录集合
	 * @return Map 结果
	 * @throws SQLException 转换时的 SQL 异常
	 */
	public static Map<String, Object> getResultMap(ResultSet rs) throws SQLException {
		// LinkedHashMap 是 HashMap 的一个子类，保存了记录的插入顺序
		Map<String, Object> map = new LinkedHashMap<>();
		ResultSetMetaData rsmd = rs.getMetaData();

		for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
			String key = rsmd.getColumnLabel(i);
			Object value = rs.getObject(i);

			map.put(key, value);
		}

		return map;
	}

	/**
	 * 记录集合转换为 bean
	 * 
	 * @param beanClz 实体类
	 * @return ResultSet 处理器，传入 ResultSet 类型对象返回 T 类型的 bean
	 */
	public static <T> ResultSetProcessor<T> getResultBean(Class<T> beanClz) {
		T bean = ReflectUtil.newInstance(beanClz);

		return rs -> {
			ResultSetMetaData rsmd = rs.getMetaData();

			for (int i = 1; i <= rsmd.getColumnCount(); i++) {// 遍历结果集
				String key = rsmd.getColumnLabel(i);
				Object _value = rs.getObject(i);

				try {
					PropertyDescriptor property = new PropertyDescriptor(key, beanClz);
					Method method = property.getWriteMethod();
					Object value = null;

					try {
						value = MappingValue.objectCast(_value, property.getPropertyType());
					} catch (NumberFormatException e) {
						LOGGER.warning(e, "保存数据到 bean 的 {0} 字段时，转换失败，输入值：{1}，输入类型 ：{2}， 期待类型：{3}", key, value,
								value == null ? " 空值 " : value.getClass(), property.getPropertyType());
						continue; // 转换失败，继续下一个字段
					}

					ReflectUtil.executeMethod(bean, method, value);
				} catch (IntrospectionException | IllegalArgumentException e) {
					if (e instanceof IntrospectionException) {// 数据库返回这个字段，但是 bean 没有对应的方法
						// LOGGER.info("数据库返回这个字段 {0}，但是 bean {1} 没有对应的方法", key, beanClz);

						try {
							if ((_value != null) && beanClz.getField("extractData") != null) {
								Object obj = ReflectUtil.executeMethod(bean, "getExtractData");

								// System.out.println(":::::::::key::"+ key +":::v:::" + _value);
								if (obj == null) {
									Map<String, Object> extractData = new HashMap<>();
									ReflectUtil.executeMethod(bean, "setExtractData", extractData);
									obj = ReflectUtil.executeMethod(bean, "getExtractData");
								}

								@SuppressWarnings("unchecked")
								Map<String, Object> map = (Map<String, Object>) obj;
								map.put(key, _value);
							}
						} catch (NoSuchFieldException | SecurityException e1) {
							// LOGGER.warning(e);
						}
						continue;
					}
					LOGGER.warning(e);
				}
			}

			return bean;
		};
	}

	/**
	 * 
	 * @param conn		数据库连接对象
	 * @param sql		SQL 语句，可以带有 ? 的占位符
	 * @param params	插入到 SQL 中的参数，可单个可多个可不填
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> queryAsList(Connection conn, String sql, Object... params) {
		return select(conn, sql, null, rs -> forEachRs(rs, _rs -> (T) rs.getObject(0)), params);
	}

	/**
	 * 查询一组结果，保存为 List&lt;Map&lt;String, Object&gt;&gt; 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param conn   数据库连接对象
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return List&lt;Map&lt;String, Object&gt;&gt; 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static List<Map<String, Object>> queryAsMapList(Connection conn, String sql, Object... params) {
		return select(conn, sql, null, rs -> forEachRs(rs, JdbcHelper::getResultMap), params);
	}

	/**
	 * 查询一组结果，保存为 List<Bean> 结构。如果查询不到任何数据返回 null。
	 * 
	 * @param beanClz	Bean 实体的类
	 * @param conn   	数据库连接对象
	 * @param sql    	SQL 语句，可以带有 ? 的占位符
	 * @param params 	插入到 SQL 中的参数，可单个可多个可不填
	 * @return List<Bean> 结构的结果。如果查询不到任何数据返回 null。
	 */
	public static <T> List<T> queryAsBeanList(Class<T> beanClz, Connection conn, String sql, Object... params) {
		return select(conn, sql, null, rs -> forEachRs(rs, getResultBean(beanClz)), params);
	}

	/**
	 * ResultSet 迭代器
	 * 
	 * @param rs			结果集合
	 * @param processor		单行处理器
	 * @return	多行记录列表集合
	 * @throws SQLException
	 */
	static <T> List<T> forEachRs(ResultSet rs, ResultSetProcessor<T> processor) throws SQLException {
		List<T> list = new ArrayList<>();

		while (rs.next())
			list.add(processor.process(rs));

		return list.size() > 0 ? list : null; // 找不到记录返回 null，不返回空的 list
	}

	/**
	 * 判断 ResultSet 是否有匹配的查询数据
	 * 
	 * @param conn	数据库连接对象
	 * @param rs	结果集合
	 * @param sql	SQL 语句，可以带有 ? 的占位符
	 * @return true 表示有数据
	 * @throws SQLException
	 */
	static boolean hasZeoResult(Connection conn, ResultSet rs, String sql) throws SQLException {
		if (isMySql(conn) ? rs.next() : rs.isBeforeFirst()) {
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
	 * @param conn   数据库连接对象
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param clz    期望的结果类型
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 数据库里面的值作为 T 出现
	 */
	@SuppressWarnings("unchecked")
	public static <T> T queryOne(Connection conn, String sql, Class<T> clz, Object... params) {
		Map<String, Object> map = queryAsMap(conn, sql, params);

		if (map != null)
			for (String key : map.keySet()) {// 有且只有一个记录
				Object obj = map.get(key);
				if (obj == null)
					return null;
				else {
					if (obj instanceof Long && clz == int.class) {
						Object _int = ((Long) obj).intValue();
						return (T) _int;
					}

					if (obj instanceof Integer && (clz == long.class || clz == Long.class)) {
						Object _int = ((Integer) obj).longValue();
						return (T) _int;
					}

					return (T) obj;
				}
			}

		return null;
	}

	/**
	 * 查询数组
	 * 
	 * @param conn 		数据库连接对象
	 * @param sql 		SQL 语句，可以带有 ? 的占位符
	 * @param clz 		注意 Integer.class 不能用 int.class 代替 
	 * @param params 	插入到 SQL 中的参数，可单个可多个可不填
	 * @return 数组结构的结果集合
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] queryArray(Connection conn, String sql, Class<T> clz, Object... params) {
		return select(conn, sql, null, (ResultSet rs) -> {
			List<T> list = forEachRs(rs, _rs -> (T) rs.getObject(1));

			if (list == null)
				return null;

			Object array = Array.newInstance(clz, list.size());// List 转为数组

			for (int i = 0; i < list.size(); i++) {
				Array.set(array, i, list.get(i));
			}

			return (T[]) array;
		}, params);
	}

	/**
	 * 初始化一个 PreparedStatement并执行
	 * 
	 * @param initPs 	初始化一个 PreparedStatement 并返回
	 * @param exe		如何执行SQL？
	 * @param conn   	数据库连接对象
	 * @param sql    	SQL 语句，可以带有 ? 的占位符
	 * @param params 	插入到 SQL 中的参数，可单个可多个可不填
	 * @return PreparedStatement 对象
	 */
	static <T> T initAndExe(BiFunction<Connection, String, PreparedStatement> initPs,
			Function<PreparedStatement, T> exe, Connection conn, String sql, Object... params) {
		String _sql = JdbcUtil.printRealSql(sql, params);
		LOGGER.infoYellow("The SQL is---->" + _sql);
		JdbcConnection.addSql(_sql); // 用来保存日志

		try (PreparedStatement ps = initPs.apply(conn, sql);) {
			for (int i = 0; i < params.length; i++)
				ps.setObject(i + 1, params[i]);

			return exe.apply(ps);
		} catch (SQLException e) {
			LOGGER.warning(e);
			return null;
		}
	}

	/**
	 * 新建记录
	 * 
	 * @param conn   数据库连接对象
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
	 */
	public static Serializable create(Connection conn, String sql, Object... params) {
		Object newlyId = initAndExe((_conn, _sql) -> {
			try {
				return conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			} catch (SQLException e) {
				LOGGER.warning(e);
				return null;
			}
		}, ps -> {
			try {
				ps.executeUpdate();

				// 当保存之后会自动获得数据库返回的主键
				try (ResultSet rs = ps.getGeneratedKeys();) {
					if (rs.next())
						return rs.getObject(1);
				}
			} catch (SQLException e) {
				LOGGER.warning(e);
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
	 * @param conn   数据库连接对象
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 成功修改的行数
	 * @throws SQLException
	 */
	public static int update(Connection conn, String sql, Object... params) {
		return initAndExe((_conn, _sql) -> {
			try {
				return conn.prepareStatement(sql);
			} catch (SQLException e) {
				LOGGER.warning(e);
				return null;
			}
		}, ps -> {
			try {
				return ps.executeUpdate();
			} catch (SQLException e) {
				LOGGER.warning(e);
			}
			return null;
		}, conn, sql, params);
	}

	/**
	 * 
	 * @param tableName 表格名称
	 * @param isInsert	true=新建记录
	 * @return
	 */
	private static StringBuilder initSB(String tableName, boolean isInsert) {
		StringBuilder sb = new StringBuilder();
		sb.append(isInsert ? "INSERT INTO " : "UPDATE ");
		sb.append(tableName + " ");
		if (!isInsert)
			sb.append("SET");
		sb.append(" ");

		return sb;
	}
	
	/**
	 * 遍历 Map
	 * 
	 * @param map		Map 实体
	 * @param onField	每当访问一字段时的回调
	 */
	private static void everyMap(Map<String, Object> map, BiConsumer<String, Object> onField) {
		if (map.size() == 0)
			throw new NullPointerException("该实体没有任何字段和数据");

		for (String field : map.keySet()) {
			if ("id".equals(field))
				continue; // 忽略 id 字段

			onField.accept(field, map.get(field));
		}
	}
	
	/**
	 * 新建记录，送入的数据是 Map
	 * 
	 * @param conn			数据库连接对象
	 * @param map			Map 实体
	 * @param tableName		表格名称
	 * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
	 */
	public static Serializable createMap(Connection conn, Map<String, Object> map, String tableName) {
		LOGGER.info("DAO 创建记录 name:{0}！", map.get("name"));
		
		List<String> fields = new ArrayList<>(), placeholders = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		everyMap(map, (field, value) -> {
			fields.add(field);
			placeholders.add("?");
			values.add(value);
		});

		StringBuilder sb = initSB(tableName, true);
		sb.append("(" + String.join(", ", fields) + ")");
		sb.append(" VALUES (" + String.join(", ", placeholders) + ")");
		
		Serializable newlyId = create(conn, sb.toString(), values.toArray());

		map.put("id", newlyId); // id 一开始是没有的，保存之后才有，现在增加到实体
		return newlyId;
	}

	/**
	 * 修改实体，送入的数据是 Map
	 * 
	 * @param conn      数据库连接对象
	 * @param bean      Bean 实体
	 * @param tableName 表格名称
	 * @return 成功修改的行数，一般为 1
	 */
	public static int updateMap(Connection conn, Map<String, Object> map, String tableName) {
		LOGGER.info("更新记录 id:{0}, name:{1}！", map.get("id"), map.get("name"));

		List<String> fields = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		everyMap(map, (field, value) -> {
			fields.add(field + " = ?");
			values.add(value);
		});

		StringBuilder sb = initSB(tableName, false);
		sb.append(String.join(", ", fields));
		sb.append("WHERE id = ?");

		values.add(map.get("id"));
		
		return update(conn, sb.toString(), values.toArray());
	}

	/**
	 * 
	 * @author Frank Cheung
	 *
	 */
	public static class BeanMethod {
		private String fieldName;
		private Method getter;
		private Method setter;
		private Class<?> type;

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

		public Method getGetter() {
			return getter;
		}

		public void setGetter(Method getter) {
			this.getter = getter;
		}

		public Method getSetter() {
			return setter;
		}

		public void setSetter(Method setter) {
			this.setter = setter;
		}

		public Class<?> getType() {
			return type;
		}

		public void setType(Class<?> type) {
			this.type = type;
		}
	}

	/**
	 * 读取一个 bean 的信息，将其保存在一个 map 中。
	 * 
	 * @param bean Bean 实体
	 * @return 保存 Bean 的信息
	 */
	public static Map<String, BeanMethod> getBeanInfo(Object bean) {
		Map<String, BeanMethod> map = new HashMap<>();
		Class<?> beanClz = bean.getClass();

		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(beanClz);

			for (PropertyDescriptor property : beanInfo.getPropertyDescriptors()) {
				String filedName = property.getName();
				if ("class".equals(filedName))
					continue;

				BeanMethod m = new BeanMethod();
				m.setFieldName(filedName);
				m.setGetter(property.getReadMethod());
				m.setSetter(property.getWriteMethod());
				m.setType(property.getPropertyType()); // Bean 值的类型，这是期望传入的类型，也就 setter 参数的类型
				map.put(filedName, m);
			}
		} catch (IntrospectionException e) {
			LOGGER.warning(e);
		}

		return map;
	}
	
	/**
	 * 遍历 Bean
	 * 
	 * @param bean
	 * @param onField 每当访问一字段时的回调
	 */
	private static void everyBean(Object bean, BiConsumer<String, Object> onField) {
		Map<String, BeanMethod> infoMap = getBeanInfo(bean);

		for (String fieldName : infoMap.keySet()) {
			BeanMethod info = infoMap.get(fieldName);
			Object value = valueHander(bean, info);

			if (value != null) {// 有值的才进行操作
				if ("id".equals(fieldName))
					continue; // 忽略 id 字段


				onField.accept(fieldName, value);
			}
		}
	}

	/**
	 * 
	 * @param bean	Bean 实体
	 * @param info
	 * @return
	 */
	private static Object valueHander(Object bean, BeanMethod info) {
		Object value = ReflectUtil.executeMethod(bean, info.getGetter());
		Class<?> t = info.getType();

		if (value != null && t != value.getClass()) { // 类型相同，直接传入；类型不相同，开始转换
			return MappingValue.objectCast(value, t);
		} else
			return value;
	}

	/**
	 * 新建记录，送入的数据是 Bean
	 * 
	 * @param conn			数据库连接对象
	 * @param bean			Bean 实体
	 * @param tableName		表格名称
	 * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
	 */
	public static Serializable createBean(Connection conn, Object bean, String tableName) {
		try {
			LOGGER.info("创建记录 name:{0}！", ReflectUtil.executeMethod(bean, "getName"));
		} catch (Throwable e) {
		}
		
		List<String> fields = new ArrayList<>(), placeholders = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		everyBean(bean, (field, value) -> {
			fields.add(field);
			placeholders.add("?");
			values.add(value);
		});

		StringBuilder sb = initSB(tableName, true);
		sb.append("(" + String.join(", ", fields) + ")");
		sb.append(" VALUES (" + String.join(", ", placeholders) + ")");

		Serializable newlyId = create(conn, sb.toString(), values.toArray());
		
		try {
			Class<?> idClz = bean.getClass().getMethod("getId").getReturnType();// 根据 getter 推断 id 类型

			if (Long.class == idClz && newlyId instanceof Integer) {
				ReflectUtil.executeMethod(bean, "setId", new Long((int) newlyId));
			} else {
				ReflectUtil.executeMethod(bean, "setId", newlyId); // 直接保存
			}
		} catch (Throwable e) {
			LOGGER.warning(e);
		}

		return newlyId;
	}

	/**
	 * 修改实体，送入的数据是 Map
	 * 
	 * @param conn      数据库连接对象
	 * @param bean      Bean 实体
	 * @param tableName 表格名称
	 * @return 成功修改的行数，一般为 1
	 */
	public static int updateBean(Connection conn, Object bean, String tableName) {
		try {
			LOGGER.info("更新记录 id:{0}, name:{1}！", ReflectUtil.executeMethod(bean, "getId"),
					ReflectUtil.executeMethod(bean, "getName"));
		} catch (Throwable e) {
		}
		
		List<String> fields = new ArrayList<>();
		List<Object> values = new ArrayList<>();

		everyBean(bean, (field, value) -> {
			fields.add(field + " = ?");
			values.add(value);
		});

		StringBuilder sb = initSB(tableName, false);
		sb.append(String.join(", ", fields));
		sb.append("WHERE id = ?");

		values.add(ReflectUtil.executeMethod(bean, "getId"));
		
		return update(conn, sb.toString(), values.toArray());
	}

	/**
	 * 删除一个实体，以是 Map 或 Bean。注意，此方法写死 id 字段
	 * 
	 * @param conn		数据库连接对象
	 * @param bean		实体，可以是 Map 或 Bean
	 * @param tableName 表格名称
	 * @return 是否删除成功，true 表示为成功
	 */
	public static boolean delete(Connection conn, Object bean, String tableName) {
		if (bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) bean;
			return deleteById(conn, tableName, (Serializable) map.get("id"));
		} else
			return deleteById(conn, tableName, (Serializable) ReflectUtil.executeMethod(bean, "getId"));
	}

	/**
	 * 删除一条记录。注意，此方法写死 id 字段
	 * 
	 * @param conn      数据库连接对象
	 * @param tableName 表格名称
	 * @param id        ID
	 * @return 是否删除成功，true 表示为成功
	 */
	public static boolean deleteById(Connection conn, String tableName, Serializable id) {
		return update(conn, "DELETE FROM " + tableName + " WHERE id = ?", id) == 1;
	}
}
