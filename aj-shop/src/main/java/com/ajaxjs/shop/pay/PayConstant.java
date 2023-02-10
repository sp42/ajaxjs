package com.ajaxjs.shop.pay;

import com.ajaxjs.shop.pay.model.PayStrategy;
import com.ajaxjs.shop.pay.service.AliPay;
import com.ajaxjs.shop.pay.service.WechatPay;
import com.ajaxjs.spring.DiContextUtil;

/**
 * 支付常量
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public interface PayConstant {
	/**
	 * 支付服务提供商
	 * 
	 * @author Frank Cheung sp42@qq.com
	 *
	 */
	public static enum PayServiceProvider {
		ALIPAY(AliPay.class), WECHAT(WechatPay.class);

		private Class<? extends PayStrategy> strategyClass;

		PayServiceProvider(Class<? extends PayStrategy> clz) {
			strategyClass = clz;
		}

		/**
		 * 获取支付实例
		 * 
		 * @return
		 */
		PayStrategy getInstance() {
			return DiContextUtil.getBean(strategyClass);
		}
	}

	/**
	 * 支付状态
	 * 
	 */
	public static enum PayStatus {
		/**
		 * 未支付
		 */
		UNPAID,

		/**
		 * 部分支付 下达订单时，如果已经选择积分或礼品卡支付了部分金额，则状态设置为此状态。
		 */
		PARTIAL_PAID,

		/**
		 * 已支付
		 */
		PAID_OFF
	}
}
