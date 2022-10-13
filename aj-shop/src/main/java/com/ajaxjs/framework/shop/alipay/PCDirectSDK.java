package com.ajaxjs.framework.shop.alipay;

import java.util.List;

import com.ajaxjs.framework.shop.alipay.core.AbstractAlipay;
import com.ajaxjs.framework.shop.alipay.model.StringPair;

public class PCDirectSDK extends AbstractAlipay {
	public PCDirectSDK(final AlipayConfig config) {
		super(config);
	}

	public String getActionURL() {
		return "https://mapi.alipay.com/gateway.do?_input_charset=utf-8";
	}

	public AlipayRequestData makeSimpleDirectPayRequest(String tradeId, String subject, double total, String notifyURL, String returnURL, String errorNotifyURL) {
		AlipayRequestData data = new AlipayRequestData();
		// Required, sign and sign_type at last
		data.setString("service", "create_direct_pay_by_user");
		data.setString("partner", config.getPartnerId());
		data.setString("_input_charset", "utf-8");

		// Optional
		data.setString("notify_url", notifyURL);
		data.setString("return_url", returnURL);
		data.setString("error_notify_url", errorNotifyURL);

		// Required
		data.setString("out_trade_no", tradeId);
		data.setString("subject", subject);
		data.setString("payment_type", "1");
		data.setString("total_fee", String.valueOf(total));
		data.setString("seller_id", config.getPartnerId());

		return data;
	}

	public AlipayRequestData makeVerifyRequest(String notifyId) {
		AlipayRequestData data = new AlipayRequestData();
		data.setString("service", "notify_verify");
		data.setString("partner", config.getPartnerId());
		data.setString("_input_charset", "utf-8");
		data.setString("notify_id", notifyId);

		return data;
	}

	private List<StringPair> sign(List<StringPair> p) {
		String sign;

		if (preferRSA) {
			sign = signRSA(p);
			p.add(new StringPair("sign_type", "RSA"));
		} else {
			sign = signMD5(p);
			p.add(new StringPair("sign_type", "MD5"));
		}

		p.add(new StringPair("sign", sign));

		return p;
	}

	public List<StringPair> create(AlipayRequestData request) {
		return sign(request.getSortedParameters());
	}
	
	/**
	 * 验证结果
	 * 
	 * @param response
	 * @return
	 */
	public boolean verify(AlipayResponseData response) {
		List<StringPair> parameterList = response.getSortedParameters("sign", "sign_type");

		if (response.getString("sign_type").equals("RSA"))
			return verifyRSA(response.getString("sign"), parameterList);
		else if (response.getString("sign_type").equals("MD5"))
			return verifyMD5(response.getString("sign"), parameterList);
		else
			return false;
	}
}
