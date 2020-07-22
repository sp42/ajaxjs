package com.ajaxjs.weixin.mini_app;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.controller.IController;
import com.ajaxjs.web.mvc.filter.MvcFilter;

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
		return BaseController.toJson(service.wxLogin(jsCode, userInfoJson));
	}
}
