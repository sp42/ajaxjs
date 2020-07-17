
package com.ajaxjs.framework.filter;

import java.lang.reflect.Method;

import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcOutput;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;

/**
 * 分页的参数不是行记录，而是第几页的意思
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class PageParamConvert implements FilterAction {

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		int pageStart = (int) args[0], pageSize = (int) args[1];

		if (pageSize == 0)
			pageSize = 8;

		int start = 0;
		if (pageStart >= 1)
			start = (pageStart - 1) * pageSize;

		args[0] = start;

		return true;
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
