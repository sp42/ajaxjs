package com.ajaxjs.framework.service;

import java.io.Serializable;

import com.ajaxjs.framework.model.PageResult;

/**
 * 业务逻辑层，围绕数据库的增删改查而设的基础业务类
 * @author xinzhang
 * 
 * @param <T>
 *            实体类型，可以是 POJO 或 Map
 * @param <ID>
 *            序号类型，可以是 INTEGER/LONG/String
 */
public interface BaseService <T, ID extends Serializable> extends IService {
	/**
	 * 查询单个记录。如果找不到则返回 null。
	 * 
	 * @param id
	 *            序号
	 * @return POJO
	 * @throws ServiceException
	 */
	public T findById(ID id) throws ServiceException;
 
	/**
	 * 查询多个记录。
	 * @param start
	 *            起始行数
	 * @param limit
	 *            偏量值
	 * @return
	 * @throws ServiceException
	 */
	public PageResult<T> find(int start, int limit) throws ServiceException;

	/**
	 * 支持分页的查询。执行这个方法之前应先查询符合条件的记录总数，即 int count(Query query)。
	 * 
	 * @param start
	 *            起始行数
	 * @param limit
	 *            偏量值
	 * @param tablename
	 *            查询的表名
	 * @param query
	 *            特定查询对象，不能为 null，否则 MyBatis 无法通过。如果无特定查询对象，可创建空的 Query 类型。
	 * @return 分页结果
	 * @throws ServiceException
	 */
	//public PageResult<T> pagedFind(int start, int limit, BaseDao<T, ID> dao) throws ServiceException;

	/**
	 * 新建记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 新建记录之 id
	 * @throws ServiceException
	 */
	public ID create(T bean) throws ServiceException;

	/**
	 * 修改记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数
	 * @throws ServiceException
	 */
	public int update(T bean) throws ServiceException;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws ServiceException
	 */
	public boolean delete(ID id) throws ServiceException;
}
