package com.ajaxjs.shop.payment.wechat;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.net.http.HttpBasicRequest;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.wechat.model.PerpayReturn;
import com.ajaxjs.user.token.TokenService;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;

public class WxUtil {
	private static final LogHelper LOGGER = LogHelper.getLog(WxUtil.class);

	/**
	 * 对统一下单的信息进行公用的设置
	 * 
	 * @param data 下单信息
	 */
	public static void commonSetUnifiedOrder(OrderInfo order, Map<String, String> data) {
		data.put("out_trade_no", order.getOrderNo());
		data.put("total_fee", ShopHelper.toCent(order.getTotalPrice()));

		data.put("fee_type", "CNY");
		data.put("nonce_str", TokenService.getRandomString(10));
		data.put("sign_type", "MD5");
	}

	/**
	 * 生成签名. 对 map 字典排序，然后 key/value 拼接，然后加上 key，最后 md5。 注意，若含有 sign_type 字段，必须和
	 * signType 参数保持一致。
	 * 参见：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=4_3
	 *
	 * @param data 待签名数据
	 * @param key  API密钥
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

			if (data.get(k) != null && data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
				sb.append(k).append("=").append(data.get(k).trim()).append("&");
		}

		sb.append("key=").append(key);

		return Encode.md5(sb.toString());
	}

	/**
	 * 统一下单接口
	 */
	public static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

	/**
	 * 执行统一下单的请求
	 * 
	 * @param data 下单信息
	 * @return 响应的结果
	 */
	public static PerpayReturn sendUnifiedOrder(Map<String, String> data) {
		String xml = MapTool.mapToXml(data);
		LOGGER.info(" 请求 perpayid" + xml);

		String result = HttpBasicRequest.post(UNIFIED_ORDER_URL, xml);
		LOGGER.info(" 获取 perpayid 结果" + result);

		Map<String, String> resultMap = MapTool.xmlToMap(result);

		PerpayReturn perpayReturn = MapTool.map2Bean(MapTool.as(resultMap, v -> v == null ? null : (Object) v),
				PerpayReturn.class);
		return perpayReturn;
	}
}
