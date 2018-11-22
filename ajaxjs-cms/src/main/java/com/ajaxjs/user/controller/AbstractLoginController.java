 package com.ajaxjs.user.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.user.service.UserConstant;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 注册控制器
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractLoginController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractLoginController.class);

	public static final String LOGIN_PASSED = "PASSED";

	public String loginByPassword(User user, String password, HttpServletRequest request) {
		LOGGER.info("检查登录是否合法");

		String msg = "";
		
		if (request.getAttribute("CaptchaException") != null) { // 需要驗證碼參數
			msg = ((Throwable) request.getAttribute("CaptchaException")).getMessage();
		} else {
			if (password == null)
				msg = "密码不能为空";
			else {
				UserCommonAuth phoneLoign = new UserCommonAuth();
				phoneLoign.setPassword(password);
				phoneLoign.setLoginType(UserConstant.loginByPhoneNumber);

				if (getService().loginByPassword(user, phoneLoign)) {
					afterLogin(user, request);
					msg = LOGIN_PASSED;
				} else {
					msg = "用户登录失败！";
				}
			}
		}

		return msg;
	}

	/**
	 * 会员登录之后的动作，会保存 userId 和 userName 在 Session中
	 * 
	 * @param user 用户
	 * @param request 请求对象
	 */
	public void afterLogin(User user, HttpServletRequest request) {
		Attachment_picture avatar = null;
		
		try {
			UserService service = getService();
			user = service.findById(user.getId());
			avatar = ((UserService)service).findAvaterByUserId(user.getUid());
		} catch (ServiceException e) {
			e.printStackTrace();
		}
		
		request.getSession().setAttribute("userId", user.getId());
		request.getSession().setAttribute("userUid", user.getUid());
		request.getSession().setAttribute("userName", user.getName());
		request.getSession().setAttribute("userPhone", user.getPhone());
		
		if(avatar != null)
			request.getSession().setAttribute("userAvatar", request.getContextPath() + avatar.getPath());
	}

	/**
	 * 用户登出
	 * 
	 * @return
	 */
	public void logout() {
		LOGGER.info("用户登出");
		MvcRequest.getHttpServletRequest().getSession().invalidate();
	}
}