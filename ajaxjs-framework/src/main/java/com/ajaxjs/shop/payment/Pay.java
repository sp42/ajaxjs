package com.ajaxjs.shop.payment;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.ali.Alipay;
import com.ajaxjs.shop.payment.wechat.WxPay;
import com.ajaxjs.shop.payment.wechat.model.PerpayReturn;
import com.ajaxjs.util.logger.LogHelper;
import com.alipay.api.AlipayApiException;

/**
 * 统一支付
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Pay {
	private static final LogHelper LOGGER = LogHelper.getLog(Pay.class);

	public static String doPay(OrderInfo order, int payType, ModelAndView mv) {
		LOGGER.info("进行支付");

		switch (payType) {
		case ShopConstant.ALI_PAY:
			order.setPayType(ShopConstant.ALI_PAY);

			Alipay alipay = new Alipay();
			alipay.setSubject("支付我们的产品");
			alipay.setBody("");
			alipay.setOut_trade_no(order.getOrderNo());
			alipay.setTotal_amount(order.getTotalPrice().toString());

			try {
				return "html::" + Alipay.connect(alipay);
			} catch (AlipayApiException e) {
				LOGGER.warning(e);
				return "html::" + e;
			}
		case ShopConstant.WX_PAY:
			order.setPayType(ShopConstant.WX_PAY);
			PerpayReturn p = WxPay.pcUnifiedOrder(order);

			mv.put("totalPrice", order.getTotalPrice());
			mv.put("codeUrl", p.getCode_url());

			return Constant.JSP_PERFIX_WEBINF + "/shop/wxpay";
		}

		return "html:: ERROR Can't pay!";
	}
}
