package com.ajaxjs.user.sso.common;

import java.time.LocalDateTime;

import com.ajaxjs.sso.model.ExpiresCheck;
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
}
