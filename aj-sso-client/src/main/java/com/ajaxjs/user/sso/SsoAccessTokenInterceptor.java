package com.ajaxjs.user.sso;

import java.io.IOException;
import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.UserSession;
import com.ajaxjs.util.date.LocalDateUtils;

/**
 * 校验 AccessToken 的拦截器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@Component
public class SsoAccessTokenInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp, Object handler) {
		String accessToken = req.getParameter("access_token");

		if (!StringUtils.hasText(accessToken)) {
			err(resp, "缺少 access_token 参数");

			return false;
		}

		Object object = req.getSession().getAttribute(accessToken);

		if (object == null) {
			// TODO 是否拿 Token 去 SSO 中心再校验一下
			err(resp, "非法 AccessToken");

			return false;
		} else {
		}

		UserSession userSess = (UserSession) object;

		// 如果 Access Token 已经失效，则返回错误提示
		if (checkIfExpire(userSess.accessToken)) {
			// TODO 是否要删除过期 token？
			err(resp, "access_token 已超时");
			return false;
		} else
			return true;
	}

	/**
	 * 获取 expiresIn 与当前时间对比，看是否超时
	 * 
	 * @param token 令牌
	 * @return true 表示超时
	 */
	static boolean checkIfExpire(AccessToken token) {
		long expiresIn = token.getExpiresIn();
		LocalDateTime expiresDateTime = LocalDateUtils.ofEpochSecond(expiresIn);// 过期日期
		return expiresDateTime.isBefore(LocalDateTime.now());
	}

	static void err(HttpServletResponse resp, String msg) {
		resp.setStatus(HttpStatus.UNAUTHORIZED.value());
		resp.setHeader("Content-type", "application/json;charset=UTF-8");

		try {
			resp.getWriter().write(BaseController.jsonNoOk(msg));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
