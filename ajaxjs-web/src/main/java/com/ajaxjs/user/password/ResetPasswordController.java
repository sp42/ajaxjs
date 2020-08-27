package com.ajaxjs.user.password;

import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.framework.filter.CaptchaFilter;
import com.ajaxjs.framework.filter.DataBaseFilter;
import com.ajaxjs.sql.orm.IBaseService;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.profile.AbstractAccountInfoController;
import com.ajaxjs.user.service.AccountService;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.MvcFilter;

/**
 * 
 * 重置密码
 */
@Path("/user/reset_password")
@Component
public class ResetPasswordController extends BaseController<Map<String, Object>> {
	private static final LogHelper LOGGER = LogHelper.getLog(ResetPasswordController.class);

	@GET
	public String get() {
		LOGGER.info("重置密码-输入邮箱");
		return jsp("user/reset-password");
	}

	private final static String FIND_BY_EMAIL = "/user/reset_password/findByEmail/";

	@Resource("UserService")
	private UserService userService;

	@POST
	@Path("findBySms")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	public String findBySms(@NotNull @FormParam("phone") String phone, ModelAndView mv) {
		LOGGER.info("重置密码-输入新密码 by SMS");

		User user = UserService.dao.findByPhone(phone);
		if (user == null)
			throw new IllegalArgumentException("找不到该手机 " + phone + "的用户");

		if (AbstractAccountInfoController.sendSms(phone, user.getId())) {
			// 适应 api 和 页面
			if (isJson())
				return jsonOk("发送手机 " + phone + " 验证码成功");
			else {
				mv.put("phone", phone);
				mv.put("userId", user.getId());
				mv.put("showMode", 3);

				return jsp("user/reset-password-findBySMS");
			}
		}

		throw new IllegalArgumentException("发送短信失败");
	}

	@POST
	@Path("findBySms/verify")
	@MvcFilter(filters = { DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String updatePwdBySMS(@NotNull @FormParam("userId") long userId, @NotNull @FormParam("v_code") String v_code, @NotNull @FormParam("password") String password)
			throws ServiceException {
		LOGGER.info("重置密码-保存新密码 by SMS");

		AbstractAccountInfoController.checkSmsCode(userId, v_code);
		UserCommonAuth auth = UserCommonAuthService.dao.findByUserId(userId);

		if (auth != null && passwordService.updatePwd(auth, password))
			return jsonOk("重置密码成功");
		else
			return jsonNoOk("重置密码失败！");
	}

	@POST
	@Path("findByEmail")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String sendRestEmail(@NotNull @FormParam("email") String email, MvcRequest req) {
		LOGGER.info("重置密码-发送 Token 邮件");

		return AccountService.sendTokenMail(email, "重置密码", req.getBasePath() + FIND_BY_EMAIL) ? jsonOk("发送邮件成功") : jsonNoOk("发送邮件失败！");
	}

	@GET
	@Path("findByEmail")
	@MvcFilter(filters = { DataBaseFilter.class })
	public String findByEmailJSP(@NotNull @QueryParam("token") String token, @NotNull @QueryParam("email") String email, ModelAndView mv) {
		LOGGER.info("重置密码-输入新密码");

		Long userId = AccountService.checkEmail_VerifyToken(token, email);

		if (userId != null && userId != 0) {
			mv.put("token", token);
			mv.put("email", email);
			return jsp("user/reset-password-findByEmail");
		} else {
			throw new IllegalAccessError("非法访问");
		}
	}

	@Resource
	private UserCommonAuthService passwordService;

	@POST
	@Path("findByEmail/verify")
	@MvcFilter(filters = { DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String updatePwd(@NotNull @QueryParam("token") String token, @NotNull @QueryParam("email") String email, @NotNull @QueryParam("password") String password)
			throws ServiceException {
		LOGGER.info("重置密码-保存新密码");

		Long userId = AccountService.checkEmail_VerifyToken(token, email);
		UserCommonAuth auth = UserCommonAuthService.dao.findByUserId(userId);

		if (userId != null && userId != 0) {
			if (auth != null && passwordService.updatePwd(auth, password))
				return jsonOk("重置密码成功");
			else
				return jsonNoOk("重置密码失败！");
		} else {
			throw new IllegalAccessError("非法访问");
		}
	}

	@Override
	public IBaseService<Map<String, Object>> getService() {
		return null;
	}
}
