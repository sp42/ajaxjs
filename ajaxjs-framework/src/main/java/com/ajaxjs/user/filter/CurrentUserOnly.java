package com.ajaxjs.user.filter;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.mvc.filter.FilterAfterArgs;
import com.ajaxjs.user.controller.BaseUserController;

public class CurrentUserOnly implements FilterAction {

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		String id;

		if ("PUT".equals(request.getMethod())) {
			Map<String, Object> map = request.getPutRequestData();
			id = map.get("id").toString();
		} else {
			id = request.getParameter("id");
		}

		Objects.requireNonNull(id);
		long userId = BaseUserController.getUserId(request);

		if (userId != Long.parseLong(id))
			throw new IllegalAccessError("用户 id 错误，非法访问！");

		return true;
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}

}
