package com.ajaxjs.cms.user.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.user.User;
import com.ajaxjs.cms.user.UserCommonAuth;
import com.ajaxjs.cms.user.service.UserConstant;
import com.ajaxjs.cms.user.service.UserService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.mvc.controller.MvcRequest;
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

	public String loginByPassword(User user, String password, HttpServletRequest request) throws ServiceException {
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
System.out.println(getService());
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

		UserService service = getService();
		user = service.findById(user.getId());
		avatar = service.findAvaterByUserId(user.getUid());

		request.getSession().setAttribute("userId", user.getId());
		request.getSession().setAttribute("userUid", user.getUid());
		request.getSession().setAttribute("userName", user.getName());
		request.getSession().setAttribute("userPhone", user.getPhone());

		if (avatar != null)
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
