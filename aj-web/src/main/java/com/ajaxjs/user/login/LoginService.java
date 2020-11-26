package com.ajaxjs.user.login;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.user.BaseUserService;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.UserHelper;
import com.ajaxjs.user.password.UserCommonAuth;
import com.ajaxjs.user.password.UserCommonAuthService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;

@Component
public class LoginService extends BaseUserService {
	private static final LogHelper LOGGER = LogHelper.getLog(LoginService.class);
	
	@Resource
	private LogLoginService logLoginService;

	public User loginByPassword(String userID, String password, HttpServletRequest req) throws ServiceException {
		User user;
		userID = userID.trim();
		int passWordLoginType = ConfigService.getValueAsInt("user.login.passWordLoginType");

		// 密码支持帐号、邮件、帐号作为身份凭证
		if (UserHelper.isVaildEmail(userID)) {
			if (!UserHelper.testBCD(UserConstant.PSW_LOGIN_EMAIL, passWordLoginType))
				throw new ServiceException("禁止使用邮件登录");

			user = DAO.findByEmail(userID);
		} else if (UserHelper.isVaildPhone(userID)) {
			if (!UserHelper.testBCD(UserConstant.PSW_LOGIN_PHONE, passWordLoginType))
				throw new ServiceException("禁止使用手机登录");

			user = DAO.findByPhone(userID);
		} else {
			if (!UserHelper.testBCD(UserConstant.PSW_LOGIN_ID, passWordLoginType))
				throw new ServiceException("禁止使用用户名登录");

			// 用户名
			user = DAO.findByUserName(userID);
		}

		if (user == null || user.getId() == null || user.getId() == 0)
			throw new ServiceException("用户 " + userID + " 不存在");

		if (checkUserLogin(user, password))
			LOGGER.info(user.getName() + " 登录成功！");

		saveLoginLog(user, req);
		afterLogin(user, req);

		return user;
	}

	public static boolean checkUserLogin(User user, String password) throws ServiceException {
		// 密码校验
		UserCommonAuth auth = UserCommonAuthService.dao.findByUserId(user.getId());

		if (auth == null)
			throw new ServiceException("系統異常，用戶 " + user.getId() + " 沒有對應的密碼記錄");

		return checkUserLogin(auth.getPassword(), password);
	}

	public static boolean checkUserLogin(String passwordInDB, String password) throws ServiceException {
		passwordInDB = passwordInDB.trim();
		password = UserCommonAuthService.encode(password.trim());

		if (!passwordInDB.equalsIgnoreCase(password)) {
			LOGGER.info("密码不正确，数据库密码：{0}, 提交密码 {1}", passwordInDB, password);

			throw new ServiceException("密码不正确");
		}

		return true;
	}

	/**
	 * 用户登录日志
	 * 
	 * @param user
	 * @param req
	 */
	private void saveLoginLog(User user, HttpServletRequest req) {
		LogLogin userLoginLog = new LogLogin();
		userLoginLog.setUserId(user.getId());
		userLoginLog.setLoginType(UserConstant.PASSWORD);
		LogLoginController.initBean(userLoginLog, req);

		if (logLoginService.create(userLoginLog) <= 0)
			LOGGER.warning("更新会员登录日志出错");
	}

	/**
	 * 会员登录之后的动作，会保存 userId 和 userName 在 Session中
	 * 
	 * @param user    用户
	 * @param request 请求对象
	 */
	private static void afterLogin(User user, HttpServletRequest request) {
		if (request == null)
			return;

		HttpSession sess = request.getSession();

		sess.setAttribute("userId", user.getId());
		sess.setAttribute("userUid", user.getUid());
		sess.setAttribute("userName", user.getName());
		sess.setAttribute("userPhone", user.getPhone());
		sess.setAttribute("userAvatar",
				user.getAvatar() == null ? null : (ConfigService.get("uploadFile.imgPerfix") + user.getAvatar()));

		// 获取资源权限总值
		sess.setAttribute("userGroupId", user.getRoleId());

		if (user.getRoleId() == null || user.getRoleId() == 0L) {
			// 未设置用户权限
		} else {
			long privilegeTotal = DAO.getPrivilegeByUserGroupId(user.getRoleId());
			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
			sess.setAttribute("privilegeTotal", privilegeTotal);
		}

//		Attachment_picture avatar = dao.findAvaterByUserId(user.getUid());
//
//		if (avatar != null)
	}
}
