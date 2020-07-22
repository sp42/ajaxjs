package com.ajaxjs.user.filter;

import javax.servlet.http.HttpSession;

import com.ajaxjs.user.role.RoleService;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

public class PrivilegeFilter implements FilterAction {
	private int value;

	public PrivilegeFilter(int value) {
		this.value = value;
	}

	@Override
	public boolean before(FilterContext ctx) {
		HttpSession session = ctx.request.getSession();
		Object privilegeTotal = session.getAttribute("privilegeTotal");

		if (privilegeTotal == null)
			return false;

		return RoleService.check(ctx.request.getSession(), value);
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}

}
