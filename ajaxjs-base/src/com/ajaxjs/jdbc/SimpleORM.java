package com.ajaxjs.jdbc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ajaxjs.jdbc.sqlbuilder.SqlBuilder;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.map.Map2Pojo;
import com.ajaxjs.util.reflect.BeanUtil;

public class SimpleORM<T> extends Helper {
	private static final LogHelper LOGGER = LogHelper.getLog(SimpleORM.class);
	
	private Class<T> entryType;

	private Connection conn;

	/**
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param entryType
	 */
	public SimpleORM(Connection conn, Class<T> entryType) {
		this.conn = conn;
		this.setEntryType(entryType);
	}

	/**
	 * 记录集合转换为 Map
	 * 
	 * @param sql SQL 语句，可以带有 ? 的占位符
	 * @return Map 结果
	 */
	@SuppressWarnings("unchecked")
	public T query(String sql, Object... params) {
		Map<String, Object> map = query(conn, sql, params);
		
		// 如是 map 直接返回即可
		return entryType == Map.class ? (T) map : new Map2Pojo<T>(entryType).map2pojo(map);
	}

	@SuppressWarnings("unchecked")
	public List<T> queryList(String sql, Object... params) {
		List<Map<String, Object>> list = queryList(conn, sql, params);
		System.out.println(entryType);
		System.out.println(new Map2Pojo<T>(entryType));
		return entryType == Map.class ? (List<T>) list : new Map2Pojo<T>(entryType).map2pojo(list);
	}
	
	public Object create(T obj, String tableName) {
		SqlBuilder sql = new SqlBuilder();
		sql.INSERT_INTO(tableName);
		addFieldValues(sql, obj, obj.getClass().getMethods(), false, null);
		System.out.println(sql.toString());
		return create(conn, sql.toString());
	}
	
	public int update(final T obj, final String tableName) {
		final Method[] methods = obj.getClass().getMethods();
		LOGGER.info("DAO 更新记录 {0}！", obj);
		
		String sql = new SqlBuilder() {
			{
				UPDATE(tableName);
				addFieldValues(this, obj, methods, true, null);
				WHERE("id = #{id}");
			}
		}.toString();
		
		int e = update(conn, sql);
		
		return e;
	}
	
	/**
	 * 
	 * @param sql
	 *            动态 SQL 实例
	 * @param model
	 *            实体
	 * @param methods
	 *            反射获取字段
	 * @param isSet
	 *            true=Update/false=Create
	 */
	private void addFieldValues(SqlBuilder sql, Object bean, Method[] methods, boolean isSet, Map<String, String> fieldMapping) {
		for (Method method : methods) { // 反射获取字段
			String methodName = method.getName();

			if (isOk_field(methodName)) {
				try {
					if (method.invoke(bean) != null) {
						methodName = BeanUtil.getFieldName(methodName);
						String pojoName = methodName;
						
						// 字段映射
						if (fieldMapping != null && fieldMapping.size() > 0 && fieldMapping.containsKey(methodName)) {
							pojoName = methodName;
							methodName = fieldMapping.get(methodName);
						}

						if (isSet)
							sql.SET(methodName + "= #{" + pojoName + "}");
						else
							sql.VALUES(methodName, "#{" + pojoName + "}");
					}
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					LOGGER.warning(e);
				}
			}
		}
	}
	
	/**
	 * 不是 pojo 所有的字段都要，这里判断
	 * 
	 * @param methodName
	 *            方法名称
	 * @return
	 */
	private static boolean isOk_field(String methodName) {
		return (methodName.startsWith("get") || methodName.startsWith("is")) && !"getId".equals(methodName)
				&& !"getClass".equals(methodName) && !"getService".equals(methodName);
	}
	
	public Class<T> getEntryType() {
		return entryType;
	}

	public void setEntryType(Class<T> entryType) {
		this.entryType = entryType;
	}
}
