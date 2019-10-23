 package com.ajaxjs.user.controller;

import java.util.HashMap;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 注册控制器。为方便使用，它继承与登录控制器，但它们的关系的平等的。只是为了 Java的单根继承
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractRegisterController extends AbstractLoginController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractRegisterController.class);
	
	@Resource("AliyunSMSSender")
	private SMS sms;
	
	@GET
	@Path("/register")
	public String regsiter() {
		LOGGER.info("用户注册页");
		
		return jsp("user/register");
	}
	
	@POST
	@Path("/register")
	@MvcFilter(filters = { DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String doRegister(User user, @NotNull @QueryParam("password") String password) throws ServiceException {
		LOGGER.info("正在注册");
		
		registerByPhone(user, password);
		
		return jsonOk("恭喜你，注册成功");
	}

//	@POST
//	@MvcFilter(filters = {  DataBaseFilter.class })
//	public String doRegister(User user, @NotNull @QueryParam("password") String password) throws ServiceException {
//		registerByPhone(user, password);
//
//		return "/user/index.jsp?msg=" + Encode.urlEncode("恭喜你，注册成功！<a href=\"../user/login/\">马上登录</a>") ;
//	}

	/**
	 * 检查是否重复的手机号码
	 * 
	 * @param phone 手机号码
	 * @return true=已存在
	 */
	@GET
	@Path("/checkIfUserPhoneRepeat")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String checkIfUserPhoneRepeat(@NotNull @QueryParam("phone") String phone) {
		LOGGER.info("检查是否重复的手机号码：" + phone);

		return toJson(new HashMap<String, Boolean>() {
			private static final long serialVersionUID = -5033049204280154615L;
			{
				put("isRepeat", getService().checkIfUserPhoneRepeat(phone));
			}
		});
	}

	@POST
	@Path("sendSMScode")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendSMScode(@QueryParam("phoneNo") String phoneNo) {
		return super.sendSMScode(phoneNo);
	}

	private void registerByPhone(User user, String password) throws ServiceException {
		LOGGER.info("执行用户注册");

		if (password == null)
			throw new IllegalArgumentException("注册密码不能为空");

		UserCommonAuth passwordModel = new UserCommonAuth();
		passwordModel.setPhone_verified(1); // 已验证
		passwordModel.setPassword(password);

		long id = getService().register(user, passwordModel);
		LOGGER.info("创建用户成功，id：" + id);
	}

}
