package com.ajaxjs.cms.user;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean("UserLoginLogService")
public class UserLoginLogService extends BaseService<UserLoginLog>  {
	@TableName(value = "user_login_log", beanClass = UserLoginLog.class)
	public static interface UserLoginLogDao extends IBaseDao<UserLoginLog> {
		final static String sql = "SELECT e.*, user.username AS userName FROM ${tableName} e LEFT JOIN user ON user.id = e.userId";

		@Select(sql)
		@Override
		public PageResult<UserLoginLog> findPagedList(int start, int limit);

		@Select(sql + " WHERE e.userId = ?")
		public PageResult<UserLoginLog> findUserLoginLogByUserId(long userId, int start, int limit);
	}
	
	public UserLoginLogDao dao = new Repository().bind(UserLoginLogDao.class);
	
	{
		setUiName("用户登录日志");
		setShortName("userLoginLog");
		setDao(dao);
	}

}