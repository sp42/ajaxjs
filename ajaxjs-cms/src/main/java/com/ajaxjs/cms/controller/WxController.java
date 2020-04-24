package com.ajaxjs.cms.controller;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.mini_app.MiniAppUserService;

@Path("/api/wx")
public class WxController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(WxController.class);
	MiniAppUserService service = new MiniAppUserService();

	@POST
	@Path("mini_app/login")
	@Produces(MediaType.APPLICATION_JSON)
	public String miniappLogin(@FormParam("code") String jsCode, @FormParam("userInfo") String userInfoJson) {
		LOGGER.info("小程序登录");
		service.wxLogin(jsCode, userInfoJson);
		return "";
	}
}
