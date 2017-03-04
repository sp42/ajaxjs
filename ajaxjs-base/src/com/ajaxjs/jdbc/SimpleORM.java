package com.ajaxjs.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ajaxjs.jdbc.sqlbuilder.CommonSQL;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.map.Map2Pojo;
import com.ajaxjs.util.reflect.Reflect;

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
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
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

		return entryType == Map.class ? (List<T>) list : new Map2Pojo<T>(entryType).map2pojo(list);
	}
	
	/**
	 * 
	 * 新建实体
	 * 
	 * @param bean
	 *            Bean 实体
	 * @param tableName
	 *            表格名称
	 * @return 新增主键
	 */
	public Serializable create(T bean, String tableName) {
		try {
			LOGGER.info("DAO 创建记录 name:{0}！", Reflect.executeMethod(bean, "getName"));
		} catch (Throwable e) {}

		CommonSQL sql = new CommonSQL();
		Object[] values = sql.insert(bean, tableName);

		return create(conn, sql.toString(), values);
	}

	/**
	 * 修改实体
	 * 
	 * @param bean
	 *            Bean 实体
	 * @param tableName
	 *            表格名称
	 * @return 成功修改的行数
	 */
	public int update(T bean, String tableName) {
		try {
			LOGGER.info("DAO 更新记录 id:{0}, name:{1}！", Reflect.executeMethod(bean, "getId"), Reflect.executeMethod(bean, "getName"));
		} catch (Throwable e) {}

		CommonSQL sql = new CommonSQL();
		Object[] values = sql.update(bean, tableName);

		return update(conn, sql.toString(), values);
	}
	
	
	/**
	 * 删除实体
	 * 
	 * @param bean
	 *            Bean 实体
	 * @param tableName
	 *            表格名称
	 * @return 是否删除成功
	 */
	public boolean delete(T bean, String tableName) {
		Serializable id;
		
		try {
			id = (Serializable)Reflect.executeMethod(bean, "getId");
		} catch (Throwable e) {
			throw new RuntimeException("获取 bean 实体之 id 失败！");
		}
		
		LOGGER.info("DAO 删除记录 id:{0}", id);
		
		return delete(conn, tableName, id);
	}
	
	public Class<T> getEntryType() {
		return entryType;
	}

	public void setEntryType(Class<T> entryType) {
		this.entryType = entryType;
	}
}
