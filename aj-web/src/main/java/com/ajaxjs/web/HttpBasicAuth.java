package com.ajaxjs.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;

import com.ajaxjs.util.StrUtil;

/**
 * 简单的 HTTP Basic 认证
 *
 * https://blog.csdn.net/zhangxin09/article/details/17226953
 * https://www.iteye.com/blog/fengjianrong-2379764
 * https://www.cnblogs.com/trawalker/p/13624310.html
 */
public class HttpBasicAuth {
	/**
	 * @param resp
	 */
	@SuppressWarnings("deprecation")
	public static void sendInput(HttpServletResponse resp) {
		String msg = "\"Please input your account\"";  // 如果认证失败,则要求认证 ，不能输入中文
		resp.setStatus(401, "Authentication Required");// 发送状态码 401, 不能使用 sendError，坑
		resp.setHeader("WWW-Authenticate", "Basic realm=" + msg);// 发送要求输入认证信息,则浏览器会弹出输入框

//        try {
//            resp.getWriter().append("<meta charset=\"utf-8\" />Please login! 请登录系统！");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
	}

	/**
	 * 检查是否合法登录
	 *
	 * @param request 请求对象
	 * @return 是否合法登录
	 */
	public static boolean checkAuth(HttpServletRequest request, String username, String password) {
		return checkAuth(request.getHeader("Authorization"), username, password);
	}

	/**
	 * 检查是否合法登录
	 *
	 * @param authorization 认证后每次HTTP请求都会附带上 Authorization 头信息
	 * @param username      用户名
	 * @param password      密码
	 * @return true = 认证成功/ false = 需要认证
	 */
	private static boolean checkAuth(String authorization, String username, String password) {
		if (!StringUtils.hasText(authorization))
			return false;

		String[] basicArray = authorization.split("\\s+");
		if (isBadArray(basicArray))
			return false;

		String idpass = StrUtil.base64Decode(basicArray[1]);
		if (!StringUtils.hasText(idpass))
			return false;

		String[] idpassArray = idpass.split(":");
		if (isBadArray(idpassArray))
			return false;

		return username.equalsIgnoreCase(idpassArray[0]) && password.equalsIgnoreCase(idpassArray[1]);
	}

	/**
	 * 是否不合法的数组
	 *
	 * @param arr
	 * @return 是否不合法的数组
	 */
	private static boolean isBadArray(String[] arr) {
		return arr == null || arr.length != 2;
	}
}
