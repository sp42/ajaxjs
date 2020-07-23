package com.ajaxjs.shop.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.Alipay;
import com.ajaxjs.shop.payment.wxpay.PerpayReturn;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.MvcConstant;
import com.alipay.api.AlipayApiException;

/**
 * 微信支付方法
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Pay {
	private static final LogHelper LOGGER = LogHelper.getLog(Pay.class);

	/**
	 * PC 版支付微信下单接口
	 * 
	 * @param order 订单详情
	 * @return
	 */
	public static PerpayReturn pcUnifiedOrder(OrderInfo order) {
		LOGGER.info("PC 微信下单请求交易开始");

		Map<String, String> data = commonSetUnifiedOrder(order);
		data.put("body", "TESTTEST");
		data.put("trade_type", "NATIVE");
		data.put("appid", ConfigService.getValueAsString("shop.payment.wx.appId"));
		data.put("spbill_create_ip", Tools.getIp());
		data.put("product_id", order.getId() + ""); // 商品 id，但一个订单可能包含多个商品，所以填入订单数据库 id
		data.put("device_info", "web");
		data.put("sign", generateSignature(data, ConfigService.getValueAsString("shop.payment.wx.apiSecret")));

		return sendUnifiedOrder(data);
	}

	/**
	 * 小程序调起支付参数
	 * 
	 * @param userId    用户 id
	 * @param orderInfo 订单详情
	 * @param ip        IP 地址
	 * @return 小程序支付所需的参数
	 */
	public static Map<String, ?> miniAppPay(long userId, OrderInfo orderInfo, String ip) {
		LOGGER.info("微信统一下单请求交易开始");

		Map<String, String> data = commonSetUnifiedOrder(orderInfo);
		data.put("body", "集结号-网超");
		data.put("trade_type", "JSAPI");
		data.put("appid", ConfigService.getValueAsString("mini_program.appId"));
		data.put("spbill_create_ip", ip);
		data.put("openid", OrderService.dao.findUserOpenId(userId)); // 小程序内调用登录接口，获取到用户的 openid
		data.put("sign", generateSignature(data, ConfigService.get("shop.payment.wx.apiSecret")));

		PerpayReturn result = sendUnifiedOrder(data);// 商户 server 调用支付统一下单

		if (result.isSuccess()) {
			LOGGER.info("获取 perpayid 成功！{0}", result.getPrepay_id());

			// 商户调用再次签名
			Map<String, String> map = new HashMap<>();
			map.put("appId", ConfigService.getValueAsString("mini_program.appId"));
			map.put("timeStamp", System.currentTimeMillis() / 1000 + "");
			map.put("nonceStr", CommonUtil.getRandomString(10)); // 可以是不同的随机字符串
			map.put("package", "prepay_id=" + result.getPrepay_id());
			map.put("signType", "MD5");

			String paySign = generateSignature(map, ConfigService.get("shop.payment.wx.apiSecret"));
			map.put("paySign", paySign);
			map.put("orderInfoId", orderInfo.getId() + ""); // 新订单 id

			return map; // 将此 map 返回给小程序客户端，让它来调起支付界面
		} else {
			LOGGER.warning("获取 perpayid 失败！错误代码：{0}，错误信息：{1}。", result.getError_code(), result.getReturn_msg());

			return new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;

				{
					put("isOk", false);
					put("msg", result.getReturn_msg());
				}
			};
		}
	}

	/**
	 * 小程序调起支付参数
	 * 
	 * @param userId  用户 id
	 * @param orderId 订单 id
	 * @param ip      IP 地址
	 * @return 小程序支付所需的参数
	 */
	public Map<String, ?> miniAppPay(long userId, long orderId, String ip) {
		return miniAppPay(userId, OrderService.dao.findById(orderId), ip);
	}

	/**
	 * 
	 * @param order
	 * @param mv
	 * @return
	 * @throws ServiceException
	 * @throws AlipayApiException
	 */
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

			PerpayReturn p = pcUnifiedOrder(order);

			mv.put("totalPrice", order.getTotalPrice());
			mv.put("codeUrl", p.getCode_url());

			return MvcConstant.JSP_PERFIX_WEBINF + "/shop/wxpay";
		}

		return "html:: ERROR Can't pay!";
	}

	/**
	 * 对统一下单的信息进行公用的设置
	 * 
	 * @param order 订单详情
	 */
	private static Map<String, String> commonSetUnifiedOrder(OrderInfo order) {
		Map<String, String> data = new HashMap<>();
		data.put("out_trade_no", order.getOrderNo());
		data.put("total_fee", toCent(order.getTotalPrice()));
		data.put("fee_type", "CNY");
		data.put("sign_type", "MD5");
		data.put("nonce_str", CommonUtil.getRandomString(10));
		data.put("mch_id", ConfigService.getValueAsString("shop.payment.wx.mchId"));
		data.put("notify_url", ConfigService.getValueAsString("shop.payment.wx.notifyUrl"));

		return data;
	}

	/**
	 * 统一下单接口
	 */
	private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 执行统一下单的请求
	 * 
	 * @param data 下单信息
	 * @return 响应的结果
	 */
	private static PerpayReturn sendUnifiedOrder(Map<String, String> data) {
		String xml = MapTool.mapToXml(data);
		LOGGER.info(" 请求 perpayid" + xml);

		String result = HttpBasicRequest.post(UNIFIED_ORDER_URL, xml);
		LOGGER.info(" 获取 perpayid 结果" + result);

		Map<String, String> resultMap = MapTool.xmlToMap(result);

		PerpayReturn perpayReturn = MapTool.map2Bean(MapTool.as(resultMap, v -> v == null ? null : (Object) v), PerpayReturn.class);
		return perpayReturn;
	}

	/**
	 * 生成签名. 对 map 字典排序，然后 key/value 拼接，然后加上 key，最后 md5。 注意，若含有 sign_type 字段，必须和
	 * signType 参数保持一致。
	 * 
	 * 参见：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=4_3
	 *
	 * @param data 待签名数据
	 * @param key  API 密钥，通常是商家的 API 密钥，而不是程序的密钥
	 * @return 签名
	 */
	public static String generateSignature(Map<String, String> data, String key) {
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();

		for (String k : keyArray) {
			if (k.equals("sign"))
				continue;

			String value = data.get(k);
			if (!CommonUtil.isEmptyString(value)) // 参数值为空，则不参与签名
				sb.append(k).append("=").append(value.trim()).append("&");
		}

		sb.append("key=").append(key);

		return Encode.md5(sb.toString());
	}

	/**
	 * 转换为分的字符串
	 * 
	 * @param price 单位是元
	 * @return 分的字符串
	 */
	public static String toCent(BigDecimal price) {
		return String.valueOf(price.multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).intValue());
	}
}
