package com.ajaxjs.user.register;

import java.util.Map;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcReader;
import com.ajaxjs.user.BaseUserService;
import com.ajaxjs.user.User;
import com.ajaxjs.user.password.UserCommonAuth;
import com.ajaxjs.user.password.UserCommonAuthService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 用户注册业务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class RegisterService extends BaseUserService {
	private static final LogHelper LOGGER = LogHelper.getLog(RegisterService.class);

	@Resource("UserCommonAuthService")
	private UserCommonAuthService passwordService;

	/**
	 * 普通口令注册 ˙
	 * 
	 * @param user   用户对象
	 * @param params 密码对象
	 * @return 新注册用户
	 * @throws ServiceException
	 */
	public User registerByPsw(User user, Map<String, Object> params) throws ServiceException {
		LOGGER.info("用户普通口令注册");

		String password = (String) params.get("password");

		if (CommonUtil.isEmptyString(password))
			throw new ServiceException("注册密码不能为空");

		checkIfRepeated(user);

		Long userId = create(user); // 写入数据库
		user.setId(userId);

		UserCommonAuth passwordModel = new UserCommonAuth(); // 保存密码
		passwordModel.setPassword(password);
		passwordModel.setUserId(userId);

		passwordService.create(passwordModel);

		return user;
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param user 用户对象
	 * @throws ServiceException
	 */
	private static void checkIfRepeated(User user) throws ServiceException {
		if (user.getPhone() != null) {
			user.setPhone(user.getPhone().trim()); // 消除空格
			checkIfRepeated("phone", user.getPhone(), "手机号码");
		}

		if (user.getName() != null) {
			user.setName(user.getName().trim());
			checkIfRepeated("name", user.getName(), "用户名");
		}

		if (user.getEmail() != null) {
			user.setEmail(user.getEmail().trim());
			checkIfRepeated("email", user.getEmail(), "邮箱");
		}
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field 数据库里面的字段名称
	 * @param value 欲检查的值
	 * @param type  提示的类型
	 * @return true=值重复
	 * @throws ServiceException
	 */
	public static boolean checkIfRepeated(String field, String value, String type) throws ServiceException {
		if (value != null) {
			value = value.trim();
			String sql = "SELECT * FROM user WHERE " + field + " = ? LIMIT 1";

			if (JdbcReader.queryOne(JdbcConnection.getConnection(), sql, Object.class, value) == null)
				return true;
			else
				throw new ServiceException(type + " " + value + " 已注册");
		}

		return false;
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field 数据库里面的字段名称
	 * @param value 欲检查的值
	 * @return true=值重复
	 */
	private static boolean checkIfRepeated(String field, String value) {
		value = value.trim();

		return value != null && JdbcReader.queryOne(JdbcConnection.getConnection(),
				"SELECT * FROM user WHERE " + field + " = ? LIMIT 1", Object.class, value) != null;
	}

	public boolean checkIfRepeated_(String name, String email, String phone) {
		boolean checkIfRepeated;

		if (name != null)
			checkIfRepeated = checkIfRepeated("name", name);
		else if (email != null)
			checkIfRepeated = checkIfRepeated("email", email);
		else if (phone != null)
			checkIfRepeated = checkIfRepeated("phone", phone);
		else
			checkIfRepeated = true;
		
		return checkIfRepeated;
	}
}
