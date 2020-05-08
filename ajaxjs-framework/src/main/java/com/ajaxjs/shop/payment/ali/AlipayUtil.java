package com.ajaxjs.shop.payment.ali;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

public class AlipayUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(AlipayUtil.class);

	// 支付宝网关
	private static final String GATEWAY_URL = "https://openapi.alipay.com/gateway.do";

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

		LOGGER.info("::::::::::" + result);

		// 返回付款信息
		return result;
	}
}
