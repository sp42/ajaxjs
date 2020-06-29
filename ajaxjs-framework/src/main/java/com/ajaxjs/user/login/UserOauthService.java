package com.ajaxjs.user.login;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

@Bean
public class UserOauthService extends BaseService<UserOauth> {
	
	@TableName(value = "user_oauth", beanClass = UserOauth.class)
	public static interface UserOauthDao extends IBaseDao<UserOauth> {
	}
	
	UserOauthDao dao = new Repository().bind(UserOauthDao.class);

	{
		setUiName("用户三方登录");
		setShortName("userOauth");
		setDao(dao);
	}
}