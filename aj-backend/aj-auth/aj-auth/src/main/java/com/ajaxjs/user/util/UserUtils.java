package com.ajaxjs.user.util;

import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserConstants;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户工具类
 *
 * @author Frank Cheung
 */
public class UserUtils {
    /**
     * 验证 email 是否合法正确
     */
    private final static String EMAIL_REGEXP = "^([a-z0-9A-Z]+[-|.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public final static Pattern EMAIL_REG = Pattern.compile(EMAIL_REGEXP);

    /**
     * 是否合法的邮件
     *
     * @param email 待测试的邮件地址
     * @return true 表示为合法邮件
     */
    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEXP);
    }

    /**
     * 验证手机号码是否合法正确
     */
    private static final String PHONE_REGEXP = "^[1][3-8]\\d{9}$";

    public final static Pattern PHONE_REG = Pattern.compile(PHONE_REGEXP);

    /**
     * 是否合法的手机号码，仅限中国大陆号码
     *
     * @param phoneNo 待测试的手机号码
     * @return true 表示为手机号码
     */
    public static boolean isValidPhone(String phoneNo) {
        return phoneNo.matches(PHONE_REGEXP);
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

    public static User getLoginedUser(HttpServletRequest req) {
        return getLoginedUser(req.getSession());
    }

    /**
     * 获取用户
     *
     * @param session
     * @return
     */
    public static User getLoginedUser(HttpSession session) {
        Object obj = session.getAttribute(UserConstants.USER_SESSION_KEY);

        if (obj == null)
            throw new IllegalAccessError("用户未登录，非法访问");

        if (!(obj instanceof User))
            throw new IllegalStateException("用户不是 User 类型");

        return (User) obj;
    }

    /**
     * 用户是否已登录
     *
     * @param req
     * @return true=已登录
     */
    public static boolean isLogin(HttpServletRequest req) {
        return isLogin(req.getSession());
    }

    /**
     * 用户是否已登录
     *
     * @param session
     * @return true=已登录
     */
    public static boolean isLogin(HttpSession session) {
        return session.getAttribute(UserConstants.USER_SESSION_KEY) != null;
    }
}
