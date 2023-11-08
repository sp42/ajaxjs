package com.ajaxjs.iam.user.service;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.TestHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 租户管理
 */
@Service
public class TenantService {
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
    public static Integer getTenantId() {
        HttpServletRequest request = DiContextUtil.getRequest();

        if (request == null)
            return TestHelper.isRunningTest() ? 1 : 0; // 单测模式下，给个测试值

        String id = request.getHeader(AUTH_TENANT_ID);

        return StringUtils.hasText(id) ? Integer.parseInt(id) : null;
    }

    /**
     * 添加租户 id 的过滤
     *
     * @param sql SQL
     * @return SQL
     */
    public static String addTenantIdQuery(String sql) {
        int tenantId = getTenantId();

        if (tenantId != 0)
            sql += " AND　tenant_id = " + tenantId;

        return sql;
    }
}
