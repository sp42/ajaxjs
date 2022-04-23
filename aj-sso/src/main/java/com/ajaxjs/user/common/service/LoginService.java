package com.ajaxjs.user.common.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserAuth;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.common.model.LogLogin;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 用户登录业务
 * 
 * @author Frank Cheung
 *
 */
@Component
public class LoginService implements UserConstant, UserDAO {
	private static final LogHelper LOGGER = LogHelper.getLog(LoginService.class);

	@Value("${user.login.passWordLoginType}")
	private int passWordLoginType;

	/**
	 * 根据密码登录
	 * 
	 * @param userID
	 * @param password
	 * @param tenantId
	 * @param req
	 * @return
	 */
	public User loginByPassword(String userID, String password, int tenantId, HttpServletRequest req) {
		User user;
		userID = userID.trim();

		// 密码支持帐号、邮件、帐号作为身份凭证
		if (UserUtils.isVaildEmail(userID)) {
			if (!UserUtils.testBCD(Login.PSW_LOGIN_EMAIL, passWordLoginType))
				throw new SecurityException("禁止使用邮件登录");

			user = findByEmail(userID, tenantId);
		} else if (UserUtils.isVaildPhone(userID)) {
			if (!UserUtils.testBCD(Login.PSW_LOGIN_PHONE, passWordLoginType))
				throw new SecurityException("禁止使用手机登录");

			user = findByPhone(userID, tenantId);
		} else {
			if (!UserUtils.testBCD(Login.PSW_LOGIN_ID, passWordLoginType))
				throw new SecurityException("禁止使用用户名登录");

			// 用户名
			user = findByUsername(userID, tenantId);
		}

		if (user == null || user.getId() == null || user.getId() == 0)
			throw new SecurityException("用户 " + userID + " 不存在");

		if (checkUserLogin(user, password))
			LOGGER.info(user.getName() + " 登录成功！");

		saveLoginLog(user, req);
		afterLogin(user, req);

		return user;
	}

	/**
	 * 检查密码是否匹配正确
	 * 
	 * @param user     用户信息
	 * @param password 输入待测试的密码
	 * @return
	 */
	public boolean checkUserLogin(User user, String password) {
		// 密码校验
		UserAuth auth = findPswByUserId(user.getId());

		if (auth == null)
			throw new SecurityException("系統異常，用戶 " + user.getId() + " 沒有對應的密碼記錄");

		return isPswMatch(auth.getCredential(), password);
	}

	@Value("${user.login.password_salt}")
	private String passwordSalt;

	/**
	 * 加密密码
	 * 
	 * @param plainPsw 原始密码
	 * @return 加密后的密码
	 */
	public String encodePassword(String plainPsw) {
		plainPsw = plainPsw.trim();
		plainPsw = plainPsw.toLowerCase();

		return Digest.getSHA1(plainPsw + passwordSalt);
	}

	/**
	 * 检查密码是否匹配正确
	 * 
	 * @param passwordInDB 数据库中的密码，是加密后的字符串
	 * @param password     输入待测试的密码
	 * @return 密码校验的结果
	 */
	public boolean isPswMatch(String passwordInDB, String password) {
		passwordInDB = passwordInDB.trim();
		password = encodePassword(password);

		if (!passwordInDB.equalsIgnoreCase(password)) {
			LOGGER.info("密码不正确，数据库密码：{0}, 提交密码 {1}", passwordInDB, password);

			throw new SecurityException("密码不正确");
		}

		return true;
	}

	/**
	 * 用户登录日志
	 * 
	 * @param user
	 * @param req
	 */
	private static void saveLoginLog(User user, HttpServletRequest req) {
		LogLogin userLoginLog = new LogLogin();
		userLoginLog.setUserId(user.getId());
		userLoginLog.setLoginType(Login.LoginType.PASSWORD);
		initBean(userLoginLog, req);

		if (LogLoginDAO.create(userLoginLog) == null)
			LOGGER.warning("更新会员登录日志出错");
	}

	/**
	 * 会员登录之后的动作，会保存 userId 和 userName 在 Session中
	 * 
	 * @param user 用户
	 * @param req  请求对象
	 */
	private static void afterLogin(User user, HttpServletRequest req) {
		if (req == null)
			return;

		HttpSession session = req.getSession();
		session.setAttribute(USER_SESSION_KEY, user);
		session.setAttribute("userGroupId", user.getRoleId());// 获取资源权限总值

		if (user.getRoleId() == null || user.getRoleId() == 0L) {
			// 未设置用户权限
		} else {
//			long privilegeTotal = DAO.getPrivilegeByUserGroupId(user.getRoleId());
//			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
//			sess.setAttribute("privilegeTotal", privilegeTotal);
		}
	}

	/**
	 * 获取客户端有关信息
	 * 
	 * @param bean
	 * @param req
	 */
	public static void initBean(LogLogin bean, HttpServletRequest req) {
		if (req == null)
			return;

		String ip = WebHelper.getIp(req);

		if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
			ip = "localhost";
			bean.setIpLocation("本机");
		} else {
			try {
				// ip 查询
				Map<String, Object> map = Get.api("http://ip-api.com/json/" + ip + "?lang=zh-CN");

				if (!map.containsKey(Get.ERR_MSG)) {
					String location = map.get("country") + " " + map.get("regionName");
					bean.setIpLocation(location);
				} else
					throw new Exception("接口返回不成功 " + map.get(Get.ERR_MSG));
			} catch (Exception e) {
				System.err.println(e);
			}
		}

		bean.setIp(ip);
		bean.setUserAgent(req.getHeader("user-agent"));
	}
}
