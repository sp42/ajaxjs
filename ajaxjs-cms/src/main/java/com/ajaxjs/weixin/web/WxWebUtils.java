package com.ajaxjs.weixin.web;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.weixin.CommonWxUtil;

public class WxWebUtils {
	public final static String accessTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	/**
	 * Access_token 是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。
	 * 1、access_token的存储至少要保留512个字符空间 2、access_token的有效期目前为2个小时，需定时刷新
	 * 3、重复获取将导致上次获取的access_token失效
	 * 
	 * https://mp.weixin.qq.com/wiki?action=doc&id=mp1421140183
	 * 
	 * {"errcode":40164,"errmsg":"invalid ip 119.33.28.152, not in whitelist hint:
	 * [g_G8404063055]"}
	 * 
	 * @param appId
	 * @param appSecret
	 * @return
	 */
	public static AccessToken getAccessToken(String appId, String appSecret) {
		AccessToken accessToken = null;
		String jsonStr = NetUtil.simpleGET(String.format(accessTokenApi, appId, appSecret));

		if (jsonStr != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> result = (Map<String, Object>) JsonHelper.parse(jsonStr);

			if (result.get("errmsg") != null) {
				throw new NullPointerException(result.get("errmsg").toString());
			} else {
//				accessToken.setToken(result.get("access_token").toString());
//				accessToken.setExpiresIn((int) result.get("expires_in"));
			}
		} else {
			throw new NullPointerException("通讯腾讯  AccessToken API 接口失败");
		}

		return accessToken;
	}

	public final static String ticketApi = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

	public static JsApiTicket getJsApiTicket(String accessToken) {
		JsApiTicket ticket = null;
		String jsonStr = NetUtil.simpleGET(String.format(ticketApi, accessToken));

		if (jsonStr != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> result = (Map<String, Object>) JsonHelper.parse(jsonStr);

			if (!"ok".equals(result.get("errmsg").toString())) {
				throw new NullPointerException(result.get("errmsg").toString());
			} else {
				ticket = new JsApiTicket();
				ticket.setTicket(result.get("ticket").toString());
				ticket.setExpiresIn((int) result.get("expires_in"));

				long e = System.currentTimeMillis() + (ticket.getExpiresIn() * 1000); // 设置过期时间
				Calendar ever = Calendar.getInstance();
				ever.setTime(CommonUtil.Objet2Date(e));
				ticket.setExpiresDate(ever);
			}
		} else {
			throw new NullPointerException("通讯腾讯 JsApiTicket API 接口失败");
		}

		return ticket;
	}

	// 创建缓存
	private static JsApiTicket jsApiTicket;

	private static AccessToken accessToken;

	public static JsApiTicket getJsApiTicket(String appId, String appSecret) {
		if (jsApiTicket == null) {
			accessToken = getAccessToken(appId, appSecret);
			jsApiTicket = getJsApiTicket(accessToken.getToken());
			// 第一次获取
		} else {
			// 重新获取
			if (!jsApiTicket.getExpiresDate().after(Calendar.getInstance())) {
				accessToken = getAccessToken(appId, appSecret);
				jsApiTicket = getJsApiTicket(accessToken.getToken());
			}
		}

		return jsApiTicket;
	}

	public static Map<String, String> generateSignature(String url, String jsApiTicket) {
		Map<String, String> map = new HashMap<>();
		map.put("url", url);
		map.put("jsapi_ticket", jsApiTicket);
		map.put("noncestr", CommonWxUtil.getNonceStr());
		map.put("timestamp", CommonWxUtil.getTimeStamp());
		
		String raw = generateSignature(map);
		
		map.put("signature", Encode.getSHA1(raw));

		return map; // 因为签名用的 noncestr 和 timestamp 必须与 wx.config中的nonceStr 和 timestamp
					// 相同，所以还需要使用这两个参数
	}

	/**
	 * 字段名的ASCII 码从小到大排序（字典序）后，使用URL键值对的格式（即key1=value1&key2=value2…）拼接成字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String generateSignature(Map<String, String> data) {
		Set<String> keySet = data.keySet();
		String[] keyArray = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keyArray);
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (String k : keyArray) {
			if (data.get(k) != null && data.get(k).trim().length() > 0) // 参数值为空，则不参与签名
				sb.append(k).append("=").append(data.get(k).trim()).append(++i < data.size() ? "&" : "");
		}

		return sb.toString();
	}
}
