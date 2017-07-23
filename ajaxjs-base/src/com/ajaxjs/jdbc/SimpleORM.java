/**
 * 版权所有 2017 Frank Cheung
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
package com.ajaxjs.jdbc;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import com.ajaxjs.jdbc.sqlbuilder.CommonSQL;
import com.ajaxjs.util.collection.Map2Pojo;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.reflect.Reflect;

/**
 * 简易的 ORM
 * @author xinzhang
 *
 * @param <T>
 */
public class SimpleORM<T> extends Helper {
	private static final LogHelper LOGGER = LogHelper.getLog(SimpleORM.class);
	
	/**
	 * 实体是 bean 还是 map？
	 */
	private Class<T> entryType;

	/**
	 * 数据库连接对象
	 */
	private Connection conn;
	
	/**
	 * 
	 * @param conn
	 *            数据库连接对象
	 * @param entryType
	 *            实体是 bean 还是 map？
	 */
	public SimpleORM(Connection conn, Class<T> entryType) {
		this.conn = conn;
		this.setEntryType(entryType);
	}

	/**
	 * 查询一个结果。记录集合转换为 Map
	 * 
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            参数列表
	 * @return Map 结果
	 */
	@SuppressWarnings("unchecked")
	public T query(String sql, Object... params) {
		Map<String, Object> map = query(conn, sql, params);
		
		// 如是 map 直接返回即可
		return entryType == Map.class ? (T) map : new Map2Pojo<T>(entryType).map2pojo(map);
	}

	/**
	 * 查询多个结果。记录集合转换为 List<Map>
	 * 
	 * @param sql
	 *            SQL 语句，可以带有 ? 的占位符
	 * @param params
	 *            参数列表
	 * @return List<Map> 结果
	 */
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

		Serializable newlyId = create(conn, sql.toString(), values);
		
		// 保存 id
		if(bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)bean;
			map.put("id", newlyId);
		}else{
			try {
				Class<?> idClz = bean.getClass().getMethod("getId").getReturnType();/* 根据 getter 推断 id 类型 */
				
				if (Long.class == idClz && newlyId instanceof Integer) {
					Reflect.executeMethod(bean, "setId", new Long((int)newlyId));
				} else {
					Reflect.executeMethod(bean, "setId", newlyId); // 直接保存
				}
			} catch (Throwable e) {
				LOGGER.warning(e);
			}
		}
		
		return newlyId;
	} 
	
	/**
	 * 修改实体
	 * 
	 * @param bean
	 *            Bean 实体
	 * @param tableName
	 *            表格名称
	 * @return 成功修改的行数，一般为 1
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
		
		if(bean instanceof Map) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>)bean;
			id = (Serializable)map.get("id");
		}else{
			try {
				id = (Serializable)Reflect.executeMethod(bean, "getId");
			} catch (Throwable e) {
				throw new RuntimeException("获取 bean 实体之 id 失败！");
			}
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
