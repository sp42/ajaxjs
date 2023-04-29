package com.ajaxjs.user;

import java.util.Map;

import com.ajaxjs.data_service.sdk.Caller;
import com.ajaxjs.data_service.sdk.IDataService;

/**
 * 用户常见的一些 DAO 方法
 * 
 * @author Frank Cheung
 *
 */
public interface UserCommonDAO {
	static interface UserDAO extends IDataService<User> {
		/**
		 * 带密码的用户查询，因为跨表的关系，返回 Map
		 *
		 * @return
		 */
		Map<String, Object> findOneWithAuth();

		User findUserByOauthId(String password, int loginType);
	}

	static final UserDAO UserDAO = new Caller("cms", "user").bind(UserDAO.class, User.class);

	/**
	 * 根据登录账号查找用户
	 *
	 * @param username
	 * @param tenantId
	 * @return 返回用户，若 null 表示找不到用户
	 */
	default User findByUsername(String username, int tenantId) {
		String whereSql = String.format(" username = '%s' AND tenantId = %s ", username.trim(), tenantId);

		return UserDAO.setWhereQuery(whereSql).findOne();
	}

	/**
	 * 根据邮件查找用户
	 *
	 * @param email
	 * @param tenantId
	 * @return 返回用户，若 null 表示找不到用户
	 */
	default User findByEmail(String email, int tenantId) {
		String whereSql = String.format(" email = '%s' AND tenantId = %s ", email.trim(), tenantId);

		return UserDAO.setWhereQuery(whereSql).findOne();
	}

	/**
	 * 根据手机查找用户
	 *
	 * @param phone
	 * @param tenantId
	 * @return 返回用户，若 null 表示找不到用户
	 */
	default User findByPhone(String phone, int tenantId) {
		String whereSql = String.format(" phone = '%s' AND tenantId = %s ", phone.trim(), tenantId);

		return UserDAO.setWhereQuery(whereSql).findOne();
	}

	static interface UserAuthDAO extends IDataService<UserAuth> {
	}

	static final UserAuthDAO UserAuthDAO = new Caller("cms", "user_auth").bind(UserAuthDAO.class, UserAuth.class);
}
