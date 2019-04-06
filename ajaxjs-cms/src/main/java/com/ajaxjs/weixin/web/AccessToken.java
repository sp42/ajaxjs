package com.ajaxjs.weixin.web;

import java.util.Calendar;
import java.util.Map;

import com.ajaxjs.js.JsonHelper;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.weixin.mini_app.MiniApp;

/**
 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
 * 
 * @author Frank Cheung
 *
 */
public class AccessToken {

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
	public AccessToken(String appId, String appSecret) {
		setAppId(appId);
		setAppSecret(appSecret);

		load(); // 马上获取
	}

	public void load() {
		String jsonStr = NetUtil.simpleGET(String.format(accessTokenApi, appId, appSecret));

		if (jsonStr != null) {
			@SuppressWarnings("unchecked")
			Map<String, Object> result = (Map<String, Object>) JsonHelper.parse(jsonStr);

			if (result.get("errmsg") != null) {
				throw new NullPointerException(result.get("errmsg").toString());
			} else {
				setToken(result.get("access_token").toString());
				setExpiresIn((int) result.get("expires_in"));

				long e = System.currentTimeMillis() + (getExpiresIn() * 1000); // 设置过期时间
				Calendar ever = Calendar.getInstance();
				ever.setTime(CommonUtil.Objet2Date(e));
				setExpiresDate(ever);
			}
		} else {
			throw new NullPointerException("通讯腾讯  AccessToken API 接口失败");
		}
	}

	/**
	 * 获取小程序的 appId 和 appSecret
	 * 
	 * @return
	 */
	public static AccessToken miniP_factory() {
		String appId = MiniApp.getAppId(), appSecret = MiniApp.getAppSecret();
		return new AccessToken(appId, appSecret);
	}

	/**
	 * 生成 JS-SDK 使用权限签名
	 */
//	public void initJsApiTicket() {
//		String jsonStr = NetUtil.simpleGET(String.format(ticketApi, getToken()));
//
//		if (jsonStr != null) {
//			@SuppressWarnings("unchecked")
//			Map<String, Object> result = (Map<String, Object>) JsonHelper.parse(jsonStr);
//
//			if (!"ok".equals(result.get("errmsg").toString())) {
//				throw new NullPointerException(result.get("errmsg").toString());
//			} else {
//				setJsApiTicket(result.get("ticket").toString());
//			}
//		} else {
//			throw new NullPointerException("通讯腾讯 JsApiTicket API 接口失败");
//		}
//	}

	public final static String accessTokenApi = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	private String appId;

	private String appSecret;

	// 获取到的凭证
	private String token;

	// 凭证有效时间，单位：秒
	private int expiresIn;

	private Calendar expiresDate; // 超时时间

	public int getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(int expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Calendar getExpiresDate() {
		return expiresDate;
	}

	public void setExpiresDate(Calendar expiresDate) {
		this.expiresDate = expiresDate;
	}

	public String getToken() {
		if (Calendar.getInstance().after(getExpiresDate())) {
			// 超时了，重新获取
			load();
//			System.out.println("重新获取");
		}

		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getJsApiTicket() {
		return jsApiTicket;
	}

	public void setJsApiTicket(String jsApiTicket) {
		this.jsApiTicket = jsApiTicket;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	/**
	 * 公众号用
	 */
	private String jsApiTicket;
}
