package com.ajaxjs.user.role;

import java.lang.reflect.Method;

import javax.servlet.http.HttpSession;

import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcOutput;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;

public class PrivilegeFilter implements FilterAction {
	private int value;

	public PrivilegeFilter(int value) {
		this.value = value;
	}

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		HttpSession session = request.getSession();
		Object privilegeTotal = session.getAttribute("privilegeTotal");

		if (privilegeTotal == null)
			return false;

		return RoleService.check((long) privilegeTotal, value);
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}

}
