package com.ajaxjs.user;

import com.ajaxjs.framework.TestHelper;
import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.user.model.ExpiresCheck;
import com.ajaxjs.util.date.LocalDateUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * SSO 工具类
 *
 * @author Frank Cheung
 */
public class SsoUtil {
    /**
     * 获取 expiresIn 与当前时间对比，看是否超时
     *
     * @param token 令牌
     * @return true 表示超时
     */
    public static boolean checkIfExpire(ExpiresCheck token) {
        long expiresIn = token.getExpiresIn();
        LocalDateTime expiresDateTime = LocalDateUtils.ofEpochSecond(expiresIn);// 过期日期

        return expiresDateTime.isBefore(LocalDateTime.now());
    }

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
}
