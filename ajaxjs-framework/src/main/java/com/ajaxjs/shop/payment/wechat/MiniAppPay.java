package com.ajaxjs.shop.payment.wechat;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.wechat.model.PerpayInfo;
import com.ajaxjs.shop.payment.wechat.model.PerpayReturn;
import com.ajaxjs.user.token.TokenService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.mini_app.MiniApp;
import com.ajaxjs.weixin.payment.PayConstant;

/**
 * 小程序的微信支付方法
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class MiniAppPay implements PayConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(MiniAppPay.class);

	/**
	 * 生成统一下单用的信息
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

		/** 以下五个参数，在 this.fillRequestData 方法中会自动赋值 **/
		// 小程序ID appid 是 String(32) wxd678efh567hg6787 微信分配的小程序ID
		data.put("appid", MiniApp.getAppId());
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
		map.put("appId", MiniApp.getAppId());
		map.put("timeStamp", System.currentTimeMillis() / 1000 + "");
		map.put("nonceStr", TokenService.getRandomString(10));
		map.put("package", "prepay_id=" + perpayId);
		map.put("signType", "MD5");
	
		String paySign = WxUtil.generateSignature(map, ConfigService.getValueAsString("mini_program.MchSecretId"));
		map.put("paySign", paySign);
	
		return map;
	}
}
