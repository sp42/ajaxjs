package com.ajaxjs.framework.shop;

import java.util.Calendar;

import com.ajaxjs.sql.util.SnowflakeId;

public class Utils {

	/**
	 * 生成外显的订单号
	 * 
	 * @return 外显的订单号
	 */
	public static String getOutterOrderNo() {
		return Calendar.getInstance().get(Calendar.YEAR) + "" + SnowflakeId.get();
	}
}
