package com.ajaxjs.user.service;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.user.common.session.UserSession;
import com.ajaxjs.user.controller.OAuthUserController;
import com.ajaxjs.user.model.AccessToken;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.business.UserBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Service
public class OAuthUserService implements OAuthUserController {
    @Autowired
    UserBusinessService userBusinessService;

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

        User user = userBusinessService.getUserLoginByPassword(loginId, password);
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

    @Override
    public ModelAndView logout(String returnUrl) {
        return null;
    }

    @Override
    public AccessToken appLogin(String clientId, String clientSecret) {
        return null;
    }

    @Override
    public AccessToken appLogin(String clientId) {
        return null;
    }
}
