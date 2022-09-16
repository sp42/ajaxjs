package com.ajaxjs.util;

import java.util.Date;

/**
 * 日期时间的工具类
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class DateTimeHelper {
	/**
	 * 过去了多少分钟
	 * 
	 * @param date 比较的日期
	 * @return 过去了多少分钟
	 */
	public static int passedMins(Date date) {
		long diff = new Date().getTime() - date.getTime();
		return new Long((diff / 1000) / 60).intValue();
	}
}
