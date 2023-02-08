package com.ajaxjs.shop.pay.service;

import org.springframework.stereotype.Component;

import com.ajaxjs.shop.pay.model.PayStrategy;
import com.ajaxjs.shop.pay.model.PayStrategyContext;

/**
 * 微信支付
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
@Component
public class WechatPay implements PayStrategy {
	@Override
	public boolean prePay(PayStrategyContext ctx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void afterPay(PayStrategyContext ctx, boolean isPaySuccess) {
		// TODO Auto-generated method stub

	}

}
