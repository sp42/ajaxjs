package com.ajaxjs.framework.teant;

import java.util.List;

import com.ajaxjs.framework.PageResult;

/**
 * 带租户隔离的实体，即有 tenantId 字段
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface ITenantService<T> {
	/**
	 * 
	 * @param teantId
	 * @return
	 */
	List<T> findByTenantId(long teantId);

	PageResult<T> findPageByTenantId(long teantId);
}
