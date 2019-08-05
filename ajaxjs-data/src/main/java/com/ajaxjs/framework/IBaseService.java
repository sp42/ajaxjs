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
package com.ajaxjs.framework;

import java.util.List;
import java.util.function.Function;

/**
 * 通用业务方法
 * 
 * @author Frank Cheung
 *
 * @param <T> 实体
 */
public interface IBaseService<T> {
	/**
	 * 查询单个记录。如果找不到则返回 null
	 * 
	 * @param id 序号
	 * @return POJO 对象
	 */
	public T findById(Long id);

	/**
	 * 查询列表数据

	 * @return 结果对象
	 * @业务异常
	 */
	public List<T> findList();

	/**
	 * 查询列表数据

	 * @return 结果对象
	 * @业务异常
	 */
	public List<T> findList(Function<String, String> sqlHandler);

	/**
	 * 查询分页数据
	 * 
	 * @param start 开始行数
	 * @param limit 读取行数
	 * @return 分页结果对象
	 * @业务异常
	 */
	public PageResult<T> findPagedList(int start, int limit);
	
	/**
	 * 
	 * @param start
	 * @param limit
	 * @param sqlHandler
	 * @return
	 */
	public PageResult<T> findPagedList(int start, int limit, Function<String, String> sqlHandler);

	/**
	 * 新建记录
	 * 
	 * @param bean POJO 对象
	 * @return 新建记录之 id 序号
	 * @业务异常
	 */
	public Long create(T bean);

	/**
	 * 修改记录
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数，理应 = 1
	 * @业务异常
	 */
	public int update(T bean);

	/**
	 * 单个删除
	 * 
	 * @param bean POJO 对象
	 * @return 影响的行数
	 * @业务异常
	 */
	public boolean delete(T bean);

	/**
	 * 返回业务名称，可用于 UI 显示
	 * 
	 * @return 业务名称
	 */
	public String getUiName();

	/**
	 * 通用的命名
	 * 
	 * @return 通用的命名
	 */
	public String getShortName();

	/**
	 * 返回数据库表名
	 * 
	 * @return 数据库表名
	 */
	public String getTableName();

	/**
	 * 获取 DAO 对象，直接在 Controller 里面使用 DAO，跳过 Service
	 * 
	 * @return DAO 对象
	 */
	public IBaseDao<T> getDao();
}
