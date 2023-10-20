package com.ajaxjs.user.service.business;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.user.common.UserConstants;
import com.ajaxjs.user.common.UserUtils;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Function;

/**
 * 用户业务
 */
@Service
public class UserBusinessService implements UserConstants {
    private static final LogHelper LOGGER = LogHelper.getLog(UserBusinessService.class);

    @Value("${user.loginIdType:1}")
    int loginIdType;

    @Autowired
    @Qualifier("passwordEncode")
    Function<String, String> passwordEncode;

    /**
     * 密码支持帐号、邮件、手机作为身份凭证
     */
    public User getUserLoginByPassword(String loginId, String password) {
        String sql = "SELECT u.* FROM user u INNER JOIN user_auth a ON a.user_id = u.id WHERE u.stat != -1 AND u.%s = ? AND a.credential = ?";

        if (UserUtils.testBCD(LoginIdType.PSW_LOGIN_EMAIL, loginIdType) && EMAIL_REG.matcher(loginId).find())
            sql = String.format(sql, "email");
        else if (UserUtils.testBCD(LoginIdType.PSW_LOGIN_PHONE, loginIdType) && PHONE_REG.matcher(loginId).find())
            sql = String.format(sql, "phone");
        else
            sql = String.format(sql, "username");

        String encodePsw = passwordEncode.apply(password);
        User user = CRUD.info(User.class, sql, loginId, encodePsw);

        if (user == null)
            throw new BusinessException("用户 " + loginId + " 登录失败，用户不存在或密码错误");

        LOGGER.info(user.getName() + " 登录成功！");

        return user;
    }

}
