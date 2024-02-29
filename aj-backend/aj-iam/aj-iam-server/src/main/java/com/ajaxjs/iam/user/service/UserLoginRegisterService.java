package com.ajaxjs.iam.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.framework.spring.filter.google_captcha.GoogleCaptchaCheck;
import com.ajaxjs.iam.server.common.IamUtils;
import com.ajaxjs.iam.user.common.UserConstants;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.common.util.CheckStrength;
import com.ajaxjs.iam.user.controller.UserLoginRegisterController;
import com.ajaxjs.iam.user.model.User;
import com.ajaxjs.iam.user.model.UserAccount;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class UserLoginRegisterService implements UserLoginRegisterController, UserConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(UserLoginRegisterService.class);

    @Override
    public Boolean isLogin() {
        return userSession.getUserFromSession() != null;
    }

    @Autowired
    UserSession userSession;

    @Autowired
    LogLoginService logLoginService;

    @Override
    @GoogleCaptchaCheck
    public boolean login(String loginId, String password, String returnUrl, HttpServletRequest req, HttpServletResponse resp) {
        loginId = loginId.trim();

        User user = getUserLoginByPassword(loginId, password);

        // 会员登录之后的动作，会保存 userId 和 userName 在 Session 中
        userSession.put(UserSession.SESSION_KEY + user.getId() + "-" + StrUtil.getRandomString(4), user); // 同一个用户多端登录，加随机码区分
//        session.setAttribute("userGroupId", user.getRoleId());// 获取资源权限总值

//        if (user.getRoleId() == null || user.getRoleId() == 0L) {
//            // 未设置用户权限
//        } else {
////			long privilegeTotal = DAO.getPrivilegeByUserGroupId(user.getRoleId());
////			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
////			sess.setAttribute("privilegeTotal", privilegeTotal);
//        }
        logLoginService.saveLoginLog(user, req);

        if (StringUtils.hasText(returnUrl))
            IamUtils.send303Redirect(resp, returnUrl);

        return true;
    }

    @Value("${user.loginIdType:1}")
    int loginIdType;

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    /**
     * 密码支持帐号、邮件、手机作为身份凭证
     */
    public User getUserLoginByPassword(String loginId, String password) {
        String sql = "SELECT u.* FROM user u INNER JOIN user_account a ON a.user_id = u.id WHERE u.stat != 1 AND u.%s = ? AND a.password = ?";

        if (UserUtils.testBCD(LoginIdType.PSW_LOGIN_EMAIL, loginIdType) && UserUtils.isValidEmail(loginId))
            sql = String.format(sql, "email");
        else if (UserUtils.testBCD(LoginIdType.PSW_LOGIN_PHONE, loginIdType) && UserUtils.isValidPhone(loginId))
            sql = String.format(sql, "phone");
        else
            sql = String.format(sql, "login_id");

        String encodePsw = passwordEncode.apply(password);
        User user = CRUD.info(User.class, sql, loginId, encodePsw);

        if (user == null)
            throw new BusinessException("用户 " + loginId + " 登录失败，用户不存在或密码错误");

        LOGGER.info(user.getName() + " 登录成功！");

        return user;
    }

    @Override
    public ModelAndView logout(String returnUrl) {
        return null;
    }

    @Override
    public Boolean register(Map<String, Object> params) {
        // 所有字符串 trim 一下
        for (String key : params.keySet()) {
            Object obj = params.get(key);

            if (obj instanceof String)
                params.put(key, obj.toString().trim());
        }

        // 校验
        if (isNull(params, "tenantId"))
            throw new IllegalArgumentException("租户 id 不能为空");

        if (isNull(params, "password"))
            throw new IllegalArgumentException("注册密码不能为空");

        boolean hasNoUsername = isNull(params, "username"), hasNoEmail = isNull(params, "email"), hasNoPhone = isNull(params, "phone");
        if (hasNoUsername && hasNoEmail && hasNoPhone)
            throw new IllegalArgumentException("没有用户标识， username/email/phone 至少填一种");

        int tenantId = Integer.parseInt(params.get("tenantId").toString());

        // 是否重复
        if (!hasNoUsername && isRepeat("username", params.get("username").toString(), tenantId))
            throw new IllegalArgumentException("用户名 username: " + params.get("username").toString() + " 重复");

        if (!hasNoEmail && isRepeat("email", params.get("email").toString(), tenantId))
            throw new IllegalArgumentException("邮箱: " + params.get("email").toString() + " 重复");

        if (!hasNoPhone && isRepeat("phone", params.get("phone").toString(), tenantId))
            throw new IllegalArgumentException("手机: " + params.get("phone").toString() + " 重复");

        // 有些字段不要
        String psw = params.get("password").toString();
        params.remove("password");

        // 检测密码强度
        CheckStrength.LEVEL passwordLevel = CheckStrength.getPasswordLevel(psw);

        if (passwordLevel == CheckStrength.LEVEL.EASY)
            throw new UnsupportedOperationException("密码强度太低");

        long userId = CRUD.create(params); // 写入数据库
        UserAccount auth = new UserAccount();
        auth.setUserId(userId);
        auth.setPassword(passwordEncode.apply(psw));
        auth.setRegisterType(LoginType.PASSWORD);
        auth.setRegisterIp(WebHelper.getIp(Objects.requireNonNull(DiContextUtil.getRequest())));

        return true;
    }

    private static boolean isNull(Map<String, Object> params, String key) {
        return !params.containsKey(key) || !StringUtils.hasText(params.get(key).toString());
    }

    @Override
    public Boolean checkRepeat(String field, String value) {
        return isRepeat(field, value, TenantService.getTenantId());
    }

    /**
     * 检查某个值是否已经存在一样的值
     *
     * @param field 数据库里面的字段名称
     * @param value 欲检查的值
     * @return true=值重复
     */
    public static boolean isRepeat(String field, String value, int tenantId) {
        String sql = "SELECT id FROM user WHERE stat != 1 AND %s = ? AND tenantId = ? LIMIT 1";
        sql = String.format(sql, field.trim());

        return CRUD.queryOne(Long.class, sql, value.trim(), tenantId) != null; // 有这个数据表示重复
    }
}
