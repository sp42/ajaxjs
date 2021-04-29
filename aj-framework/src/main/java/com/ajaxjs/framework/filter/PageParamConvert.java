/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
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
