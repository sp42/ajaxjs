package com.ajaxjs.cms.shop;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({ "unchecked", "rawtypes" })
public interface ShopConstant {
	public static final int ENTRY_GOODS = 56;
	public static final int ENTRY_GROUP = 57;
	
	public static final int TRADING = 0;
	public static final int TRADE_DONE = 1;
	public static final int TRADE_CANCELLED = 2;
	public static final int TRADE_FINISHED = 3;
	public static final int TRADE_SHIPPING = 4;
	
	public static final Map<Integer, String> TradeStatus = new HashMap() {
		private static final long serialVersionUID = -1L;

		{
			put(TRADING, "进行中");
			put(TRADE_SHIPPING, "发货中");
			put(TRADE_DONE, "已完成");
			put(TRADE_CANCELLED, "取消交易");
			put(TRADE_FINISHED, "已结算");
		}
	};

	public static final int WX_PAY = 0;
	public static final int ALI_PAY = 1;
	public static final int OFFLINE_PAY = 2;

	public static final Map<Integer, String> PayType = new HashMap() {
		private static final long serialVersionUID = -1L;
		
		{
			put(WX_PAY, "微信支付");
			put(ALI_PAY, "支付宝支付");
			put(OFFLINE_PAY, "货到付款");
		}
	};

	public static final int NOT_YET_PAY = 0;
	public static final int PAYED = 1;
	public static final int PAY_OFFLINE = 2;
	public static final int PAYED_OFFLINE = 3;
	
	public static final Map<Integer, String> PayStatus = new HashMap() {
		private static final long serialVersionUID = -1L;
		{
			put(NOT_YET_PAY, "未付款");
			put(PAYED, "已付款");
			put(PAY_OFFLINE, "线下付款");
			put(PAYED_OFFLINE, "线下付款已收款");
		}
	};
}
