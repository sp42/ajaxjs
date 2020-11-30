package com.ajaxjs.user.login;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.controller.IController;

@Path("/user/thirdpartrybinding")
public class ThirdLogin implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(ThirdLogin.class);

	private final static String html = 
			"<table width=\"100%%\" style=\"height: 100%%;\"><tr><td valign=\"middle\" align=\"center\"><h3>%s</h3>三秒后关闭窗口</td></tr></table><script>setTimeout(()=>{" 
			+ "window.opener.window.aj.OauthLoginInstance.result = ' %s ';"		 // 把返回的数据传给父窗口
			+ "window.opener.window.aj.OauthLoginInstance.closeWin();}, 3000);</script>";// 授权完成后，关闭子窗口
	@GET
	@Path("weibo_callback")
	public String callback(@NotNull @QueryParam("code") String code, @NotNull @QueryParam("state") String state) {
		LOGGER.info("code::::::::::" + code);
		LOGGER.info("state::::::::::" + state);
		
		// 获取token
		Map<String, Object> map = Weibo.getAccessToken(code);
		String accessToken = map.get("access_token").toString(), uid = map.get("uid").toString();
		int expires = (int) map.get("expires_in");
		
		System.out.println(expires);
		
		LOGGER.info("accessToken::::::::::" + accessToken);

		Map<String, Object> userInfoMap = Weibo.getUserInfo(accessToken, uid);
		LOGGER.info("userInfoMap::::::::::" + userInfoMap);
		String msg  = "登录成功";

		return "html::" + String.format(html, msg, msg);
	}

	@GET
	@Path("weibo")
	@Produces(MediaType.APPLICATION_JSON)
	public String weibo(@NotNull @QueryParam("code") String code, @NotNull @QueryParam("state") String state) {
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
