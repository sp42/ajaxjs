package com.ajaxjs.user.oauth;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;

public class WeiboOauthController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(WeiboOauthController.class);

	/**
	 * 获取 Token
	 */
	private final static String GET_TOKEN = "https://api.weibo.com/oauth2/access_token";

	/**
	 * 获取用户详情
	 */
	private final static String GET_USER_INFO = "https://api.weibo.com/2/users/show.json??access_token=%s&uid=%s";

	@GET
	@Path("weibo")
	@Produces(MediaType.APPLICATION_JSON)
	public String callback(@NotNull @QueryParam("code") String code, @NotNull @QueryParam("state") String state) {
		LOGGER.info("code::::::::::" + code);
		LOGGER.info("state::::::::::" + state);
		// code = Authorization Code 

		if ("register".equals(state)) {
			// 获取token

			StringBuilder sb = new StringBuilder();
			sb.append("grant_type=authorization_code");
			sb.append("&client_id=" + ConfigService.getValueAsString("user.oauth.weibo.clientId"));
			sb.append("&client_secret=" + ConfigService.getValueAsString("user.oauth.weibo.clientSercret"));
			sb.append("&redirect_uri=" + ConfigService.getValueAsString("user.oauth.weibo.callback"));
			sb.append("&code=" + code);

			LOGGER.info("获取 AccessToken ：" + sb.toString());
			String result = NetUtil.post(GET_TOKEN, sb.toString());
			LOGGER.info("AccessToken 接口返回：" + result);

			/**
			 * 返回数据 { "access_token": "ACCESS_TOKEN", "expires_in": 1234,
			 * "remind_in":"798114", "uid":"12341234" }
			 */
			if (result != null) {
				WeiboToken token = JsonHelper.parseMapAsBean(result, WeiboToken.class);
				LOGGER.info("accessToken::::::::::" + token.getAccess_token());
//			Map<String, Object> userInfoMap = getUserInfo(accessToken, uid);
			}

		} else {
			// result = "微博授权失败";
		}

		return "json::{'OK':true}";
	}

	/**
	 * 获取用户信息。 返回参数文档 http://open.weibo.com/wiki/2/users/show
	 * 
	 * @param accessToken 微博用户登录的唯一票据
	 * @param uid         查询的用户ID
	 * @return 用户信息
	 */
	private static Map<String, Object> getUserInfo(String accessToken, String uid) {
		LOGGER.info("获取用户信息 uid:" + uid);

		String result = NetUtil.simpleGET(String.format(GET_USER_INFO, accessToken, uid));

		if (CommonUtil.isEmptyString(result))
			throw new NullPointerException("访问微博 GET_USER_INFO 接口失败");

		Map<String, Object> map = JsonHelper.parseMap(result);

		Object errCode = map.get("error_code");
		if (errCode != null && (int) errCode != 0)
			throw new NullPointerException("访问微博 GET_USER_INFO 错误： " + errCode + " " + map.get("error") + " " + map.get("error_description"));

		return map;
	}
}
