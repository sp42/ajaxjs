package com.ajaxjs.model;

import java.util.concurrent.TimeUnit;

/**
 * 过期时间相关枚举
 *
 * @author zifangsky
 * @date 2018/8/18
 */
public enum ExpireEnum {
	/**
	 * Authorization Code 的有效期为 10 分钟
	 */
	AUTHORIZATION_CODE(10, TimeUnit.MINUTES),

	/**
	 * Access Token 的有效期为 30 天
	 */
	ACCESS_TOKEN(30, TimeUnit.DAYS),

	/**
	 * Refresh Token 的有效期为 365 天
	 */
	REFRESH_TOKEN(365, TimeUnit.DAYS);

	/**
	 * 过期时间
	 */
	private int time;

	/**
	 * 时间单位
	 */
	private TimeUnit timeUnit;

	/**
	 * 创建过期时间相关枚举
	 * 
	 * @param time     过期时间
	 * @param timeUnit 时间单位
	 */
	ExpireEnum(int time, TimeUnit timeUnit) {
		this.time = time;
		this.timeUnit = timeUnit;
	}

	public int getTime() {
		return time;
	}

	public TimeUnit getTimeUnit() {
		return timeUnit;
	}
}
