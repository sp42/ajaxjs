package com.ajaxjs.web.website.data;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.convert.EntityConvert;

public class GetData {
    private static final String BASE_API = "http://127.0.0.1:8088/base";

    private static final String PAGE_API = "/common_api/%s/page?auth_tenant_id=%s&start=%s&limit=%s";

    public static void getPageList(HttpServletRequest req, int tenantId, String namespace, String accessToken) {
        String start = req.getParameter("start") == null ? "0" : req.getParameter("start");
        String limit = req.getParameter("pageSize") == null ? "10" : req.getParameter("pageSize");
        String url = BASE_API + String.format(PAGE_API, namespace, tenantId, start, limit);

        Map<String, Object> resultMap = Get.api(url,
                conn -> conn.addRequestProperty("Authorization", "Bearer " + accessToken));
        ResponseResult result = EntityConvert.map2Bean(resultMap, ResponseResult.class);

        if (result.getStatus() == 1) {
            Map<String, Object> data = result.getData();

            if (req != null) {
                req.setAttribute("LIST", data.get("rows"));
                req.setAttribute("PAGE_TOTAL", data.get("total"));
            }
        }
    }

    private static final String INFO_API = "/common_api/%s/%s?auth_tenant_id=%s";

    public static void getInfo(HttpServletRequest req, int tenantId, String namespace, String accessToken) {
        String id = req.getParameter("id");
        String url = BASE_API + String.format(INFO_API, namespace, id, tenantId);

        Map<String, Object> resultMap = Get.api(url, conn -> conn.addRequestProperty("Authorization", "Bearer " + accessToken));
        ResponseResult result = EntityConvert.map2Bean(resultMap, ResponseResult.class);

        if (result.getStatus() == 1) {
            Map<String, Object> data = result.getData();

            if (req != null) {
                req.setAttribute("info", data);
            }
        }
    }

    /**
     * 获取其他 QueryString 参数
     *
     * @param request
     * @return
     */
    public static String getQueryString(HttpServletRequest request) {
        Map<String, String[]> parameterMap = request.getParameterMap();

        // 遍历参数列表，并将每个参数的值添加到分页参数中
        StringBuffer sb = new StringBuffer();

        for (String name : parameterMap.keySet()) {
            if (!name.equals("start") && !name.equals("pageSize")) {
                String[] values = parameterMap.get(name);
                // 只取第一个值
                String value = values[0];
                // 编码
                value = StrUtil.urlEncodeQuery(value);
                // 添加到查询字符串中
                sb.append("&").append(name).append("=").append(value);
            }
        }

        return sb.toString();
    }

    public static String substring(String str, int length) {
        if (str.length() < length)
            return str;
        else return str.substring(0, length);
    }
}
