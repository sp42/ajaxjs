package com.ajaxjs.user;

public class LogLoginService  {
	@TableName(value = "user_login_log", beanClass = LogLogin.class)
	public static interface UserLoginLogDao extends IBaseDao<LogLogin> {

		@Select("SELECT * FROM ${tableName} WHERE userId = ? ORDER BY createDate DESC LIMIT 1")
		public LogLogin getLastUserLoginedInfo(long userId);
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