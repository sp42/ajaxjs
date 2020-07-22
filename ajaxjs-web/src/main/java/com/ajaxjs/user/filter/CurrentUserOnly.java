package com.ajaxjs.user.filter;

import java.util.Map;
import java.util.Objects;

import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

public class CurrentUserOnly implements FilterAction {

	@Override
	public boolean before(FilterContext ctx) {
		String id;

		if ("PUT".equals(ctx.request.getMethod())) {
			Map<String, Object> map = ctx.request.getPutRequestData();
			id = map.get("id").toString();
		} else {
			id = ctx.request.getParameter("id");
		}

		Objects.requireNonNull(id);
		long userId = BaseUserController.getUserId(ctx.request);

		if (userId != Long.parseLong(id))
			throw new IllegalAccessError("用户 id 错误，非法访问！");

		return true;
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}

}
