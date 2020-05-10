package com.ajaxjs.weixin.mini_app;

import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.logger.LogHelper;

@Path("/api/wx")
public class MiniAppController implements IController {
	private static final LogHelper LOGGER = LogHelper.getLog(MiniAppController.class);
	MiniAppUserService service = new MiniAppUserService();

	@POST
	@Path("mini_app/login")
	@MvcFilter(filters = DataBaseFilter.class)
	@Produces(MediaType.APPLICATION_JSON)
	public String miniappLogin(@FormParam("code") String jsCode, @FormParam("userInfo") String userInfoJson) {
		LOGGER.info("小程序登录");
		UserLoginToken token = service.wxLogin(jsCode, userInfoJson);
		
		Map<String, Object> map = new HashMap<>();
		map.put("sessionId", token.getSessionId());
		map.put("userId", token.getUserId());

		return BaseController.toJson(map);
	}
}
