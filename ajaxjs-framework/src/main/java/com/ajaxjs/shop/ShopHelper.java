package com.ajaxjs.shop;

import java.math.BigDecimal;
import java.util.Calendar;

import com.ajaxjs.orm.SnowflakeIdWorker;

public class ShopHelper {
	/**
	 * 生成外显的订单号
	 * 
	 * @return 外显的订单号
	 */
	public static String getOutterOrderNo() {
		return Calendar.getInstance().get(Calendar.YEAR) + "" + SnowflakeIdWorker.idWorker.nextId();
	}

	/**
	 * 转换为分的字符串
	 * 
	 * @param price 单位是元
	 * @return 分的字符串
	 */
	public static String toCent(BigDecimal price) {
		return String.valueOf(price.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
	}
}
