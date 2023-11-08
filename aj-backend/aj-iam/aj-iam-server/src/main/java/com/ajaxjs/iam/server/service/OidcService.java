package com.ajaxjs.iam.server.service;

import com.ajaxjs.iam.server.common.IamConstants;
import com.ajaxjs.iam.server.common.IamUtils;
import com.ajaxjs.iam.server.controller.OidcController;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class OidcService implements OidcController, IamConstants {
    private static final String NOT_LOGIN_TEXT = "<meta http-equiv=\"refresh\" content=\"2;url=%s\" /> 用户尚未登录，两秒后跳转到登录页面……";

    @Override
    public void authorization(HttpServletRequest req, HttpServletResponse resp) {
        Object userObj = req.getSession().getAttribute(USER_IN_SESSION);

        if (userObj == null) { // 未登录
            // 返回一段 HTML
            String html = String.format(NOT_LOGIN_TEXT, "../login?" + req.getQueryString());
            IamUtils.responseHTML(resp, html);
        } else {// 已登录
//            IamUtils.send303Redirect(resp, "../login?" + req.getQueryString());

        }
    }
}
