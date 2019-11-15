package com.ajaxjs.user.controller;

import java.util.HashMap;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.login.LoginService;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.captcha.CaptchaController;
import com.ajaxjs.web.captcha.CaptchaFilter;

/**
 * 注册控制器。为方便使用，它继承与登录控制器，但它们的关系的平等的。只是为了 Java的单根继承
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractUserController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractUserController.class);

	@GET
	@Path("login")
	public String login(ModelAndView mv) {
		LOGGER.info("用户登录页");

		LoginService.setUserId(mv);
		mv.put(CaptchaController.CAPTCHA_CODE, CaptchaController.CAPTCHA_CODE);

		return jsp("user/login");
	}

	@POST
	@Path("login")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String loginByPassword(@NotNull @QueryParam("userID") String userID,
			@NotNull @QueryParam("password") String password, HttpServletRequest req) throws ServiceException {
		LOGGER.info("执行登录（按密码的）");
		if(isLogined())
			return jsonNoOk("你已经登录，无须重复登录！");

		userID = userID.trim();
		User user = LoginService.loginByPassword(userID, password);

		if (user != null) {
			LoginService.saveLoginLog(user, req);
			
			if (getAfterLoginCB() != null) 
				getAfterLoginCB().accept(user, req);
			
			LoginService.afterLogin(user, req);
			
			String msg = user.getName() == null ? user.getPhone() : user.getName();

			return jsonOk("用户 " + msg + "登录成功！欢迎回来！ <a href=\"" + req.getContextPath() + "/user/user-center/\">点击进入“用户中心”</a>。");
		} else {
			return jsonNoOk("登录失败！");
		}
	}

	/**
	 * 用户登出
	 * 
	 * @return
	 */
	@GET
	@Path("logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String doLogout() {
		LOGGER.info("用户登出");

		MvcRequest.getHttpServletRequest().getSession().invalidate();

		return jsonOk("退出成功！");
	}

	@POST
	@Path("sendSMScode")
	@Produces(MediaType.APPLICATION_JSON)
	public String sendSMScode(@NotNull @QueryParam("phoneNo") String phoneNo) {
		return super.sendSMScode(phoneNo);
	}

	public abstract BiConsumer<User, HttpServletRequest> getAfterLoginCB();

	@GET
	@Path("register")
	public String regsiter() {
		LOGGER.info("用户注册页");

		return jsp("user/register");
	}

	@POST
	@Path("register")
	@MvcFilter(filters = { DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String doRegister(User user, @NotNull @QueryParam("password") String password) throws ServiceException {
		LOGGER.info("正在注册");

		getService().register(user, password);

		return jsonOk("恭喜你，注册成功");
	}

	// @POST
	// @MvcFilter(filters = { DataBaseFilter.class })
	// public String doRegister(User user, @NotNull @QueryParam("password") String
	// password) throws ServiceException {
	// registerByPhone(user, password);
	//
	// return "/user/index.jsp?msg=" + Encode.urlEncode("恭喜你，注册成功！<a
	// href=\"../user/login/\">马上登录</a>") ;
	// }
	
	/**
	 * 检查是否重复的用户名
	 * 
	 * @param phone 手机号码
	 * @return true=已存在
	 */
	@GET
	@Path("checkIfUserNameRepeat")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String checkIfUserNamePhoneRepeat(@NotNull @QueryParam("name") String name) {
		LOGGER.info("检查是否重复的用户名：" + name);
		
		return toJson(new HashMap<String, Boolean>() {
			private static final long serialVersionUID = -5033049204280154615L;
			{
				getService();
				put("isRepeat", UserService.checkIfRepeated("name", name));
			}
		});
	}

	/**
	 * 检查是否重复的手机号码
	 * 
	 * @param phone 手机号码
	 * @return true=已存在
	 */
	@GET
	@Path("checkIfUserEmailRepeat")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String checkIfUserEmailRepeat(@NotNull @QueryParam("email") String email) {
		LOGGER.info("检查是否重复的邮件：" + email);

		return toJson(new HashMap<String, Boolean>() {
			private static final long serialVersionUID = -5033049204280154615L;
			{
				getService();
				put("isRepeat", UserService.checkIfRepeated("email", email));
			}
		});
	}
	
	/**
	 * 检查是否重复的手机号码
	 * 
	 * @param phone 手机号码
	 * @return true=已存在
	 */
	@GET
	@Path("checkIfUserPhoneRepeat")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String checkIfUserPhoneRepeat(@NotNull @QueryParam("phone") String phone) {
		LOGGER.info("检查是否重复的手机号码：" + phone);

		return toJson(new HashMap<String, Boolean>() {
			private static final long serialVersionUID = -5033049204280154615L;
			{
				getService();
				put("isRepeat", UserService.checkIfRepeated("phone", phone));
			}
		});
	}
	
}
