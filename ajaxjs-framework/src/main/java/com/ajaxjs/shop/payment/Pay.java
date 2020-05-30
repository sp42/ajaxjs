package com.ajaxjs.shop.payment;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.mvc.MvcConstant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.payment.Alipay;
import com.ajaxjs.payment.wxpay.PerpayReturn;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.model.OrderInfo;
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

	public static String doPay(OrderInfo order, ModelAndView mv) throws ServiceException, AlipayApiException {
		LOGGER.info("进行支付");

		switch (order.getPayType()) {
		case ShopConstant.ALI_PAY:
			if (!ShopHelper.hasState(ConfigService.getValueAsInt("shop.AllowPayType"), ShopHelper.ALI_PAY))
				throw new ServiceException("不支持支付宝支付");

			Alipay alipay = new Alipay();
			alipay.setSubject("支付我们的产品");
			alipay.setBody("");
			alipay.setOut_trade_no(order.getOrderNo());
			alipay.setTotal_amount(order.getTotalPrice().toString());

			return "html::" + Alipay.connect(alipay);
		case ShopConstant.WX_PAY:
			if (!ShopHelper.hasState(ConfigService.getValueAsInt("shop.AllowPayType"), ShopHelper.WX_PAY))
				throw new ServiceException("不支持微信支付");

			PerpayReturn p = WxPay.pcUnifiedOrder(order);

			mv.put("totalPrice", order.getTotalPrice());
			mv.put("codeUrl", p.getCode_url());

			return MvcConstant.JSP_PERFIX_WEBINF + "/shop/wxpay";
		}

		return "html:: ERROR Can't pay!";
	}
}
