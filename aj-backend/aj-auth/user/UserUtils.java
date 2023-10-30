package com.ajaxjs.user;

import com.ajaxjs.framework.TestHelper;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.user.model.ExpiresCheck;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserConstants;
import com.ajaxjs.util.date.LocalDateUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 用户工具类
 *
 * @author Frank Cheung
 */
public class UserUtils {


    private static final String AUTH_TENANT_CODE = "auth_tenant_code";

    /**
     * 从 HTTP 头中获取租户编码
     *
     * @return 租户编码
     */
    public static String getTenantCode() {
        return Objects.requireNonNull(DiContextUtil.getRequest()).getHeader(AUTH_TENANT_CODE);
    }

    private static final String AUTH_TENANT_ID = "auth_tenant_id";

    /**
     * 从 HTTP 头中获取租户 id
     *
     * @return 租户编码
     */
    public static int getTenantId() {
        HttpServletRequest request = DiContextUtil.getRequest();

        if (request == null) {
            if (TestHelper.isRunningTest())
                return 1; // 单测模式下，给个测试值
            else
                return 0;
        }

        String id = request.getHeader(AUTH_TENANT_ID);

        return StringUtils.hasText(id) ? Integer.parseInt(id) : 0;
    }

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
}
