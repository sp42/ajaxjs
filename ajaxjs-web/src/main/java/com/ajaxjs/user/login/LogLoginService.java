package com.ajaxjs.user.login;

import java.util.List;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.UserDao;
import com.ajaxjs.user.profile.ProfileService;
import com.ajaxjs.util.ioc.Component;

@Component
public class LogLoginService extends BaseService<LogLogin> {
	@TableName(value = "user_login_log", beanClass = LogLogin.class)
	public static interface UserLoginLogDao extends IBaseDao<LogLogin> {
		@Select(UserDao.LEFT_JOIN_USER_SELECT)
		@Override
		public PageResult<LogLogin> findPagedList(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT * FROM ${tableName} WHERE userId = ? ORDER BY createDate LIMIT 1")
		public LogLogin getLastUserLoginedInfo(long userId);
	}

	public static final UserLoginLogDao DAO = new Repository().bind(UserLoginLogDao.class);

	{
		setUiName("用户登录日志");
		setShortName("userLoginLog");
		setDao(DAO);
	}

	/**
	 * 查找登录日志的列表
	 * 
	 * @param start 分页起始
	 * @param limit 分页结束
	 * @return 登录日志的列表
	 */
	@Override
	public PageResult<LogLogin> findPagedList(int start, int limit) {
		return findPagedList(start, limit, byAny().andThen(BaseService::betweenCreateDateWithE).andThen(ProfileService.byUserId));
	}

	/**
	 * 根据用户 id 查找其登录日志的列表
	 * 
	 * @param userId 用户 id
	 * @return 登录日志的列表
	 */
	public List<LogLogin> findListByUserId(long userId) {
		return findList(by("userId", userId).andThen(BaseService::orderById_DESC).andThen(top(10)));
	}

	public LogLogin getLastUserLoginedInfo(long userId) {
		return DAO.getLastUserLoginedInfo(userId);
	}
}