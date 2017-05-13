package com.ajaxjs.user.auth;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.Init;

import sun.misc.BASE64Decoder;

/**
 * Servlet Filter implementation class Filter
 */

public class HttpBasicAuthFilter implements javax.servlet.Filter {
	public static final String userid = "admin"; // 写死只有一个用户 admin
	public static String pwd = "123123";
	
	/**
	 * @see HttpBasicAuthFilter#init(FilterConfig)
	 */
	public void init(FilterConfig config) throws ServletException {
		System.out.println(Init.ConsoleDiver + System.getProperty("line.separator") + "启动 HTTP BasicAuth 后台管理" + Init.ConsoleDiver);
		
		// 读取配置密码
		if(config.getInitParameter("adminPassword") != null){
			pwd = config.getInitParameter("adminPassword");
		}
	}

	/**
	 * @see HttpBasicAuthFilter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	@SuppressWarnings("deprecation")
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)_request;
		HttpServletResponse response = (HttpServletResponse)_response;
		
		if (!checkAuth(request)) {
			// 如果认证失败,则要求认证 ，不能输入中文
			String msg = "\"Please input your account\"";

			// 发送状态码 401, 不能使用 sendError，坑
			response.setCharacterEncoding("utf-8");
			response.setStatus(401, "Authentication Required");
			// 发送要求输入认证信息,则浏览器会弹出输入框
			response.setHeader("WWW-Authenticate", "Basic realm=" + msg);
			response.setCharacterEncoding("utf-8");
			response.getWriter().append("<meta charset=\"utf-8\" />Please login! 请登录系统！");
		} else {
			request.setAttribute("userName", userid);
			chain.doFilter(request, response);
		}
	}
	
	/**
	 * @see HttpBasicAuthFilter#destroy()
	 */
	@Override
	public void destroy() {
	}
	
	public static boolean checkAuth(HttpServletRequest request) {
		return checkAuth(request.getHeader("Authorization"), userid, pwd);
	}
	
	/**
	 * 是否空字符串
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmptyString(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * 是否不合法的数组
	 * 
	 * @param arr
	 * @return
	 */
	public static boolean isBadArray(String[] arr) {
		return arr == null || arr.length != 2;
	}
	
	/**
	 * 
	 * @param authorization
	 *            认证后每次HTTP请求都会附带上 Authorization 头信息
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @return true = 认证成功/ false = 需要认证
	 */
	public static boolean checkAuth(String authorization, String username, String password) {
		if (isEmptyString(authorization))
			return false;

		String[] basicArray = authorization.split("\\s+");
		if (isBadArray(basicArray))
			return false;

		String idpass = null;
		try {
			byte[] buf = new BASE64Decoder().decodeBuffer(basicArray[1]);
			idpass = new String(buf, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if (isEmptyString(idpass))
			return false;

		String[] idpassArray = idpass.split(":");
		if (isBadArray(idpassArray))
			return false;

		return username.equalsIgnoreCase(idpassArray[0]) && password.equalsIgnoreCase(idpassArray[1]);
	}
}
