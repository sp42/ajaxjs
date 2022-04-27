package com.ajaxjs.user.sso;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.sso.model.ErrorCodeEnum;
import com.ajaxjs.user.sso.model.ExpiresCheck;
import com.ajaxjs.util.date.LocalDateUtils;

/**
 * SSO 工具类
 * 
 * @author Frank Cheung
 *
 */
public class SsoUtil {
	/**
	 * 输出符合 Oauth 规范的异常
	 * 
	 * @param err
	 * @return
	 */
	public static String oauthError(ErrorCodeEnum err) {
		return oauthError(err.getError(), err.getErrorDescription());
	}

	public static String oauthError(String errCode, String errMsg) {
		Map<String, Object> map = new HashMap<>();
		map.put("error", errCode);
		map.put("error_description", errMsg);

		return BaseController.toJson(map, false, false);
	}

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
}
