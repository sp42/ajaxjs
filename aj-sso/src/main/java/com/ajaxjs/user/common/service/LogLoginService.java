package com.ajaxjs.user.common.service;

public class LogLoginService  {
//	@TableName(value = "user_login_log", beanClass = LogLogin.class)
//	public static interface UserLoginLogDao extends IBaseDao<LogLogin> {
//		@Select(UserDao.LEFT_JOIN_USER_SELECT)
//		@Override
//		public PageResult<LogLogin> findPagedList(int start, int limit, Function<String, String> sqlHandler);
//
//		@Select("SELECT * FROM ${tableName} WHERE userId = ? ORDER BY createDate DESC LIMIT 1")
//		public LogLogin getLastUserLoginedInfo(long userId);
//	}
//
//	public static final UserLoginLogDao DAO = new Repository().bind(UserLoginLogDao.class);
//
//	/**
//	 * 查找登录日志的列表
//	 * 
//	 * @param start 分页起始
//	 * @param limit 分页结束
//	 * @return 登录日志的列表
//	 */
//	@Override
//	public PageResult<LogLogin> findPagedList(int start, int limit) {
//		return findPagedList(start, limit, byAny().andThen(BaseService::betweenCreateDateWithE).andThen(ProfileService.byUserId));
//	}
//
//	/**
//	 * 根据用户 id 查找其登录日志的列表
//	 * 
//	 * @param userId 用户 id
//	 * @return 登录日志的列表
//	 */
//	public List<LogLogin> findListByUserId(long userId) {
//		return findList(by("userId", userId).andThen(BaseService::orderById_DESC).andThen(top(10)));
//	}
//
//	public LogLogin getLastUserLoginedInfo(long userId) {
//		return DAO.getLastUserLoginedInfo(userId);
//	}

}