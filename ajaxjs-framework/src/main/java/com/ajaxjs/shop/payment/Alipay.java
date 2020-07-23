package com.ajaxjs.shop.payment;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.map.JsonHelper;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

public class Alipay {
	/* 商户订单号，必填 */
	private String out_trade_no;

	/* 订单名称，必填 */
	private String subject;

	/* 付款金额，必填 */
	private String total_amount;
	/* 商品描述，可空 */
	private String body;

	/* 超时时间参数 */
	private String timeout_express = "10m";

	private String product_code = "FAST_INSTANT_TRADE_PAY";

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTimeout_express() {
		return timeout_express;
	}

	public void setTimeout_express(String timeout_express) {
		this.timeout_express = timeout_express;
	}

	public String getProduct_code() {
		return product_code;
	}

	public void setProduct_code(String product_code) {
		this.product_code = product_code;
	}

	// 支付宝网关
	private static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

	/**
	 * 发起支付
	 * 
	 * @param alipay
	 * @return
	 * @throws AlipayApiException
	 */
	public static String connect(Alipay alipay) throws AlipayApiException {
		// 获取配置文件中应用ID
		String appId = ConfigService.getValueAsString("shop.payment.Alipay.app_id");

		// 获取配置文件中商户私钥
		String merchant_private_key = ConfigService.getValueAsString("shop.payment.Alipay.merchant_private_key");

		// 获取配置文件中支付宝公钥
		String alipay_public_key = ConfigService.getValueAsString("shop.payment.Alipay.merchant_public_key");

		// 获取配置文件中服务器异步通知页面路径
		String notifyUrl = ConfigService.getValueAsString("shop.payment.Alipay.notify_url");

		// 获取配置文件中页面跳转同步通知页面路径
		String returnUrl = ConfigService.getValueAsString("shop.payment.Alipay.return_url");

		// 1、获得初始化的AlipayClient
		AlipayClient alipayClient = new DefaultAlipayClient(GATEWAY_URL, appId, merchant_private_key, "json", "utf-8",
				alipay_public_key, "RSA2");

		// 2、设置请求参数
		AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
		alipayRequest.setReturnUrl(returnUrl);// 页面跳转同步通知页面路径
		alipayRequest.setNotifyUrl(notifyUrl);// 服务器异步通知页面路径
		alipayRequest.setBizContent(JsonHelper.beanToJson(alipay));// 封装参数

		// 3、请求支付宝进行付款，并获取支付结果
		String result = alipayClient.pageExecute(alipayRequest).getBody();

		// 返回付款信息
		return result;
	}
}
