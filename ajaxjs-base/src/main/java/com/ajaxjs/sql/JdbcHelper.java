/**
 * Copyright sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.sql;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import com.ajaxjs.sql.annotation.IgnoreDB;
import com.ajaxjs.util.MappingValue;
import com.ajaxjs.util.ReflectUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * SQL 写入到数据库
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class JdbcHelper extends JdbcReader {
	private static final LogHelper LOGGER = LogHelper.getLog(JdbcHelper.class);

	/**
	 * 初始化一个 PreparedStatement并执行
	 * 
	 * @param initPs 初始化一个 PreparedStatement 并返回
	 * @param exe    如何执行SQL？
	 * @param conn   数据库连接对象
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return PreparedStatement 对象
	 */
	static <T> T initAndExe(BiFunction<Connection, String, PreparedStatement> initPs, Function<PreparedStatement, T> exe, Connection conn, String sql, Object... params) {
		String _sql = JdbcUtil.printRealSql(sql, params);
		LOGGER.infoYellow("SQL-->" + _sql);
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
		SQLException[] sqlE = new SQLException[1]; // lambda 不能直接获取异常，于是用个数组装

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
				sqlE[0] = e;
				System.out.println("11111111111111");
				LOGGER.warning(e);
			}

			return null;
		}, conn, sql, params);

		System.out.println("222222222222");
		if (sqlE.length == 1 && sqlE[0] != null) {
			System.out.println(Arrays.toString(sqlE));
			throw new RuntimeException(sqlE[0] + "");
		}

		if (newlyId != null && !(newlyId instanceof Serializable)) {
			LOGGER.warning(String.format("返回 id :{0} 类型:{1}", newlyId, newlyId.getClass().getName()));
			throw new RuntimeException("返回 id 类型不是 Serializable");
		}

		return newlyId == null ? 0 : (Serializable) newlyId;
	}

	/**
	 * 执行 SQL UPDATE 更新。
	 * 
	 * @param conn   数据库连接对象
	 * @param sql    SQL 语句，可以带有 ? 的占位符
	 * @param params 插入到 SQL 中的参数，可单个可多个可不填
	 * @return 成功修改的行数
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
	 * @param isInsert  true=新建记录
	 * @return
	 */
	private static StringBuilder initSB(String tableName, boolean isInsert) {
		StringBuilder sb = new StringBuilder();
		sb.append(isInsert ? "INSERT INTO " : "UPDATE ");
		sb.append("`" + tableName + "`" + " ");

		if (!isInsert)
			sb.append("SET");

		sb.append(" ");

		return sb;
	}

	/**
	 * 遍历 Map
	 * 
	 * @param map     Map 实体
	 * @param onField 每当访问一字段时的回调
	 */
	private static void everyMap(Map<String, Object> map, BiConsumer<String, Object> onField) {
		if (map.size() == 0)
			throw new NullPointerException("该实体没有任何字段和数据");

		for (String field : map.keySet()) {
			if ("id".equals(field))
				continue; // 忽略 id 字段

			onField.accept("`" + field + "`", map.get(field));
		}
	}

	/**
	 * 新建记录，送入的数据是 Map
	 * 
	 * @param conn      数据库连接对象
	 * @param map       Map 实体
	 * @param tableName 表格名称
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
	 * @param map       Map 实体
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
		sb.append(" WHERE id = ?");

		values.add(map.get("id"));

		return update(conn, sb.toString(), values.toArray());
	}

	/**
	 * 
	 * @author sp42 frank@ajaxjs.com
	 *
	 */
	public static class BeanMethod {
		private String fieldName;
		private Method getter;
		private Method setter;
		private Class<?> type;

		private Boolean ignoreDB; // 是否不参与数据库的操作

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

		public Boolean getIgnoreDB() {
			return ignoreDB;
		}

		public void setIgnoreDB(Boolean ignoreDB) {
			this.ignoreDB = ignoreDB;
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

				Method method = property.getReadMethod();

				BeanMethod m = new BeanMethod();
				m.setFieldName(filedName);
				m.setGetter(method);
				m.setSetter(property.getWriteMethod());
				m.setType(property.getPropertyType()); // Bean 值的类型，这是期望传入的类型，也就 setter 参数的类型
				m.setIgnoreDB(method.getAnnotation(IgnoreDB.class) == null);
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

			if (!info.getIgnoreDB())
				continue;

			if (value != null) {// 有值的才进行操作
				if ("id".equals(fieldName))
					continue; // 忽略 id 字段

				onField.accept(fieldName, value);
			}
		}
	}

	/**
	 * 
	 * @param bean Bean 实体
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
	 * @param conn      数据库连接对象
	 * @param bean      Bean 实体
	 * @param tableName 表格名称
	 * @return 新增主键，为兼顾主键类型，返回的类型设为同时兼容 int/long/string 的 Serializable
	 */
	public static Serializable createBean(Connection conn, Object bean, String tableName) {
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

			if (Long.class == idClz && newlyId instanceof Integer)
				ReflectUtil.executeMethod(bean, "setId", new Long((int) newlyId));
			else
				ReflectUtil.executeMethod(bean, "setId", newlyId); // 直接保存
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
			LOGGER.info("更新记录 id:{0}, name:{1}！", ReflectUtil.executeMethod(bean, "getId"), ReflectUtil.executeMethod(bean, "getName"));
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
		sb.append(" WHERE id = ?");

		values.add(ReflectUtil.executeMethod(bean, "getId"));

		return update(conn, sb.toString(), values.toArray());
	}

	/**
	 * 删除一个实体，以是 Map 或 Bean。注意，此方法写死 id 字段
	 * 
	 * @param conn      数据库连接对象
	 * @param bean      实体，可以是 Map 或 Bean
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
