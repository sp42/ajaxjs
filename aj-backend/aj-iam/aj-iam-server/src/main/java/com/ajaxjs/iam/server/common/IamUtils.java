package com.ajaxjs.iam.server.common;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class IamUtils {
    public static void send303Redirect(HttpServletResponse resp, String newUrl) {
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);   // 设置状态码为 303
        resp.setHeader("Location", newUrl);  // 设置重定向的目标 URL
    }

    public static void responseHTML(HttpServletResponse resp, String html) {
        resp.setContentType("text/html");

        try {
            resp.getWriter().write(html);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
