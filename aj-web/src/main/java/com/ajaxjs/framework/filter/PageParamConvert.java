
package com.ajaxjs.framework.filter;

import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

/**
 * 分页的参数不是行记录，而是第几页的意思
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */	
public class PageParamConvert implements FilterAction {

	@Override
	public boolean before(FilterContext ctx) {
		int pageStart = (int) ctx.args[0], pageSize = (int) ctx.args[1];

		if (pageSize == 0)
			pageSize = 8;

		int start = 0;
		if (pageStart >= 1)
			start = (pageStart - 1) * pageSize;

		ctx.args[0] = start;

		return true;
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
