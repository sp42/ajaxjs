package com.ajaxjs.cms.shop;

import java.util.Calendar;

import com.ajaxjs.orm.thirdparty.SnowflakeIdWorker;

public class ShopHelper {
	/**
	 * 生成外显的订单号
	 * @return
	 */
	public static String getOutterOrderNo() {
		String orderNo = Calendar.getInstance().get(Calendar.YEAR) + "" + SnowflakeIdWorker.idWorker.nextId();
		return orderNo;
	}
}
