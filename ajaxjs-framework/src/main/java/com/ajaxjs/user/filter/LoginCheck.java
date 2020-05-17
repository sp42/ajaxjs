package com.ajaxjs.user.filter;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.util.CommonUtil;

/**
 * 是否已经登录的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LoginCheck implements FilterAction {
	@Override
	public boolean before(ModelAndView model, MvcRequest req, MvcOutput response, Method method, Object[] args) {
		boolean sessionBase = BaseUserController.isLogined(req);
		if (sessionBase)
			return true;

		// Token 检测，适用于 App 或 小程序
		String token = req.getHeader(Constant.USER_SESSION_ID), id = req.getHeader(Constant.USER_ID);

		if (!CommonUtil.isEmptyString(token) && !CommonUtil.isEmptyString(id)) {
			// TODO 鉴权！
			return true;
					 
		}

		throw new IllegalAccessError("尚未登录，没有权限操作");
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
