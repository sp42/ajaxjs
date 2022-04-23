package com.ajaxjs.user.rbac.model;

import com.ajaxjs.user.rbac.RbacConstant;

/**
 * 基础资料列表
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface Resources {
	public final static Resource ARTICLE = new Resource("图文系统", RbacConstant.RESOURCES_GROUP.COMMON);

	public final static Resource FEEDBACK = new Resource("留言系统", RbacConstant.RESOURCES_GROUP.COMMON);
}
