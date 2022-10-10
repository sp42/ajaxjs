package com.ajaxjs.util;

import java.util.concurrent.TimeUnit;

public class ThreadUtil {
	/**
	 * 挂起当前线程
	 *
	 * @param timeout  挂起的时长
	 * @param timeUnit 时长单位
	 * @return 被中断返回false，否则true
	 */
	public static boolean sleep(Number timeout, TimeUnit timeUnit) {
		try {
			timeUnit.sleep(timeout.longValue());
		} catch (InterruptedException e) {
			return false;
		}

		return true;
	}

	public static boolean sleep(Number timeout) {
		return sleep(timeout, TimeUnit.SECONDS);
	}

}
