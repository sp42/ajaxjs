package com.ajaxjs.shop.pay.model;

/**
 * 支付策略
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface PayStrategy {
	/**
	 * 支付前准备支付参数
	 *
	 * @param ctx
	 * @return
	 */
	boolean prePay(PayStrategyContext ctx);

	/**
	 * 支付后处理支付回调结果
	 * 
	 * @param ctx
	 * @param isPaySuccess
	 */
	void afterPay(PayStrategyContext ctx, boolean isPaySuccess);
}
