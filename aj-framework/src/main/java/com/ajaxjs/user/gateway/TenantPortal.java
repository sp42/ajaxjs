package com.ajaxjs.user.gateway;

/**
 * 带租户、门户字段的实体
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface TenantPortal {
	/**
	 * 返回租户 id
	 * 
	 * @return 租户 id
	 */
	default Long getTenantId() {
		return null;
	}

	/**
	 * 返回门户 id
	 * 
	 * @return 门户 id
	 */
	default Long getPortalId() {
		return null;
	}
}
