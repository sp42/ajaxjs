package com.ajaxjs.user.profile;

import java.util.Objects;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.common.TreeLikeService;
import com.ajaxjs.cms.service.ThirdPartyService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.BaseUserController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.UserHelper;
import com.ajaxjs.user.filter.LoginCheck;
import com.ajaxjs.user.filter.UserPasswordFilter;
import com.ajaxjs.user.login.LogLoginService;
import com.ajaxjs.user.password.UserCommonAuth;
import com.ajaxjs.user.password.UserCommonAuthService;
import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.util.cache.ExpireCache;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.ComponentMgr;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 帐号管理控制器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Path("/user/account")
@Component
public class AccountController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AccountController.class);
	
	@Resource
	private AccountService service;

	@Resource
	private UserCommonAuthService passwordService;

	@Resource
	private LogLoginService logLoginService;

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	public String account(ModelAndView mv) {
		LOGGER.info("用户会员中心-帐号管理-首页");
		User user = getService().findById(getUserId());
		mv.put("userInfo", user);
		mv.put("isEmailVerified", RoleService.simple8421(user.getVerify() == null ? 0 : user.getVerify(), UserConstant.VERIFIED_EMAIL));
		mv.put("lastUserLoginedInfo", logLoginService.getLastUserLoginedInfo(getUserId()));
		mv.put("UserGroups", TreeLikeService.idAsKey(RoleService.dao.findList(null)));

		return user("account/account");
	}

	@POST
	@Path("modiflyUserName")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyUserName(@NotNull @QueryParam("userName") String userName, HttpServletRequest req) {
		LOGGER.info("修改用户名");

		User user = new User();
		user.setId(getUserId(req));
		user.setName(userName);

		if (getService().update(user) != 0) {
			req.getSession().setAttribute("userName", user.getName());
			return jsonOk("修改用户名成功");
		} else
			return jsonNoOk("修改用户名失败！");
	}

	private final static String EMAIL_VERIFY = "/user/account/emailVerify/";

	@POST
	@Path("emailVerify")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String emailVerify_sendLink(@NotNull @FormParam("email") String email, @FormParam("isUpdate") boolean isUpdate, MvcRequest req) {
		LOGGER.info("邮箱-发送审核链接 " + email);

		if (isUpdate) {// 修改邮箱，所以要写入数据库，但设置状态为未审核的邮箱
			long userId = getUserId();
			User targetUser = getService().findById(userId);
			Objects.requireNonNull(targetUser, "程序异常，没有找到对应的用户，用户 id 为 " + userId);

			User saveEmail = new User(); // 复制一个新的用户，保存部分相关字段
			saveEmail.setId(userId);
			saveEmail.setEmail(email);
			int v = targetUser.getVerify();
			if (RoleService.simple8421(v, AccountService.EMAIL))
				saveEmail.setVerify(v - AccountService.EMAIL);
			getService().update(saveEmail);
		}

		return AccountService.sendTokenMail(email, "邮箱审核", req.getBasePath() + EMAIL_VERIFY) ? jsonOk("修改邮箱成功") : jsonNoOk("修改邮箱失败！");
	}

	@GET
	@Path("emailVerify")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String emailVerif(@NotNull @QueryParam("token") String token, @NotNull @QueryParam("email") String email, ModelAndView mv) {
		LOGGER.info("邮箱-审核链接 " + token);

		long userId = AccountService.checkEmail_VerifyToken(token, email);

		User targetUser = getService().findById(userId);
		Objects.requireNonNull(targetUser, "程序异常，没有找到对应的用户，用户 id 为 " + userId);

		int v = targetUser.getVerify();
		if (RoleService.simple8421(v, AccountService.EMAIL))
			throw new IllegalArgumentException("用户之前已经验证邮件，这次校验无效。");

		User saveEmail = new User(); // 复制一个新的用户，保存部分相关字段
		saveEmail.setId(userId);
		saveEmail.setEmail(email);
		saveEmail.setVerify(v + AccountService.EMAIL);
		getService().update(saveEmail);

		mv.put("title", email + " 已通过审核！");
		mv.put("msg", email + " 已通过审核！");
		mv.put("redirect", "../../login/");

		return page("msg");

	}

	private final static int SMS_EXPIRE_SECONDS = 5 * 60;

	@POST
	@Path("modiflyPhone")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyPhone(@NotNull @QueryParam("phone") String phone) throws ServiceException {
		if (!UserHelper.isVaildPhone(phone))
			throw new IllegalArgumentException(phone + " 不是有效的手机号码");

		ProfileService.checkIfRepeated("phone", phone, "手机号码");

		if (sendSms(phone, getUserId())) {
			return jsonOk("发送验证码成功，五分钟内有效！");
		} else
			return jsonNoOk("发送验证码失败！");
	}

	/**
	 * 
	 * @param phone
	 * @param userId
	 * @return
	 * @throws ServiceException
	 */
	public static boolean sendSms(String phone, long userId) {
		LOGGER.info("修改手机-发送验证码");

		String key = "sms_" + phone;
		int rad;

		if (ExpireCache.CACHE.containsKey(key))
			rad = ExpireCache.CACHE.get(key, int.class);
		else {
			rad = new Random().nextInt(900000) + 100000; // 6 位随机码

			ExpireCache.CACHE.put(key, rad, SMS_EXPIRE_SECONDS);
			ExpireCache.CACHE.put("sms_userId_" + userId, phone, SMS_EXPIRE_SECONDS);
			LOGGER.info("保存用户[{0}] 手机 {1} 验证码 {2} 缓存成功", "sms_userId_" + userId, phone, rad + "");
			/*
			 * 服务端暂存手机号码，那么客户端就不用重复提供了。 验证验证码的时候，根据 userId 查找 手机号码，再得到验证码
			 */
		}

		ThirdPartyService services = ComponentMgr.get(ThirdPartyService.class);
		boolean isOk = services.sendSms(phone, "SMS_138067918", String.format("{\"code\":\"%s\"}", rad));

		if (isOk) {
			LOGGER.info("发送手机 {0} 验证码 {1} 成功", phone, rad + "");
			return true;
		} else
			return false;
	}

	@POST
	@Path("modiflyPhone_Save")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String modiflyPhone_Save(@NotNull @QueryParam("v_code") String v_code) {
		LOGGER.info("修改手机-保存");

		long userId = getUserId();
		String phone = checkSmsCode(userId, v_code);

		User user = new User();
		user.setId(userId);
		user.setPhone(phone);

		// 查找原来 verify
		User _user = getService().findById(userId);
		int verify = _user.getVerify();

		if ((UserConstant.VERIFIED_PHONE & verify) == UserConstant.VERIFIED_PHONE) {
			// 原来已经是通过验证的状态，无须修改
		} else {
			verify += UserConstant.VERIFIED_PHONE;
			user.setVerify(verify);
		}

		if (getService().update(user) != 0) {
			// request.getSession().setAttribute("userName", phone);
			return jsonOk("修改手机成功");
		} else
			return jsonNoOk("修改手机失败！");
	}

	public static String checkSmsCode(long userId, String v_code) {
		String phone = ExpireCache.CACHE.get("sms_userId_" + userId, String.class);
		Objects.requireNonNull(phone, "找不到该用户[" + userId + "]验证码的缓存");
		phone = phone.replace("sms_userId_", "");

		if (!UserHelper.isVaildPhone(phone))
			throw new IllegalArgumentException(phone + " 不是有效的手机号码");

		Integer rad = ExpireCache.CACHE.get("sms_" + phone, Integer.class);
		if (rad == null)
			throw new IllegalArgumentException(phone + " 验证码已经失效或非法手机号码");

		if (rad != Integer.parseInt(v_code))
			throw new IllegalArgumentException("验证码不正确");

		// 验证码正确，删除缓存
		ExpireCache.CACHE.remove("sms_" + phone);
		ExpireCache.CACHE.remove("sms_userId_" + userId);

		return phone;
	}

	@POST
	@Path("resetPassword")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class, UserPasswordFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String resetPassword(@NotNull @QueryParam("new_password") String new_password, HttpServletRequest request) throws ServiceException {
		LOGGER.info("重置密码");

		if (passwordService.updatePwd((UserCommonAuth) request.getAttribute("UserCommonAuthId"), new_password))
			return jsonOk("重置密码成功");
		else
			return jsonNoOk("重置密码失败！");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("log-history")
	public String logHistory(ModelAndView mv) {
		LOGGER.info("用户会员中心-登录历史");

		mv.put("list", logLoginService.findListByUserId(getUserId()));
		return user("account/log-history");
	}

	@GET
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Path("oauth")
	public String oauth() {
		LOGGER.info("用户会员中心-账户绑定");
		return user("oauth");
	}

	@POST
	@Path("delete-account")
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class, UserPasswordFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String doDeleteAccount() {
		LOGGER.info("用户会员中心-账户管理-删除帐号");

		return jsonOk("删除帐号成功");
	}

	@Override
	public IBaseService<User> getService() {
		return service;
	}
}
