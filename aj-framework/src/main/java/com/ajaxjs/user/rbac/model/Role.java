package com.ajaxjs.user.rbac.model;

import com.ajaxjs.framework.BaseModel;

/**
 * 用户角色
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class Role extends BaseModel {
	/**
	 * 父 id
	 */
	private Long parentId;

	/**
	 * 权限值
	 */
	private Long accessKey;

	/**
	 * 租户 id
	 */
	private Long tenantId;

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(Long accessKey) {
		this.accessKey = accessKey;
	}

	public Long getTenantId() {
		return tenantId;
	}

	public void setTenantId(Long tenantId) {
		this.tenantId = tenantId;
	}
}
