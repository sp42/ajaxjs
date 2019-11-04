package com.ajaxjs.user.controller;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.user.login.Weibo;
import com.ajaxjs.util.logger.LogHelper;

@Path("/user/thirdpartrybinding")
public class ThirdLogin implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(ThirdLogin.class);

	@GET
	@Path("weixin")
	@Produces(MediaType.APPLICATION_JSON)
	public String weixin(@NotNull @QueryParam("code") String code, @NotNull @QueryParam("state") String state) {
		LOGGER.info("code::::::::::" + code);
		LOGGER.info("state::::::::::" + state);

		if ("register".equals(state)) {
			// 获取token
			Map<String, Object> map = Weibo.getAccessToken(code);
			String accessToken = map.get("access_token").toString(), uid = map.get("uid").toString();
//			int expires = (int) map.get("expires_in");

			LOGGER.info("accessToken::::::::::" + accessToken);

			Map<String, Object> userInfoMap = Weibo.getUserInfo(accessToken, uid);
			LOGGER.info("userInfoMap::::::::::" + userInfoMap);

		} else {
			// result = "微博授权失败";
		}
		return "json::{'OK':true}";
	}
}
