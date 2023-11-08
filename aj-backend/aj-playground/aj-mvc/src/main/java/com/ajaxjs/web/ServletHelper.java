/**
 * Copyright sp42 frank@ajaxjs.com
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.web;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.net.http.Tools;
import com.ajaxjs.util.cache.LRUCache;
import com.ajaxjs.web.mvc.MvcRequest;

/**
 * Servlet 辅助工具类
 *
 * @author sp42 frank@ajaxjs.com
 */
public class ServletHelper {
    /**
     * 获取所有 URL 上的参数
     *
     * @param r       请求对象
     * @param without 不需要的字段
     * @return URL 查询参数结对的字符串
     */
    public static String getAllQueryParameters(HttpServletRequest r, String without) {
        StringBuilder sb = new StringBuilder();
        Enumeration<String> parameterList = r.getParameterNames();
        int i = 0;

        while (parameterList.hasMoreElements()) {
            String name = parameterList.nextElement();
            if (name.equals(without)) // 排除指定的
                continue;

            if (i++ > 0) sb.append("&");

            sb.append(name).append("=").append(r.getParameter(name));
        }

        return sb.toString();
    }

    public static String getAllQueryParameters(HttpServletRequest r) {
        return getAllQueryParameters(r, null);
    }

    /**
     * 转义 MySql 语句中使用的字符串中的特殊字符
     *
     * @param str SQL
     * @return 转换后的字符串
     */
    public static String MysqlRealScapeString(String str) {
        str = str.replace("\\", "\\\\");
        str = str.replace("'", "\\'");
        str = str.replace("\0", "\\0");
        str = str.replace("\n", "\\n");
        str = str.replace("\r", "\\r");
        str = str.replace("\"", "\\\"");
        str = str.replace("\\x1a", "\\Z");

        return str;
    }
}
