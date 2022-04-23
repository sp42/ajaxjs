package com.ajaxjs.framework.shop.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.ajaxjs.framework.shop.alipay.core.AbstractAlipay;

/**
 * 
 * @author Frank Cheung
 *
 */
public class MobileDirectSDK extends AbstractAlipay {
	public MobileDirectSDK(AlipayConfig config) {
		super(config);
	}

	public AlipayRequestData makeSimpleMobileTrade(String tradeId, String subject, String body, String showUrl, double total, String notifyUrl) {
		AlipayRequestData request = new AlipayRequestData();
		request.setString("partner", config.getPartnerId());
		request.setString("seller_id", config.getSellerAccount());
		request.setString("out_trade_no", tradeId);
		request.setString("subject", subject);
		request.setString("body", body);
		request.setString("total_fee", String.valueOf(total));
		request.setString("notify_url", notifyUrl);
		request.setString("service", "mobile.securitypay.pay");
		request.setString("payment_type", "1");
		request.setString("_input_charset", "utf-8");
		request.setString("it_b_pay", "30m");
		request.setString("show_url", showUrl);

		return request;
	}

	public String create(AlipayRequestData request) throws UnsupportedEncodingException {
		String sign = URLEncoder.encode(signRSAWithQuote(request.getSortedParameters()), "utf-8");
		request.setString("sign", sign);
		request.setString("sign_type", "RSA");

		return join(request.getSortedParameters(), false, true);
	}
}
