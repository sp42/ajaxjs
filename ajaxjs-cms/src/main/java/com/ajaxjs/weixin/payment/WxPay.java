package com.ajaxjs.weixin.payment;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.MapTool;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.CommonWxUtil;
import com.ajaxjs.weixin.mini_app.MiniApp;

/**
 * 支付方法
 * 
 * @author Frank Cheung
 *
 */
public class WxPay implements PayConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(WxPay.class);

	/**
	 * 生成统一下单用的信息
	 * 
	 * @param order 订单对象
	 * @param perpayInfo
	 * @return 统一下单用的信息
	 */
	public static Map<String, String> unifiedOrder(OrderInfo order, PerpayInfo perpayInfo) {
		LOGGER.info("微信统一下单请求交易开始");
		Map<String, String> data = new HashMap<>();

		// 字段名 变量名 必填 类型 示例值 描述 标价币种 fee_type 否 String(16) CNY 符合ISO
		// 4217标准的三位字母代码，默认人民币：CNY，详细列表请参见货币类型
		// data.put("fee_type", FEE_TYPE_CNY);

		// 通知地址 notify_url 是 String(256) http://www.weixin.qq.com/wxpay/pay.php
		// 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
		data.put("notify_url", notifyUrl);

		// 交易类型 trade_type 是 String(16) JSAPI 小程序取值如下：JSAPI，详细说明见参数规定
		data.put("trade_type", TRADE_TYPE);

		// 用户标识 openid 否 String(128) oUpF8uMuAJO_M2pxb1Q9zNjWeS6o
		// trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
		data.put("openid", perpayInfo.getOpenid());

		// 商品描述 body 是 String(128) 腾讯充值中心-QQ会员充值 商品简单描述，该字段请按照规范传递，具体请见参数规定
		data.put("body", perpayInfo.getBody());

		// 商户订单号 out_trade_no 是 String(32) 20150806125346
		// 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
		data.put("out_trade_no", order.getOrderNo());

		// 标价金额 total_fee 是 Int 88 订单总金额，单位为分，详见支付金额 默认单位为分，系统是元，所以需要*100
		data.put("total_fee", ShopHelper.toCent(order.getTotalPrice()));

		// 终端IP spbill_create_ip 是 String(16) 123.12.12.123
		// APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
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

		// 设备号 device_info 否 String(32) 013467007045764
		// 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
		// data.put("device_info", "WEB");

		// 交易起始时间 time_start 否 String(14) 20091225091010
		// 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。其他详见时间规则
		// data.put("time_start", DateTimeUtil.getTimeShortString(time_start));
		// 交易结束时间 time_expire 否 String(14) 20091227091010
		// 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。订单失效时间是针对订单号而言的，由于在请求支付的时候有一个必传参数prepay_id只有两小时的有效期，所以在重入时间超过2小时的时候需要重新请求下单接口获取新的prepay_id。其他详见时间规则,建议：最短失效时间间隔大于1分钟
		// data.put("time_expire", DateTimeUtil.getTimeShortString(time_expire));
		// 商品ID product_id 否 String(32) 12235413214070356458058
		// trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
		// data.put("product_id", null);

		// 指定支付方式 limit_pay 否 String(32) no_credit 上传此参数no_credit--可限制用户不能使用信用卡支付
		// data.put("limit_pay", null);
		// 附加数据 attach 否 String(127) 深圳分店 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
		// data.put("attach", null);

		/** 以下五个参数，在 this.fillRequestData 方法中会自动赋值 **/
		// 小程序ID appid 是 String(32) wxd678efh567hg6787 微信分配的小程序ID
		data.put("appid", MiniApp.getAppId());
		// 商户号 mch_id 是 String(32) 1230000109 微信支付分配的商户号
		data.put("mch_id", PaySignatures.getMchId());
		// 随机字符串 nonce_str 是 String(32) 随机字符串，长度要求在32位以内。
		data.put("nonce_str", CommonWxUtil.getNonceStr());
		// 签名类型 sign_type 否 String(32) MD5 签名类型，默认为MD5，支持HMAC-SHA256和MD5。
		data.put("sign_type", MD5);
		// 签名 sign 是 String(32)
		data.put("sign", PaySignatures.generateSignature(data, PaySignatures.getMchSecretId()));

		return data;
	}

	/**
	 * 获取 perpayid
	 * 
	 * @param map
	 * @return
	 */
	public static PerpayReturn sendUnifiedOrder(Map<String, String> map) {
		String xml = MapTool.mapToXml(map);
//		LOGGER.info(" 请求 perpayid" + xml);

		String result = HttpBasicRequest.post(PayConstant.unifiedorderUrl, xml);
		LOGGER.info(" 获取 perpayid 结果" + result);

		Map<String, String> resultMap = MapTool.xmlToMap(result);

		PerpayReturn perpayReturn = MapTool.map2Bean(MapTool.as(resultMap, v -> v == null ? null : (Object) v), PerpayReturn.class);

		return perpayReturn;
	}

	/**
	 * 
	 * @param orderInfo
	 * @param perpayInfo
	 * @return
	 */
	public static PerpayReturn sendUnifiedOrder(OrderInfo orderInfo, PerpayInfo perpayInfo) {
		return sendUnifiedOrder(unifiedOrder(orderInfo, perpayInfo));
	}
}
