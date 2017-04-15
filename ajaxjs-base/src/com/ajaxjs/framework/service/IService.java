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
package com.ajaxjs.framework.service;

import java.io.Serializable;

import com.ajaxjs.framework.dao.QueryParams;
import com.ajaxjs.framework.model.ModelAndView;
import com.ajaxjs.framework.model.PageResult;
import com.ajaxjs.framework.service.annotation.ValidIt;
import com.ajaxjs.framework.service.annotation.ValidObj;

/**
 * 业务层不绑定 DAO 泛型，目的是不强制依赖 DAO
 * @author xinzhang
 *
 * @param <T>
 *            实体类型
 * @param <ID>
 *            ID 类型，可以是 INTEGER/LONG/String
 */
public interface IService<T, ID extends Serializable> {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id
	 *            序号
	 * @return POJO
	 */
	public T findById(ID id) throws ServiceException;

	/**
	 * 新建记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 新建记录之序号
	 */
	@ValidIt
	public ID create(@ValidObj T bean) throws ServiceException;

	/**
	 * 修改记录
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数，理应 = 1
	 */
	public int update(T bean) throws ServiceException;

	/**
	 * 单个删除
	 * 
	 * @param bean
	 *            POJO 对象
	 * @return 影响的行数
	 */
	public boolean delete(T bean) throws ServiceException;

	/**
	 * 
	 * @param parame
	 * @return
	 * @throws ServiceException
	 */
	public PageResult<T> findPagedList(QueryParams parame) throws ServiceException;

	/**
	 * 返回业务名称，可用于 UI 显示
	 * 
	 * @return 业务名称
	 */
	String getName();

	/**
	 * 模版方法，用于装备其他数据，如分类这些外联的表。
	 * 
	 * @param model
	 *            模型
	 */
	public void prepareData(ModelAndView model);
}
