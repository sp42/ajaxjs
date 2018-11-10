/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com
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
package com.ajaxjs.framework.service;

import java.io.Serializable;

import com.ajaxjs.framework.service.annotation.ValidIt;
import com.ajaxjs.framework.service.annotation.ValidObj;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.orm.dao.QueryParams;

/**
 * 业务逻辑层 业务层不绑定 DAO 泛型，目的是不强制依赖 DAO
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <T> 实体类型，可以是 Bean（POJO） 或 Map，虽然可以使用 map 但请注意 create 时会不管什么属性都传过来组装数据
 * @param <ID> ID 类型，可以是 INTEGER/LONG/String/UUID
 */
public interface IService<T, ID extends Serializable> {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 序号
	 * @return POJO 对象
	 */
	public T findById(ID id) throws ServiceException;

	/**
	 * 查询分页数据
	 * 
	 * @param params 查询参数
	 * @return 分页结果对象
	 * @throws ServiceException 业务异常
	 */
	public PageResult<T> findPagedList(QueryParams params, int start, int limit) throws ServiceException;

	/**
	 * 查询分页数据
	 * 
	 * @param start 开始行数
	 * @param limit 读取行数
	 * @return 分页结果对象
	 * @throws ServiceException 业务异常
	 */
	public PageResult<T> findPagedList(int start, int limit) throws ServiceException;

	/**
	 * 新建记录
	 * 
	 * @param bean POJO 对象
	 * @return 新建记录之序号
	 * @throws ServiceException 业务异常
	 */
	@ValidIt
	public ID create(@ValidObj T bean) throws ServiceException;

	/**
	 * 修改记录
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数，理应 = 1
	 * @throws ServiceException 业务异常
	 */
	public int update(T bean) throws ServiceException;

	/**
	 * 单个删除
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数
	 * @throws ServiceException 业务异常
	 */
	public boolean delete(T bean) throws ServiceException;

	/**
	 * 返回业务名称，可用于 UI 显示
	 * 
	 * @return 业务名称
	 */
	public String getName();

	/**
	 * 返回数据库表名，可作为通用的命名
	 * 
	 * @return 数据库表名，可作为通用的命名
	 */
	public String getTableName();

}
