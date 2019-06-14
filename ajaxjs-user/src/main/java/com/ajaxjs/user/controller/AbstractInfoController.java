package com.ajaxjs.user.controller;

import javax.servlet.http.HttpServletRequest;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.app.attachment.Attachment_pictureController;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.user.service.UserCommonAuthService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;

/**
 * 用户信息修改
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractInfoController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractInfoController.class);

	private UserCommonAuthService passwordService = new UserCommonAuthService();

	public UserCommonAuthService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(UserCommonAuthService passwordService) {
		this.passwordService = passwordService;
	}

	public String resetPassword(String new_password, HttpServletRequest request) throws ServiceException {
		LOGGER.info("重置密码");

		if (getPasswordService() != null && getPasswordService().updatePwd((UserCommonAuth) request.getAttribute("UserCommonAuthId"), new_password))
			return jsonOk("重置密码成功");
		else
			return jsonNoOk("重置密码失败！");
	}

	public String modiflyUserName(String userName, HttpServletRequest request) {
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

	public String modiflyEmail(String email) {
		LOGGER.info("修改邮箱");

		User user = new User();
		user.setId(getUserId());
		user.setEmail(email);

		if (getService().update(user) != 0)
			return jsonOk("修改邮箱成功");
		else
			return jsonNoOk("修改邮箱失败！");
	}

	public String modiflyPhone(String phone) {
		LOGGER.info("修改手机");
		User user = new User();
		user.setId(getUserId());
		user.setPhone(phone);

		if (getService().update(user) != 0) {
//			request.getSession().setAttribute("userName", phone);
			return jsonOk("修改手机成功");
		} else
			return jsonNoOk("修改手机失败！");
	}

	/**
	 * 更新头像
	 * 
	 * @param userUid
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public String updateOrCreateAvatar(MvcRequest request) throws Exception  {
		LOGGER.info("更新头像,uid:" + getUserUid());
		UploadFileInfo info;

		info = Attachment_pictureController.uploadByConfig(request);

		final Attachment_picture avatar = getService().updateOrCreateAvatar(getUserUid(), info);

		return toJson(new Object() {
			@SuppressWarnings("unused")
			public Boolean isOk = true;
			@SuppressWarnings("unused")
			public String msg = "修改头像成功！";
			@SuppressWarnings("unused")
			public String imgUrl = avatar.getPath();
		});
	}
}