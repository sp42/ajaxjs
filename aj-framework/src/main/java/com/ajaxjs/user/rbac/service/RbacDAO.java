package com.ajaxjs.user.rbac.service;

import org.springframework.context.support.BeanDefinitionDsl.Role;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;
import com.ajaxjs.user.rbac.model.Privilege;

/**
 * 用户角色权限系统的 DAO
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public interface RbacDAO {
	static interface RoleDAO extends IDataService<Role> {

	}

	public static final RoleDAO RoleDAO = new Caller("cms", "user_role").bind(RoleDAO.class, Role.class);
	
	static interface PrivilegeDAO extends IDataService<Privilege> {
		
	}
	
	public static final PrivilegeDAO PrivilegeDAO = new Caller("cms", "user_privilege").bind(PrivilegeDAO.class, Privilege.class);
}
