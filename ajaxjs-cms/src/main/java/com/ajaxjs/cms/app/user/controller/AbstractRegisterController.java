package com.ajaxjs.cms.app.user.controller;

import com.ajaxjs.cms.app.user.model.User;
import com.ajaxjs.cms.app.user.model.UserCommonAuth;
import com.ajaxjs.mvc.controller.JsonReuslt;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 注册控制器
 * 
 * @author Frank Cheung
 *
 */
public abstract class AbstractRegisterController extends BaseUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AbstractRegisterController.class);

	/**
	 * 检查是否重复的手机号码
	 * 
	 * @param phone 手机号码
	 * @return true=已存在
	 */
	public JsonReuslt checkIfUserPhoneRepeat(String phone) {
		LOGGER.info("检查是否重复的手机号码：" + phone);

		return new JsonReuslt("{\"isRepeat\":" + getService().checkIfUserPhoneRepeat(phone) + "}");
	}

	public void registerByPhone(User user, String password) {
		LOGGER.info("执行用户注册");

		if (password == null)
			throw new IllegalArgumentException("注册密码不能为空");

		UserCommonAuth passwordModel = new UserCommonAuth();
		passwordModel.setPhone_verified(1); // 已验证
		passwordModel.setPassword(password);

		long id = getService().create(user, passwordModel);
		LOGGER.info("创建用户成功，id：" + id);
	}

}
