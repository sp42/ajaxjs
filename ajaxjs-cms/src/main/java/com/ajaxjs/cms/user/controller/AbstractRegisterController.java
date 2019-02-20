package com.ajaxjs.cms.user.controller;

import com.ajaxjs.cms.user.User;
import com.ajaxjs.cms.user.UserCommonAuth;
import com.ajaxjs.framework.ServiceException;
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
	public String checkIfUserPhoneRepeat(String phone) {
		LOGGER.info("检查是否重复的手机号码：" + phone);

		return ("{\"isRepeat\":" + getService().checkIfUserPhoneRepeat(phone) + "}");
	}

	public void registerByPhone(User user, String password) throws ServiceException {
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
