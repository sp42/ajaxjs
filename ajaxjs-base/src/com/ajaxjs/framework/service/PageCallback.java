/**
 * 版权所有 2017 张鑫
 * 
 * 根据2.0版本Apache许可证("许可证")授权；
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
import java.util.List;

import com.ajaxjs.framework.dao.BaseDao;

/**
 * 适用于分页场合 Service.pageHelper
 * 
 * @author xinzhang
 *
 * @param <T>
 *            实体类型，可以是 POJO 或 Map
 * @param <DAO>
 *            数据访问对象
 */
public interface PageCallback<T, DAO extends BaseDao<T, ? extends Serializable>> {
	/**
	 * 返回总数
	 * 
	 * @param dao
	 *            数据访问对象
	 * @return 记录总数
	 */
	int getTotal(DAO dao);

	/**
	 * 返回结果
	 * 
	 * @param start
	 *            起始行数
	 * @param limit
	 *            偏量值
	 * @param dao
	 *            数据访问对象
	 * @return 结果
	 */
	List<T> getList(int start, int limit, DAO dao);
}