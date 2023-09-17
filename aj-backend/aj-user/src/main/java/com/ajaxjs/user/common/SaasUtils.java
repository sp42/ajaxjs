package com.ajaxjs.user.common;

import com.ajaxjs.util.TestHelper;
import com.ajaxjs.framework.spring.DiContextUtil;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

public class SaasUtils {
    private static final String AUTH_TENANT_ID = "auth_tenant_id";

    /**
     * 从 HTTP 头中获取租户 id
     *
     * @return 租户编码
     */
    public static int getTenantId() {
        HttpServletRequest request = DiContextUtil.getRequest();

        if (request == null)
            return TestHelper.isRunningTest() ? 1 : 0; // 单测模式下，给个测试值

        String id = request.getHeader(AUTH_TENANT_ID);

        return StringUtils.hasText(id) ? Integer.parseInt(id) : 0;
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
