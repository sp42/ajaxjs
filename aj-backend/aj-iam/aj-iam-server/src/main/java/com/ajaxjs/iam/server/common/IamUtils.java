package com.ajaxjs.iam.server.common;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 工具类
 */
public class IamUtils {
    /**
     * 发送HTTP 303重定向响应。
     * <p>
     * 使用此方法可以将客户端重定向到一个新的URL。HTTP 303状态码表示“See Other”，它告知客户端可以通过Location头字段中提供的URL获取所需的资源。
     *
     * @param resp   HttpServletResponse对象，用于设置响应状态码和头信息。
     * @param newUrl 重定向的目标URL，客户端将被重定向到这个URL。
     */
    public static void send303Redirect(HttpServletResponse resp, String newUrl) {
        resp.setStatus(HttpServletResponse.SC_SEE_OTHER);   // 设置状态码为 303
        resp.setHeader("Location", newUrl);  // 设置重定向的目标 URL
    }


    /**
     * 向HTTP响应中写入HTML内容。
     * <p>
     * 此方法用于设置响应的类型为HTML，并将提供的HTML字符串写入响应体。它不直接返回任何值，但通过HTTP响应向客户端发送HTML内容。
     *
     * @param resp HTTP响应对象，用于设置响应类型和写入响应体。
     * @param html 要写入响应体的HTML字符串。
     */
    public static void responseHTML(HttpServletResponse resp, String html) {
        // 设置响应的Content-Type为text/html，告知客户端将要接收的內容类型。
        resp.setContentType("text/html");

        try {
            // 通过响应对象的getWriter方法获取到输出流，然后将HTML字符串写入到输出流中。
            resp.getWriter().write(html);
        } catch (IOException e) {
            // 捕获并打印写入过程中可能出现的IOException。
            e.printStackTrace();
        }
    }

}
