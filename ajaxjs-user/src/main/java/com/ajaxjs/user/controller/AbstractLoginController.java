package com.ajaxjs.user.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.utils.sms.SMS;
import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.UserLoginLog;
import com.ajaxjs.user.UserService;
import com.ajaxjs.user.UserUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.captcha.CaptchaController;
import com.ajaxjs.web.captcha.CaptchaFilter;

/**
 * 注册控制器
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractLoginController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractLoginController.class);

	public final static int LOGIN_USER_ID = 1;

	public final static int LOGIN_USER_EMAIL = 2;

	public final static int LOGIN_USER_PHONE = 4;

	public static final String LOGIN_PASSED = "PASSED";

	@Resource("AliyunSMSSender")
	private SMS sms;

	@GET
	@Path("/login")
	public String login(ModelAndView mv) {
		LOGGER.info("用户登录页");

		setUserId(mv);
		mv.put(CaptchaController.CAPTCHA_CODE, CaptchaController.CAPTCHA_CODE);

		return jsp("user/login");
	}

	@POST
	@Path("/login")
	@MvcFilter(filters = { CaptchaFilter.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String loginByPassword(@NotNull @QueryParam("userID") String userID, @NotNull @QueryParam("password") String password, HttpServletRequest req) throws ServiceException {
		LOGGER.info("执行登录（按密码的）");
		
		userID = userID.trim();
		
		User user = new User();

		String msg = loginByPassword(user, password, req);

		if (msg.equals(LOGIN_PASSED)) {
			String name = getUserBySession().getName();

			if (name == null)
				name = getUserBySession().getPhone();

			msg = "用户 " + name + " 欢迎回来！ <a href=\"" + req.getContextPath() + "/user/center/\">点击进入“用户中心”</a>";
			return jsonOk(name + " 登录成功");
		}

		return jsonNoOk("登录失败！");
	}

	public String loginByPassword(User user, String password, HttpServletRequest req) throws ServiceException {
		LOGGER.info("检查登录是否合法");

		String msg = "";

		if (req.getAttribute("CaptchaException") != null) { // 需要驗證碼參數
			msg = ((Throwable) req.getAttribute("CaptchaException")).getMessage();
		} else {
			if (password == null)
				msg = "密码不能为空";
			else {
				UserCommonAuth phoneLoign = new UserCommonAuth();
				phoneLoign.setPassword(password);
				phoneLoign.setLoginType(UserDict.loginByPhoneNumber);

				if (getService().loginByPassword(user, phoneLoign)) {
					afterLogin(user, req);
					msg = LOGIN_PASSED;
				} else {
					msg = "用户登录失败！";
				}
			}
		}

		return msg;
	}

	/**
	 * 用户登出
	 * 
	 * @return
	 */
	@GET
	@Path("/logout")
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

	/**
	 * 会员登录之后的动作，会保存 userId 和 userName 在 Session中
	 * 
	 * @param user 用户
	 * @param request 请求对象
	 */
	public void afterLogin(User user, HttpServletRequest request) {
		// 用户登录日志
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setUserId(user.getId());
		userLoginLog.setLoginType(UserDict.PASSWORD);
		LoginLogController.initBean(userLoginLog, request);

		if (LoginLogController.service.create(userLoginLog) <= 0) {
			LOGGER.warning("更新会员登录日志出错");
		}

		//
		Attachment_picture avatar = null;

		UserService service = getService();
		user = service.findById(user.getId());
		avatar = service.findAvaterByUserId(user.getUid());

		request.getSession().setAttribute("userId", user.getId());
		request.getSession().setAttribute("userUid", user.getUid());
		request.getSession().setAttribute("userName", user.getName());
		request.getSession().setAttribute("userPhone", user.getPhone());
		request.getSession().setAttribute("userGroupId", user.getRoleId());

		// 获取资源权限总值
		request.getSession().setAttribute("userGroupId", user.getRoleId());

		if (getAfterLoginCB() != null) {
			getAfterLoginCB().accept(user, request);
		}

		if (user.getRoleId() == null || user.getRoleId() == 0L) {
			// 未设置用户权限
		} else {
			long privilegeTotal = UserService.dao.getPrivilegeByUserGroupId(user.getRoleId());

			// System.out.println("privilegeTotal:" +privilegeTotal);
			request.getSession().setAttribute("privilegeTotal", privilegeTotal);
		}

		if (avatar != null)
			request.getSession().setAttribute("userAvatar", request.getContextPath() + avatar.getPath());
	}

	/**
	 * 设置用户显示可选择的帐号
	 * 
	 * @param mv
	 */
	public static void setUserId(ModelAndView mv) {
		List<String> userID = new ArrayList<>();

		int passWordLoginType = ConfigService.getValueAsInt("user.login.passWordLoginType");

		if (UserUtil.testBCD(AbstractLoginController.LOGIN_USER_ID, passWordLoginType))
			userID.add("用户名");
		if (UserUtil.testBCD(AbstractLoginController.LOGIN_USER_EMAIL, passWordLoginType))
			userID.add("邮件");
		if (UserUtil.testBCD(AbstractLoginController.LOGIN_USER_PHONE, passWordLoginType))
			userID.add("手机");

		mv.put("userID", String.join("/", userID));
	}
}
