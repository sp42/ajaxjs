package com.ajaxjs.user.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.app.ThirdPartyService;
import com.ajaxjs.app.catalog.CatalogService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.ioc.BeanContext;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.net.mail.Mail;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.UserUtil;
import com.ajaxjs.user.controller.LoginLogController.UserLoginLogService;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.filter.UserPasswordFilter;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 帐号管理控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public abstract class AbstractAccountInfoController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractAccountInfoController.class);

	private UserCommonAuthService passwordService = new UserCommonAuthService();

	public UserCommonAuthService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(UserCommonAuthService passwordService) {
		this.passwordService = passwordService;
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("account")
	public String account(ModelAndView mv) {
		LOGGER.info("用户会员中心-帐号管理-首页");

		User user = getService().findById(getUserId());
		System.out.println(user.getVerify());
		System.out.println(user.getVerify());
		mv.put("userInfo", user);
		mv.put("isEmailVerified", RoleService.simple8421(user.getVerify(), UserDict.VERIFIED_EMAIL));
		mv.put("lastUserLoginedInfo", LoginLogController.service.dao.getLastUserLoginedInfo(getUserId()));

		mv.put("UserGroups", CatalogService.list2map_id_as_key(RoleService.dao.findList(null)));

		return user("account");
	}

	@POST
	@Path("safe/modiflyUserName")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyUserName(@NotNull @QueryParam("userName") String userName, HttpServletRequest request) {
		LOGGER.info("修改用户名");

		User user = new User();
		user.setId(getUserId(request));
		user.setName(userName);

		if (getService().update(user) != 0) {
			request.getSession().setAttribute("userName", user.getName());
			return jsonOk("修改用户名成功");
		} else
			return jsonNoOk("修改用户名失败！");
	}

	@POST
	@Path("account/emailVerify")
	@MvcFilter(filters = { LoginCheck.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String emailVerify_sendLink(@NotNull @QueryParam("email") String email) {
		LOGGER.info("邮箱-发送审核链接 " + email);

		String url = "http://www.qq.com";

		Mail mail = new Mail();
		mail.setTo("sp42@qq.com");
		mail.setSubject("邮箱审核");
		mail.setHTML_body(true);
		mail.setContent(
				"您好：请点击下面链接审核邮箱 <a target=\"_blank\" href=\"" + url + "\">审核邮箱</a>。 <br /> 如不能打开，请复制该链接到浏览器 " + url);

		ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);
		return services.sendEmail(mail) ? jsonOk("修改邮箱成功") : jsonNoOk("修改邮箱失败！");
	}

	@GET
	@Path("account/emailVerify")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String emailVerif(@NotNull @QueryParam("token") String token) {
		LOGGER.info("邮箱-审核链接 " + token);

		Mail mail = new Mail();
		mail.setTo("sp42@qq.com");
		mail.setSubject("邮箱审核");
		mail.setHTML_body(true);
		mail.setContent("我希望可以跟你做朋友 <a target=\"_blank\" href=\"http://www.qq.com\">QQ</a> 34354344");

		ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);
		return services.sendEmail(mail) ? jsonOk("修改邮箱成功") : jsonNoOk("修改邮箱失败！");
	}

	@POST
	@Path("account/modiflyEmail")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyEmail(@NotNull @QueryParam("email") String email) {
		LOGGER.info("修改邮箱");

		User user = new User();
		user.setId(getUserId());
		user.setEmail(email);

		return getService().update(user) != 0 ? jsonOk("修改邮箱成功") : jsonNoOk("修改邮箱失败！");
	}

	private final static int SMS_EXPIRE_SECONDS = 5 * 60;

	@POST
	@Path("account/modiflyPhone")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyPhone(@NotNull @QueryParam("phone") String phone) throws ServiceException {
		LOGGER.info("修改手机-发送验证码");

		if (!UserUtil.isVaildPhone(phone))
			throw new IllegalArgumentException(phone + " 不是有效的手机号码");

		UserService.checkIfRepeated("phone", phone, "手机号码");

		String key = "sms_" + phone;
		int rad;

		if (ExpireCache.CACHE.containsKey(key))
			rad = ExpireCache.CACHE.get(key, int.class);
		else {
			rad = new Random().nextInt(900000) + 100000; // 6 位随机码

			ExpireCache.CACHE.put(key, rad, SMS_EXPIRE_SECONDS);
			ExpireCache.CACHE.put("sms_userId_" + getUserId(), phone, SMS_EXPIRE_SECONDS);
			/*
			 * 服务端暂存手机号码，那么客户端就不用重复提供了。 验证验证码的时候，根据 userId 查找 手机号码，再得到验证码
			 */
		}

		ThirdPartyService services = BeanContext.getByClass(ThirdPartyService.class);

		if (services.sendSms(phone, "SMS_138067918", String.format("{\"code\":\"%s\"}", rad))) {
			LOGGER.info("发送手机 {0} 验证码 {1} 成功", phone, rad + "");
			return jsonOk("发送验证码成功，五分钟内有效！");
		} else
			return jsonNoOk("发送验证码失败！");
	}

	@POST
	@Path("account/modiflyPhone_Save")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyPhone_Save(@NotNull @QueryParam("v_code") String v_code) {
		LOGGER.info("修改手机-保存");

		long userId = getUserId();
		String phone = ExpireCache.CACHE.get("sms_userId_" + userId, String.class);
		phone = phone.replace("sms_userId_", "");

		if (!UserUtil.isVaildPhone(phone))
			throw new IllegalArgumentException(phone + " 不是有效的手机号码");

		Integer rad = ExpireCache.CACHE.get("sms_" + phone, Integer.class);
		if (rad == null)
			throw new IllegalArgumentException(phone + " 验证码已经失效或非法手机号码");

		if (rad != Integer.parseInt(v_code))
			throw new IllegalArgumentException("验证码不正确");

		// 验证码正确，删除缓存
		ExpireCache.CACHE.remove("sms_" + phone);
		ExpireCache.CACHE.remove("sms_userId_" + userId);

		User user = new User();
		user.setId(userId);
		user.setPhone(phone);

		// 查找原来 verify
		User _user = getService().findById(userId);
		int verify = _user.getVerify();

		if ((UserDict.VERIFIED_PHONE & verify) == UserDict.VERIFIED_PHONE) {
			// 原来已经是通过验证的状态，无须修改
		} else {
			verify += UserDict.VERIFIED_PHONE;
			user.setVerify(verify);
		}

		if (getService().update(user) != 0) {
			// request.getSession().setAttribute("userName", phone);
			return jsonOk("修改手机成功");
		} else
			return jsonNoOk("修改手机失败！");
	}

	@POST
	@Path("account/safe/resetPassword")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class, UserPasswordFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String resetPassword(@NotNull @QueryParam("new_password") String new_password, HttpServletRequest request)
			throws ServiceException {
		LOGGER.info("重置密码");

		if (getPasswordService() != null && getPasswordService()
				.updatePwd((UserCommonAuth) request.getAttribute("UserCommonAuthId"), new_password))
			return jsonOk("重置密码成功");
		else
			return jsonNoOk("重置密码失败！");
	}

	public static UserLoginLogService userLoginLogService = new UserLoginLogService();

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("account/log-history")
	public String logHistory(ModelAndView mv) {
		LOGGER.info("用户会员中心-登录历史");

		mv.put("list", userLoginLogService.findListByUserId(getUserId()));

		return user("log-history");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("account/oauth")
	public String oauth() {
		LOGGER.info("用户会员中心-账户绑定");
		return user("oauth");
	}

	@POST
	@Path("account/delete-account")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class, UserPasswordFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String doDeleteAccount() {
		LOGGER.info("用户会员中心-账户管理-删除帐号");

		return jsonOk("删除帐号成功");
	}
}
