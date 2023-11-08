package com.ajaxjs.iam.user.service;

import com.ajaxjs.data.CRUD;
import com.ajaxjs.framework.BusinessException;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.iam.user.common.UserConstants;
import com.ajaxjs.iam.user.common.UserUtils;
import com.ajaxjs.iam.user.common.session.UserSession;
import com.ajaxjs.iam.user.controller.UserController;
import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import com.ajaxjs.iam.user.model.User;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Function;

@Service
public class UserService implements UserController ,UserConstants{
    private static final LogHelper LOGGER = LogHelper.getLog(UserService.class);

    @Override
    public Boolean isLogin() {
        return null;
    }

    @Autowired
    UserSession userSession;

    @Autowired
    LogLoginService logLoginService;

    @Override
    public ModelAndView login(String loginId, String password, String returnUrl) {
        loginId = loginId.trim();

        User user = getUserLoginByPassword(loginId, password);
        HttpServletRequest req = DiContextUtil.getRequest();

        // 会员登录之后的动作，会保存 userId 和 userName 在 Session 中
        assert req != null;
//        HttpSession session = req.getSession();
        userSession.put(UserSession.SESSION_KEY + user.getId(), user);
//        session.setAttribute("userGroupId", user.getRoleId());// 获取资源权限总值

//        if (user.getRoleId() == null || user.getRoleId() == 0L) {
//            // 未设置用户权限
//        } else {
////			long privilegeTotal = DAO.getPrivilegeByUserGroupId(user.getRoleId());
////			LOGGER.info("获取用户权限 privilegeTotal:" + privilegeTotal);
////			sess.setAttribute("privilegeTotal", privilegeTotal);
//        }
        logLoginService.saveLoginLog(user, req);

        return null;
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

    @Override
    public ModelAndView logout(String returnUrl) {
        return null;
    }
}
