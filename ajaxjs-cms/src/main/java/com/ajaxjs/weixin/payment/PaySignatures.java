package com.ajaxjs.weixin.payment;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.util.Encode;
import com.ajaxjs.weixin.CommonWxUtil;

public final class PaySignatures implements PayConstant {

	/**
	 * 返回商户 id
	 * 
	 * @return
	 */
	public static String getMchId() {
		return ConfigService.getValueAsString("mini_program.MchId");
	}

	/**
	 * 返回商户密钥
	 * 
	 * @return
	 */
	public static String getMchSecretId() {
		return ConfigService.getValueAsString("mini_program.MchSecretId");
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

	/**
	 * 生成签名. 对 map 字典排序，然后 key/value 拼接，然后加上 key，最后 md5 注意，若含有 sign_type 字段，必须和
	 * signType 参数保持一致。
	 * 参见：https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=4_3
	 *
	 * @param data 待签名数据
	 * @param key API密钥
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
	 * 小程序调起支付参数
	 * 
	 * @param perpayId
	 * @param nonceStr
	 * @return
	 */
	public static Map<String, String> getPayParam(String perpayId, String nonceStr) {
		Map<String, String> map = new HashMap<>();
		map.put("appId", CommonWxUtil.getAppId());
		map.put("timeStamp", CommonWxUtil.getTimeStamp());
		map.put("nonceStr", CommonWxUtil.getNonceStr());
		map.put("package", "prepay_id=" + perpayId);
		map.put("signType", "MD5");

		String paySign = generateSignature(map, getMchSecretId());
		map.put("paySign", paySign);

		return map;
	}
}
