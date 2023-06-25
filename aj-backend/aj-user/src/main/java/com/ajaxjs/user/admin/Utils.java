package com.ajaxjs.user.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Utils {
    /**
     * 用户是否已登录
     *
     * @param req
     * @return true=已登录
     */
    public static boolean isLogined(HttpServletRequest req) {
        return isLogined(req.getSession());
    }

    /**
     * 用户是否已登录
     *
     * @param session
     * @return true=已登录
     */
    public static boolean isLogined(HttpSession session) {
        return session.getAttribute("USER") != null;
    }
}
