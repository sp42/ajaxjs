package com.ajaxjs.wechat.applet;

import java.util.Map;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.wechat.applet.model.WeChatAppletConfig;

/**
 * 小程序的一些方法
 * 
 * @author Frank Cheung
 *
 */
public class App {
	private static final LogHelper LOGGER = LogHelper.getLog(App.class);

	private final static String TOKEN_API = "https://api.weixin.qq.com/cgi-bin/token";

	/**
	 * 获取 Client AccessToken
	 * 
	 * @param cfg
	 */
	public static void getAccesToken(WeChatAppletConfig cfg) {
		LOGGER.info("获取 Client AccessToken");
		String params = String.format("?grant_type=client_credential&appid=%s&secret=%s", cfg.getAccessKeyId(), cfg.getAccessSecret());
		Map<String, Object> map = Get.api(TOKEN_API + params);

		if (map.containsKey("access_token")) {
			cfg.setAccessToken(map.get("access_token").toString());
			LOGGER.warning("获取令牌成功！ AccessToken [{0}]", cfg.getAccessToken());
		} else if (map.containsKey("errcode"))
			LOGGER.warning("获取令牌失败！ Error [{0}:{1}]", map.get("errcode"), map.get("errmsg"));
		else
			LOGGER.warning("获取令牌失败！未知异常 [{0}]", map);
	}
}
