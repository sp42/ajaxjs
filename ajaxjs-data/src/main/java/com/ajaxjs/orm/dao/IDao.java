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
package com.ajaxjs.orm.dao;

import java.io.Serializable;
import java.util.List;

/**
 * 数据持久层由 DAO 及其实现类组成。 数据访问对象（Data Access Object），提供数据库的增删改查服务。
 * 必须继承这个接口而不能直接实现这个接口。这个接口里面的方法都是参考用，开发者可自行制订自己的业务方法。
 * 
 * @author Sp42 frank@ajaxjs.com
 *
 * @param <T> 实体类型，可以是 POJO 或 Map，这里虽然指定了一种类型，但是可以混合着出现。
 * @param <ID> 序号类型，可以是 INTEGER/LONG/String
 */
public interface IDao<T, ID extends Serializable> {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 序号
	 * @return POJO
	 */
	public T findById(ID id);

	/**
	 * 列表查询，如果找不到则返回 null。
	 * 
	 * @return 结果集
	 */
	public List<T> findList();

	/**
	 * 支持分页的查询，如果找不到则返回 null。执行这个方法前应先查询符合条件的记录总数，即 int count()
	 * 
	 * @param start 分页之起始行数
	 * @param limit 分页之偏量值
	 * @return 结果集
	 */
	public PageResult<T> findPagedList(int start, int limit);

	/**
	 * 新建记录
	 * 
	 * @param bean POJO 对象
	 * @return 新建记录之序号
	 */
	public ID create(T bean);

	/**
	 * 修改记录
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数，理应 = 1
	 */
	public int update(T bean);

	/**
	 * 单个删除
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数
	 */
	public boolean delete(T bean);
}
