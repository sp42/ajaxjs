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