package com.ajaxjs.user.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiFunction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ajaxjs.framework.Application;
import com.ajaxjs.framework.IComponent;
import com.ajaxjs.user.role.RightConstant;
import com.ajaxjs.user.role.RoleService;

/**
 * 进入后台一切资源的拦截器。
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserAdminFilter implements IComponent {
	private static final String NO_ACCESS = "<title>禁止访问，非法权限</title><meta charset=\"utf-8\" /> 禁止访问，非法权限。Authentication Required <a href=\"%s/admin/login/\">登 录</a>";

	private static final BiFunction<HttpServletRequest, HttpServletResponse, Boolean> checkAdmin = (req, resp) -> {
		String ctxPath = req.getContextPath(), uri = req.getRequestURI();

		if (uri.startsWith(ctxPath + "/admin")) {
			HttpSession s = req.getSession();

			if (uri.equals(ctxPath + "/admin/login/")) {
				// 后台登录
				return true;
			} else if (!RoleService.check(s, RightConstant.ADMIN_SYSTEM_ALLOW_ENTNER)) {
				try {
					resp.setStatus(401);
					resp.setCharacterEncoding(StandardCharsets.UTF_8.toString());
					resp.setContentType("text/html");
					resp.getWriter().append(String.format(NO_ACCESS, req.getContextPath()));
				} catch (IOException e) {
					e.printStackTrace();
				}

				return false;
			}
		}

		return true;
	};

	static {
		Application.onRequest.add(0, checkAdmin);
	}
}
