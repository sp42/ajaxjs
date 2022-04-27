package com.ajaxjs.data_service.sdk;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.PageResult;

/**
 * DAO 基类接口
 * <p>
 * 子接口继承该接口后，不需要写实现
 *
 * @author Frank Cheung
 */
public abstract interface IDataServiceBase {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 *
	 * @param id 记录 id
	 * @return 单个记录的 Bean
	 */
	Map<String, Object> findByIdAsMap(Serializable id);

	/**
	 * 查询列表数据
	 *
	 * @return Map 格式的列表结果
	 */
	List<Map<String, Object>> findListAsListMap();

	/**
	 * 查询分页数据
	 *
	 * @param start 开始行数
	 * @param limit 读取行数
	 * @return 分页结果对象
	 */
	PageResult<Map<String, Object>> findPagedListAsMap(int start, int limit);

	/**
	 * 新建记录
	 *
	 * @param map Map 实体
	 * @return 新建记录之 id 序号
	 */
	Serializable create(Map<String, Object> map);

	/**
	 * 修改记录
	 *
	 * @param map Map 实，应包含 id 字段
	 * @return 影响的行数，理应 = 1
	 */
	Boolean update(Map<String, Object> map);

	/**
	 * 单个删除
	 *
	 * @param beanOrMapOrId
	 * @return 是否删除成功
	 */
	Boolean delete(Object beanOrMapOrId);

	/**
	 * 设置 SQL 的查询条件参数。这是针对 MyBatis 插值的用的
	 * 
	 * @param queryParams
	 * @return
	 */
	IDataServiceBase setQuery(Map<String, Object> queryParams);

	/**
	 * 设置 SQL WHERE 的查询条件参数。 不用设置 Map，固定 where 参数
	 * 
	 * @param queryWhereParams 完整的 WHERE 语句部分，不包括 WHERE 关键字
	 * @return
	 */
	IDataServiceBase setWhereQuery(Map<String, Object> queryWhereParams);

	/**
	 * 设置 SQL WHERE 的查询条件参数。 不用设置 Map，固定 where 参数
	 * 
	 * @param where 完整的 WHERE 语句部分，不包括 WHERE 关键字
	 * @return
	 */
	IDataServiceBase setWhereQuery(String where);

	/**
	 * 设置 SQL WHERE 的查询条件参数。 不用设置 Map，固定 where 参数
	 * 
	 * @param fieldName 查询的字段名
	 * @param value     查询的值
	 * @return
	 */
	IDataServiceBase setWhereQuery(String fieldName, Object value);
}
