package com.ajaxjs.cms.user.role;

import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;

@Bean("UserRoleResourcesService")
public class ResourcesService extends BaseService<Map<String, Object>> {

	UserRolePrivilegeDao dao = new Repository().bind(UserRolePrivilegeDao.class);

	{
		setUiName("权限资源");
		setShortName("user_privilege");
		setDao(dao);
	}

	@TableName(value = "user_privilege", beanClass = Map.class)
	public static interface UserRolePrivilegeDao extends IBaseDao<Map<String, Object>> {
	}
}