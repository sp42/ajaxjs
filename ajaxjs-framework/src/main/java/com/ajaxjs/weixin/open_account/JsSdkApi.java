package com.ajaxjs.weixin.open_account;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;

/**
 * 
 * 文档 https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/JS-SDK.html
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class JsSdkApi {
	public static void init(HttpServletRequest request) {
		if (TokenMgr.instance == null) {
			String appId = ConfigService.getValueAsString("wx_open.appId");
			String appSecret = ConfigService.getValueAsString("wx_open.appSecret");
			TokenMgr.instance = new TokenMgr(appId, appSecret);
		}

		// 获取当前页面的 url
		String url = request.getScheme() + "://" + request.getServerName() + request.getRequestURI();
		if (request.getQueryString() != null)
			url += "?" + request.getQueryString();

		Map<String, String> map = generateSignature(url, TokenMgr.instance.getTicket());
		request.setAttribute("map", map);
	}

	/**
	 * 
	 * @param url         页面地址
	 * @param jsApiTicket 凭证
	 * @return 页面用的数据
	 */
	private static Map<String, String> generateSignature(String url, String jsApiTicket) {
		Map<String, String> map = new HashMap<>();
		map.put("url", url);
		map.put("jsapi_ticket", jsApiTicket);
		map.put("noncestr", CommonUtil.getRandomString(10));
		map.put("timestamp", System.currentTimeMillis() / 1000 + "");
		map.put("signature", generateSignature(map));

		return map; // 因为签名用的 noncestr 和 timestamp 必须与 wx.config中的nonceStr 和 timestamp
					// 相同，所以还需要使用这两个参数
	}

	/**
	 * 字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串
	 * 
	 * @param data
	 * @return 签名
	 */
	private static String generateSignature(Map<String, String> data) {
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (String k : keyArray) {
			String v = data.get(k);
			if (!CommonUtil.isEmptyString(v)) // 参数值为空，则不参与签名
				sb.append(k).append("=").append(v.trim()).append(++i < data.size() ? "&" : "");
		}

		return Encode.getSHA1(sb.toString());
	}
}
