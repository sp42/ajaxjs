package com.ajaxjs.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.entity.BaseEntityConstants;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.sso.common.SsoUtil;
import com.ajaxjs.user.model.LogLogin;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserAuth;
import com.ajaxjs.user.util.UserUtils;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.WebHelper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户登录业务
 *
 * @author Frank Cheung
 */
@Service
public class LoginServiceImpl implements LoginService, BaseEntityConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(LoginServiceImpl.class);

    @Value("${user.login.passWordLoginType:1}")
    private int passWordLoginType;

    @Override
    public Boolean login(String userID, String password) {
        int tenantId = SsoUtil.getTenantId();
        User user;
        userID = userID.trim();

        // 密码支持帐号、邮件、手机作为身份凭证
        if (UserUtils.isValidEmail(userID)) {
            if (!UserUtils.testBCD(Login.PSW_LOGIN_EMAIL, passWordLoginType))
                throw new SecurityException("禁止使用邮件登录");

            user = UserDao.findUserBy("email", userID, tenantId);
        } else if (UserUtils.isValidPhone(userID)) {
            if (!UserUtils.testBCD(Login.PSW_LOGIN_PHONE, passWordLoginType))
                throw new SecurityException("禁止使用手机登录");

            user = UserDao.findUserBy("phone", userID, tenantId);
        } else {
            if (!UserUtils.testBCD(Login.PSW_LOGIN_ID, passWordLoginType))
                throw new SecurityException("禁止使用用户名登录");

            // 用户名
            user = UserDao.findUserBy("username", userID, tenantId);
        }

        if (user == null || user.getId() == null || user.getId() == 0)
            throw new SecurityException("用户 " + userID + " 不存在");

        if (checkUserLogin(user, password))
            LOGGER.info(user.getName() + " 登录成功！");

        HttpServletRequest req = DiContextUtil.getRequest();

        if (req != null) {
            // 会员登录之后的动作，会保存 userId 和 userName 在 Session 中
            HttpSession session = req.getSession();
            session.setAttribute(USER_SESSION_KEY, user);
//        session.setAttribute("userGroupId", user.getRoleId());// 获取资源权限总值

//        if (user.getRoleId() == null || user.getRoleId() == 0L) {
//            // 未设置用户权限
//        } else {
////			long privilegeTotal = DAO.getPrivilegeByUserGroupId(user.getRoleId());
////			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
////			sess.setAttribute("privilegeTotal", privilegeTotal);
//        }
            saveLoginLog(user, req);
        }

        return true;
    }

    public static User findUserBy(String type, String userID, int tenantId) {
        String sql = "SELECT * FROM user WHERE " + type + " = ? AND tenant_id = ? AND stat != ?";

        return CRUD.info(User.class, sql, userID, tenantId, STATUS_DELETED);
    }

    /**
     * 检查密码是否匹配正确
     *
     * @param user     用户信息
     * @param password 输入待测试的密码
     * @return 是否匹配正确
     */
    public boolean checkUserLogin(User user, String password) {
        // 密码校验
        String sql = "SELECT * FROM user_auth WHERE loginType = 1 AND userId = ?";
        UserAuth auth = CRUD.info(UserAuth.class, sql, user.getId());

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
        plainPsw = plainPsw.trim().toLowerCase();

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

//		if (LogLoginDAO.create(userLoginLog) == null)
//			LOGGER.warning("更新会员登录日志出错");
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

    @Override
    public Boolean logout(HttpSession session) {
        session.invalidate();
        // TODO 清除 SSO 登录状态
        return true;
    }

    @Override
    public String listLog() {
        // TODO Auto-generated method stub
        return null;
    }
}