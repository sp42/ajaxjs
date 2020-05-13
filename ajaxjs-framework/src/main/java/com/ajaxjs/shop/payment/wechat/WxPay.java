package com.ajaxjs.shop.payment.wechat;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.net.http.Tools;
import com.ajaxjs.shop.dao.OrderInfoDao;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.wechat.model.PerpayInfo;
import com.ajaxjs.shop.payment.wechat.model.PerpayReturn;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.user.token.TokenService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.payment.PayConstant;

/**
 * 微信支付方法
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class WxPay implements PayConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(WxPay.class);

	/**
	 * PC 版支付微信下单接口
	 * 
	 * @param order
	 * @return
	 */
	public static PerpayReturn pcUnifiedOrder(OrderInfo order) {
		LOGGER.info("PC 微信下单请求交易开始");

		Map<String, String> data = new HashMap<>();
		WxUtil.commonSetUnifiedOrder(order, data);

		data.put("body", "TESTTEST");
		data.put("product_id", order.getId() + ""); // 商品 id，但一个订单可能包含多个商品，所以填入订单数据库 id
		data.put("notify_url", ConfigService.getValueAsString("shop.payment.wx.notifyUrl"));
		data.put("appid", ConfigService.getValueAsString("shop.payment.wx.appId"));
		data.put("mch_id", ConfigService.getValueAsString("shop.payment.wx.mchId"));

		data.put("trade_type", "NATIVE");
		data.put("device_info", "web");
		data.put("spbill_create_ip", Tools.getIp());

		data.put("sign", WxUtil.generateSignature(data, ConfigService.getValueAsString("shop.payment.wx.apiSecret")));

		return WxUtil.sendUnifiedOrder(data);
	}

	/**
	 * 小程序的生成统一下单用的信息
	 * 
	 * @param order      订单对象
	 * @param perpayInfo
	 * @return 统一下单用的信息
	 */
	public static Map<String, String> unifiedOrder(OrderInfo order, PerpayInfo perpayInfo) {
		LOGGER.info("微信统一下单请求交易开始");

		Map<String, String> data = new HashMap<>();
		WxUtil.commonSetUnifiedOrder(order, data);

		data.put("notify_url", notifyUrl);
		data.put("trade_type", "JSAPI");
		data.put("openid", perpayInfo.getOpenid());

		// 商品描述 body 是 String(128) 腾讯充值中心-QQ会员充值 商品简单描述，该字段请按照规范传递，具体请见参数规定
		data.put("body", perpayInfo.getBody());
		data.put("spbill_create_ip", perpayInfo.getIp());

		/** 以下参数为非必填参数 **/
		// 订单优惠标记 goods_tag 否 String(32) WXG 订单优惠标记，使用代金券或立减优惠功能时需要的参数，说明详见代金券或立减优惠
		if (!CommonUtil.isEmptyString(perpayInfo.getGoods_tag()))
			data.put("goods_tag", perpayInfo.getGoods_tag());

		// 商品详情 detail 否 String(6000) 商品详细描述，对于使用单品优惠的商户，改字段必须按照规范上传，详见“单品优惠参数说明”
		if (!CommonUtil.isEmptyString(perpayInfo.getDetail())) {
			data.put("detail", perpayInfo.getDetail());
			// 接口版本号
			// 新增字段，接口版本号，区分原接口，默认填写1.0。入参新增version后，则支付通知接口也将返回单品优惠信息字段promotion_detail，请确保支付通知的签名验证能通过。
			data.put("version", "1.0");
		}

		data.put("appid", ConfigService.getValueAsString("mini_program.appId"));
		// 商户号 mch_id 是 String(32) 1230000109 微信支付分配的商户号
		data.put("mch_id", ConfigService.getValueAsString("mini_program.MchId"));
		data.put("sign", WxUtil.generateSignature(data, ConfigService.getValueAsString("mini_program.MchSecretId")));

		return data;
	}

	/**
	 * 
	 * @param orderInfo
	 * @param perpayInfo
	 * @return
	 */
	public static PerpayReturn sendUnifiedOrder(OrderInfo orderInfo, PerpayInfo perpayInfo) {
		return WxUtil.sendUnifiedOrder(unifiedOrder(orderInfo, perpayInfo));
	}

	/**
	 * 小程序调起支付参数
	 * 
	 * @param perpayId
	 * @param nonceStr
	 * @return
	 */
	public static Map<String, String> getPayParam(String perpayId, String nonceStr) {
		Map<String, String> map = new HashMap<>();
		map.put("appId", ConfigService.getValueAsString("mini_program.appId"));
		map.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		map.put("nonceStr", TokenService.getRandomString(10));
		map.put("package", "prepay_id=" + perpayId);
		map.put("signType", "MD5");

		String paySign = WxUtil.generateSignature(map, ConfigService.getValueAsString("mini_program.MchSecretId"));
		map.put("paySign", paySign);

		return map;
	}

	@Resource("OrderService")
	private OrderService orderService;

	/**
	 * 生成订单并支付
	 * 
	 * @param userId    用户 id
	 * @param addressId 送货地址id
	 * @param cartIds   订单明细由购物车的数据生成
	 * @return
	 */
	public Map<String, ?> createOrderAndPay(long userId, long addressId, String[] cartIds, String ip) {
		OrderInfo orderInfo = orderService.processOrder(userId, addressId, cartIds, 0);
		return wxPay(userId, orderInfo, ip);
	}

	public Map<String, ?> pay(long userId, long orderId, String ip) {
		return wxPay(userId, dao.findById(orderId), ip);
	}

	public static OrderInfoDao dao = new Repository().bind(OrderInfoDao.class);

	/**
	 * 核心支付方法
	 * 
	 * @param userId
	 * @param orderInfo
	 * @param ip
	 * @return
	 */
	public static Map<String, ?> wxPay(long userId, OrderInfo orderInfo, String ip) {
		String openId = dao.findUserOpenId(userId); // 小程序内调用登录接口，获取到用户的 openid

		// 预付单
		PerpayInfo perpayInfo = new PerpayInfo();
		perpayInfo.setOpenid(openId);
		perpayInfo.setBody("集结号-网超");
		perpayInfo.setIp(ip);

//		Map<String, String> map = WxPay.unifiedOrder(orderInfo, perpayInfo);
//		PerpayReturn result = WxPay.sendUnifiedOrder(map); 

		PerpayReturn result = sendUnifiedOrder(orderInfo, perpayInfo);// 商户 server 调用支付统一下单

		if (result.isSuccess()) {
			LOGGER.info("获取 perpayid 成功！{0}", result.getPrepay_id());
//			Map<String, String> r = PaySignatures.getPayParam(result.getPrepay_id(), map.get("nonce_str")); // 商户 server 调用再次签名
			Map<String, String> r = getPayParam(result.getPrepay_id(), result.getNonce_str()); // 商户
																								// server
																								// 调用再次签名
			r.put("orderInfoId", orderInfo.getId() + ""); // 新订单 id

			return r; // 将此 map 返回给小程序客户端，让它来调起支付界面
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
}
