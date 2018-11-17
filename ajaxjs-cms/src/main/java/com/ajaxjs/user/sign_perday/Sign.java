package com.ajaxjs.user.sign_perday;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * 每日签到
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface Sign {
	/**
	 * 检查今天是否已经签到，避免重复签到
	 * 
	 * @return
	 */
	public boolean isSignToday();

	/**
	 * 获取一个范围内的签到情况
	 * 
	 * @param daysAgo
	 *            过去的第几天
	 * @return 用 map 保存签到情况：日期范围由 daysAgo 指定，value = true 签到了， false 没签到
	 */
	public LinkedHashMap<Date, Boolean> getSignInfo(int daysAgo);

	/**
	 * 获取一个范围内的签到情况（默认7日）
	 * 
	 * @return 用 map 保存签到情况：日期范围由 daysAgo 指定，value = true 签到了， false 没签到
	 */
	public LinkedHashMap<Date, Boolean> getSignInfo();

}
