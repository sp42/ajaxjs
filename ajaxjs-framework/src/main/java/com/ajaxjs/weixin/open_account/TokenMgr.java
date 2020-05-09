package com.ajaxjs.weixin.open_account;

import java.util.Calendar;

import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.weixin.open_account.model.AccessToken;
import com.ajaxjs.weixin.open_account.model.BaseModel;
import com.ajaxjs.weixin.open_account.model.JsApiTicket;

/**
 * 公众号 Token 管理器，应设为单例
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class TokenMgr {
	private static final LogHelper LOGGER = LogHelper.getLog(TokenMgr.class);
	
	public static TokenMgr instance;
	
	private String appId;

	private String appSecret;

	/**
	 * AccessToken 凭证实体
	 */
	private AccessToken token;

	/**
	 * Access_token 是公众号的全局唯一票据，公众号调用各接口时都需使用access_token。
	 * 1、access_token的存储至少要保留512个字符空间 2、access_token的有效期目前为2个小时，需定时刷新
	 * 3、重复获取将导致上次获取的access_token失效
	 * 
	 * https://mp.weixin.qq.com/wiki?action=doc&id=mp1421140183
	 * 
	 * {"access_token":"ACCESS_TOKEN","expires_in":7200}
	 * 
	 * 错误的例子：{"errcode":40164,"errmsg":"invalid ip 119.33.28.152, not in whitelist
	 * hint: [g_G8404063055]"}
	 * 
	 * @param appId
	 * @param appSecret
	 */
	public TokenMgr(String appId, String appSecret) {
		this.appId = appId;
		this.appSecret = appSecret;
	}

	/**
	 * 通用的解析过程，可判断错误和记录超时
	 * 
	 * @param <T>     实体类
	 * @param jsonStr 服务端响应的 JSON 字符串
	 * @param beanClz 实体类
	 * @return Bean
	 */
	private static <T extends BaseModel> T init(String jsonStr, Class<T> beanClz) {
		if (jsonStr != null) {
			T token = JsonHelper.parseMapAsBean(jsonStr, beanClz);

			if (token.getErrcode() != 0) {
				LOGGER.warning("错误：" + token.getErrmsg());
				throw new NullPointerException(token.getErrmsg());
			} else {
				long e = System.currentTimeMillis() + (token.getExpires_in() * 1000); // 设置过期时间
				Calendar ever = Calendar.getInstance();
				ever.setTime(CommonUtil.Objet2Date(e));
				token.setExpiresDate(ever);
			}

			return token;
		} else {
			throw new NullPointerException("通讯腾讯  API 接口失败");
		}
	}

	private final static String ACCESS_TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";

	/**
	 * 获取 AccessToken
	 */
	public void initAccessToken() {
		LOGGER.info("获取 AccessToken");
		String jsonStr = NetUtil.simpleGET(String.format(ACCESS_TOKEN_API, appId, appSecret));
		token = init(jsonStr, AccessToken.class);
	}

	private final static String TICKET_API = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";

	/**
	 * 生成 JS-SDK 使用权限签名。
	 */
	public void initTicket() {
		LOGGER.info("获取 JSAPI Ticket");
		String jsonStr = NetUtil.simpleGET(String.format(TICKET_API, getToken()));
		jsApiTicket = init(jsonStr, JsApiTicket.class);
	}

	private JsApiTicket jsApiTicket;

	/**
	 * 获取 AccessToken，如果超过有效期，自动刷新
	 * 
	 * @return AccessToken
	 */
	public String getToken() {
		if (token == null || Calendar.getInstance().after(token.getExpiresDate())) {
			initAccessToken();// 超时了，重新获取
		}

		return token.getAccess_token();
	}

	/**
	 * 获取 JsApiTicket，如果超过有效期，自动刷新
	 * 
	 * @return JsApiTicket
	 */
	public String getTicket() {
		if (jsApiTicket == null || Calendar.getInstance().after(jsApiTicket.getExpiresDate())) {
			initTicket();
		}

		return jsApiTicket.getTicket();
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
}
