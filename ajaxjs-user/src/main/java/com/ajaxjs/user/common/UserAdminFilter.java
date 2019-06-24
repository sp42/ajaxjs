package com.ajaxjs.user.common;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ajaxjs.framework.BaseController;

/**
 * 进入后台一切资料的拦截器
 */
public class UserAdminFilter implements Filter {
	@SuppressWarnings("deprecation")
	@Override
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;

		if (request.getRequestURI().equals(request.getContextPath() + "/admin/login/")) {
			// 后台登录
			request.getRequestDispatcher(BaseController.jsp("user/admin/admin-login.jsp")).forward(request, response);
		} else if (request.getSession().getAttribute("userId") == null) {
			response.setStatus(401, "Authentication Required");
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.setContentType("text/html");
			response.getWriter().append(String.format(noAccess, request.getContextPath()));
		} else {
			chain.doFilter(request, response);
		}

	}

	static final String noAccess = "<meta charset=\"utf-8\" /> 禁止访问，非法权限。Authentication Required <a href=\"%s/admin/login/\">登 录</a>";

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}
