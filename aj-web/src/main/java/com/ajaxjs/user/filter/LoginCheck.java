package com.ajaxjs.user.filter;

import com.ajaxjs.user.login.LoginController;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.web.mvc.MvcConstant;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

/**
 * 是否已经登录的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class LoginCheck implements FilterAction {
	@Override
	public boolean before(FilterContext ctx) {
		boolean sessionBase = LoginController.isLogined(ctx.request);
		if (sessionBase)
			return true;

		// Token 检测，适用于 App 或 小程序
		String token = ctx.request.getHeader(MvcConstant.USER_SESSION_ID), id = ctx.request.getHeader(MvcConstant.USER_ID);

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
