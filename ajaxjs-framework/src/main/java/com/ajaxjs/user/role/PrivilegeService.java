package com.ajaxjs.user.role;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

@Bean("UserRolePrivilegeService")
public class PrivilegeService extends BaseService<Privilege> {

	UserRolePrivilegeDao dao = new Repository().bind(UserRolePrivilegeDao.class);

	{
		setUiName("权限");
		setShortName("user_privilege");
		setDao(dao);
	}

	@TableName(value = "user_privilege", beanClass = Privilege.class)
	public static interface UserRolePrivilegeDao extends IBaseDao<Privilege> {
	}
}