package com.ajaxjs.data_service.sdk;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.PageResult;

/**
 * DAO 基类接口。可指定泛型
 * <p>
 * 子接口继承该接口后，不需要写实现
 * 
 * @author Frank Cheung
 *
 * @param <T> 记录的实体类型
 */
public abstract interface IDataService<T> extends IDataServiceBase {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 *
	 * @param <T> 记录的实体类型
	 * @param id  记录 id
	 * @return 单个记录的 Bean
	 */
	T findById(Serializable id);

	/**
	 * 
	 * @return
	 */
	T findOne();

	/**
	 * 查询列表数据
	 *
	 * @param <T> 记录的实体类型
	 * @param clz Bean 的类型
	 * @return Bean 格式的列表结果
	 */
	List<T> findList();

	/**
	 * 查询分页数据
	 *
	 * @param <T>   记录的实体类型
	 * @param start 开始行数
	 * @param limit 读取行数
	 * @param clz   Bean 的类型
	 * @return 分页结果对象
	 */
	PageResult<T> findPagedList(int start, int limit);

	/**
	 * 新建记录
	 *
	 * @param bean Bean 对象
	 * @return 新建记录之 id 序号
	 */
	Serializable create(T bean);

	/**
	 * 修改记录
	 *
	 * @param bean Bean 对象，应包含 id 字段
	 * @return 影响的行数，理应 = 1
	 */
	boolean update(T bean);

	@Override
	IDataService<T> setQuery(Map<String, Object> queryParams);

	@Override
	IDataService<T> setWhereQuery(Map<String, Object> queryWhereParams);

	@Override
	IDataService<T> setWhereQuery(String where);

	@Override
	IDataService<T> setWhereQuery(String fieldName, Object value);
}
