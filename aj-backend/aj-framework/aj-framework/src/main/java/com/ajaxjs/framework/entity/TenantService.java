package com.ajaxjs.framework.entity;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.util.TestHelper;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 租户管理
 */
public class TenantService {
    @Deprecated
    private static final String AUTH_TENANT_CODE = "auth_tenant_code";

    /**
     * 从 HTTP 头中获取租户编码
     *
     * @return 租户编码
     */
    @Deprecated
    public static String getTenantCode() {
        return Objects.requireNonNull(DiContextUtil.getRequest()).getHeader(AUTH_TENANT_CODE);
    }

    public static final String AUTH_TENANT_ID = "auth_tenant_id";

    /**
     * 从 HTTP 头中获取租户 id
     *
     * @return 租户 id
     */
    public static Integer getTenantId() {
        HttpServletRequest request = DiContextUtil.getRequest();

        if (request == null)
            return TestHelper.isRunningTest() ? 1 : 0; // 单测模式下，给个测试值

        String tenantIdStr = request.getHeader(AUTH_TENANT_ID);

        if (!StringUtils.hasText(tenantIdStr))
            tenantIdStr = request.getParameter(AUTH_TENANT_ID);

        if (StringUtils.hasText(tenantIdStr)) {
            Integer tenantId = Integer.parseInt(tenantIdStr);
            request.setAttribute(AUTH_TENANT_ID, tenantId);

            return tenantId;
        } else
            return null;
    }

//    private static void checkUserPrivilege(HttpServletRequest request, Integer tenantId) {
//        SimpleUser user = UserService.getUserFromRequestCxt();
//    }

    /**
     * 添加租户 id 的过滤
     *
     * @param sql SQL
     * @return SQL
     */
    public static String addTenantIdQuery(String sql) {
        Integer tenantId = getTenantId();

        if (tenantId != null && tenantId != 0) {
            if (sql.contains("1=1"))
                sql = sql.replace("1=1", "1=1 AND tenant_id = " + tenantId);
            else
                sql += " AND　tenant_id = " + tenantId;
        }

        return sql;
    }
}
