package com.ajaxjs.user.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserUtils {
    /**
     * 用户是否已登录
     *
     * @return true=已登录
     */
    public static boolean isLogin(HttpServletRequest req) {
        return isLogin(req.getSession());
    }

    /**
     * 用户是否已登录
     *
     * @return true=已登录
     */
    public static boolean isLogin(HttpSession session) {
        return session.getAttribute("USER") != null;
    }

    /**
     * 测试 8421 码是否包含 v
     *
     * @param v   当前权限值
     * @param all 同值
     * @return true=已包含
     */
    public static boolean testBCD(int v, int all) {
        return (v & all) == v;
    }
}
