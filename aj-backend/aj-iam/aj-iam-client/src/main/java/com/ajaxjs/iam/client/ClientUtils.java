package com.ajaxjs.iam.client;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

public class ClientUtils {
	public static final String OAUTH_STATE = "OAUTH_STATE";
	
	// 返回自定义错误页面
	private static final String html = "<html><body><h1>403 Forbidden</h1><p>非法操作，请求被拒绝。</p></body></html>";

	/**
	 * 返回 HTTP 403
	 * 
	 * @param response 响应对象
	 */
	public static void returnForbidden(HttpServletResponse response) {
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setContentType("text/html;charset=UTF-8");// 设置响应内容类型为HTML

		try {
			response.getWriter().println(html);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
