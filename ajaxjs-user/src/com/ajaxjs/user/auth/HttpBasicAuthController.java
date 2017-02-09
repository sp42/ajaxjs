package com.ajaxjs.user.auth;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

/**
 * Servlet implementation class Admin
 */
//@javax.servlet.annotation.WebServlet("/admin")
public class HttpBasicAuthController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public static final String userid = "admin";
	public static String pwd = "123123";
	
	@Override
	public void init(ServletConfig config) throws ServletException   {
		super.init(config);
		
		// 读取配置密码
		if(config.getInitParameter("adminPassword") != null){
			pwd = config.getInitParameter("adminPassword");
		}
	}
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("deprecation")
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("utf-8"); 
		
		if (!checkAuth(request)) {
			// 如果认证失败,则要求认证 ，不能输入中文
			String msg = "\"Please input your account\"";

			// 发送状态码 401, 不能使用 sendError，坑
			response.setCharacterEncoding("utf-8");
			response.setStatus(401, "Authentication Required");
			// 发送要求输入认证信息,则浏览器会弹出输入框
			response.setHeader("WWW-Authenticate", "Basic realm=" + msg);
		}else{
			String action = request.getParameter("action");

			if (action != null) {
				switch (action) {
				case "workbench":
					request.getRequestDispatcher("/asset/jsp/user/admin/workbench.jsp").include(request, response);
					break;
				case "logout":
					response.getWriter().append("已退出！");
					break;
				default:
					response.getWriter().append("无效 action！");
				}
			} else {
				request.getRequestDispatcher("/asset/jsp/user/admin/index.jsp").include(request, response);
			}
		}
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
