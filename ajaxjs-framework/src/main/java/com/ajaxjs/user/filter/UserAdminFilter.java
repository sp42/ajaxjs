package com.ajaxjs.user.filter;

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
import javax.servlet.http.HttpSession;

import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.user.role.RoleService;

/**
 * 进入后台一切资源的拦截器。这是一个标准的 Servlet 过滤器
 */
public class UserAdminFilter implements Filter {
	@SuppressWarnings("deprecation")
	@Override
	public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) _request;
		HttpServletResponse response = (HttpServletResponse) _response;

		HttpSession s = request.getSession();
		if (request.getRequestURI().equals(request.getContextPath() + "/admin/login/")) {
			// 后台登录
			chain.doFilter(request, response);
		} else if (!RoleService.check(s, RightConstant.ADMIN_SYSTEM_ALLOW_ENTNER)) {
			response.setStatus(401, "Authentication Required");
			response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
			response.setContentType("text/html");
			response.getWriter().append(String.format(NO_ACCESS, request.getContextPath()));
		} else
			chain.doFilter(request, response);

	}

	private static final String NO_ACCESS = "<title>禁止访问，非法权限</title><meta charset=\"utf-8\" /> 禁止访问，非法权限。Authentication Required <a href=\"%s/admin/login/\">登 录</a>";

	@Override
	public void destroy() {
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
	}
}
