package com.ajaxjs.user;

import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.sql.orm.Repository;

/**
 * 
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class BaseUserService extends BaseService<User> {
//	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserService.class);

	public static UserDao DAO = new Repository().bind(UserDao.class);

	{
		setUiName("用户");
		setShortName("user");
		setDao(DAO);
	}

	/**
	 * 按实体 userId 查找的高阶函数
	 * 
	 * @param userId userId
	 * @return SQL 处理器
	 */
	public static Function<String, String> byUserId(long userId) {
		return by("userId", userId);
	}

	public static Function<String, String> byUserId = by("userId", long.class, "userId");
}
