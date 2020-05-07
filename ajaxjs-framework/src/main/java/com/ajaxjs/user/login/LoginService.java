package com.ajaxjs.user.login;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.UserUtil;
import com.ajaxjs.user.controller.LoginLogController;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;

public class LoginService extends UserService {
	private static final LogHelper LOGGER = LogHelper.getLog(LoginService.class);

	public static User loginByPassword(String userID, String password) throws ServiceException {
		User user;

		int passWordLoginType = ConfigService.getValueAsInt("user.login.passWordLoginType");
		// 密码支持帐号、邮件、帐号作为身份凭证
		if (UserUtil.isVaildEmail(userID)) {
			if (!UserUtil.testBCD(UserDict.PSW_LOGIN_EMAIL, passWordLoginType))
				throw new ServiceException("禁止使用邮件登录");

			user = dao.findByEmail(userID);
		} else if (UserUtil.isVaildPhone(userID)) {
			if (!UserUtil.testBCD(UserDict.PSW_LOGIN_PHONE, passWordLoginType))
				throw new ServiceException("禁止使用手机登录");

			user = dao.findByPhone(userID);
		} else {
			if (!UserUtil.testBCD(UserDict.PSW_LOGIN_ID, passWordLoginType))
				throw new ServiceException("禁止使用用户名登录");

			// 用户名
			user = dao.findByUserName(userID);
		}

		if (user == null || user.getId() == null || user.getId() == 0)
			throw new ServiceException("用户 " + userID + " 不存在");

		if (checkUserLogin(user, password))
			LOGGER.info(user.getName() + " 登录成功！");

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
	public static void saveLoginLog(User user, HttpServletRequest req) {
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setUserId(user.getId());
		userLoginLog.setLoginType(UserDict.PASSWORD);
		LoginLogController.initBean(userLoginLog, req);

		if (LoginLogController.service.create(userLoginLog) <= 0)
			LOGGER.warning("更新会员登录日志出错");
	}

	/**
	 * 会员登录之后的动作，会保存 userId 和 userName 在 Session中
	 * 
	 * @param user 		用户
	 * @param request 	请求对象
	 */
	public static void afterLogin(User user, HttpServletRequest request) {
		if (request == null)
			return;

		HttpSession sess = request.getSession();

		sess.setAttribute("userId", user.getId());
		sess.setAttribute("userUid", user.getUid());
		sess.setAttribute("userName", user.getName());
		sess.setAttribute("userPhone", user.getPhone());
		sess.setAttribute("userAvatar", user.getAvatar() == null ? null : (ConfigService.getValueAsString("uploadFile.imgPerfix") + "/" + user.getAvatar()));

		// 获取资源权限总值
		sess.setAttribute("userGroupId", user.getRoleId());

		if (user.getRoleId() == null || user.getRoleId() == 0L) {
			// 未设置用户权限
		} else {
			long privilegeTotal = dao.getPrivilegeByUserGroupId(user.getRoleId());
			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
			sess.setAttribute("privilegeTotal", privilegeTotal);
		}

//		Attachment_picture avatar = dao.findAvaterByUserId(user.getUid());
//
//		if (avatar != null)
	}

	/**
	 * 设置用户显示可选择的帐号
	 * 
	 * @param mv
	 */
	public static void setUserId(ModelAndView mv) {
		List<String> userID = new ArrayList<>();

		int passWordLoginType = ConfigService.getValueAsInt("user.login.passWordLoginType");

		if (UserUtil.testBCD(UserDict.PSW_LOGIN_ID, passWordLoginType))
			userID.add("用户名");
		if (UserUtil.testBCD(UserDict.PSW_LOGIN_EMAIL, passWordLoginType))
			userID.add("邮件");
		if (UserUtil.testBCD(UserDict.PSW_LOGIN_PHONE, passWordLoginType))
			userID.add("手机");

		mv.put("userID", String.join("/", userID));
	}
}
