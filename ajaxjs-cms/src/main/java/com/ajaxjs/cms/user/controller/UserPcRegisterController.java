package com.ajaxjs.cms.user.controller;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.user.User;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 用户注册控制器
 * 
 * @author Sp42 frank@ajaxjs.com
 */
@Path("/user/register")
@Bean("PcUserRegisterController")
public class UserPcRegisterController extends AbstractRegisterController {
	private static final LogHelper LOGGER = LogHelper.getLog(UserPcRegisterController.class);

	@Resource("UserService") // 指定 service id
	private UserService service;

	@Resource("AliyunSMSSender")
	private SMS sms;

	@GET
	public String register() {
		LOGGER.info("进入注册页面");
		return jsp("user/register");
	}

	@POST
	@MvcFilter(filters = {  DataBaseFilter.class })
	public String doRegister(User user, @NotNull @QueryParam("password") String password) throws ServiceException {
		registerByPhone(user, password);

		return "/user/index.jsp?msg=" + Encode.urlEncode("恭喜你，注册成功！<a href=\"../user/login/\">马上登录</a>") ;
	}

	@GET
	@Path("/checkIfUserPhoneRepeat")
	@MvcFilter(filters = { DataBaseFilter.class })
	@Override
	public String checkIfUserPhoneRepeat(@NotNull @QueryParam("phone") String phone) {
		return super.checkIfUserPhoneRepeat(phone);
	}

	@POST
	@Path("sendSMScode")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendSMScode(@QueryParam("phoneNo") String phoneNo) {
		return super.sendSMScode(phoneNo);
	}

	@Override
	public UserService getService() {
		return service;
	}
}
