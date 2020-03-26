package com.ajaxjs.user.service;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.login.UserLoginLog;

public class UserLoginLogService extends BaseService<UserLoginLog> {
	@TableName(value = "user_login_log", beanClass = UserLoginLog.class)
	public static interface UserLoginLogDao extends IBaseDao<UserLoginLog> {
		@Select(UserDao.LEFT_JOIN_USER_SELECT)
		@Override
		public PageResult<UserLoginLog> findPagedList(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT * FROM ${tableName} WHERE userId = ? ORDER BY createDate LIMIT 1")
		public UserLoginLog getLastUserLoginedInfo(long userId);
	}

	public UserLoginLogDao dao = new Repository().bind(UserLoginLogDao.class);

	{
		setUiName("用户登录日志");
		setShortName("userLoginLog");
		setDao(dao);
	}

	public PageResult<UserLoginLog> findPagedList(int start, int limit) {
		return findPagedList(start, limit,
				byAny().andThen(BaseService::betweenCreateDate).andThen(UserService.byUserId));
	}

	public List<UserLoginLog> findListByUserId(long userId) {
		return findList(by("userId", userId).andThen(BaseService::orderById_DESC).andThen(top(10)));
	}
}
