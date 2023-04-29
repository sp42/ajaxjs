package com.ajaxjs.sso.common;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.sso.model.ExpiresCheck;
import com.ajaxjs.util.TestHelper;
import com.ajaxjs.util.date.LocalDateUtils;

/**
 * SSO 工具类
 * 
 * @author Frank Cheung
 *
 */
public class SsoUtil {
	/**
	 * 获取 expiresIn 与当前时间对比，看是否超时
	 * 
	 * @param token 令牌
	 * @return true 表示超时
	 */
	public static boolean checkIfExpire(ExpiresCheck token) {
		long expiresIn = token.getExpiresIn();
		LocalDateTime expiresDateTime = LocalDateUtils.ofEpochSecond(expiresIn);// 过期日期

		return expiresDateTime.isBefore(LocalDateTime.now());
	}

	private static final String AUTH_TENTANT_CODE = "auth_tentant_code";

	/**
	 * 从 HTTP 头中获取租户编码
	 * 
	 * @return 租户编码
	 */
	public static String getTenantCode() {
		return DiContextUtil.getRequest().getHeader(AUTH_TENTANT_CODE);
	}

	private static final String AUTH_TENTANT_ID = "auth_tentant_id";

	/**
	 * 从 HTTP 头中获取租户 id
	 * 
	 * @return 租户编码
	 */
	public static int getTenantId() {
		HttpServletRequest request = DiContextUtil.getRequest();

		if (request == null) {
			if (TestHelper.isRunningTest())
				return 1; // 单测模式下，给个测试值
			else
				return 0;
		}

		String id = request.getHeader(AUTH_TENTANT_ID);

		return StringUtils.hasText(id) ? Integer.parseInt(id) : 0;
	}
}
