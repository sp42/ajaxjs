/**
 * 版权所有 2017 Sp42 frank@ajaxjs.com 根据 2.0 版本 Apache 许可证("许可证")授权；
 * 根据本许可证，用户可以不使用此文件。 用户可从下列网址获得许可证副本：
 * http://www.apache.org/licenses/LICENSE-2.0
 * 除非因适用法律需要或书面同意，根据许可证分发的软件是基于"按原样"基础提供，
 * 无任何明示的或暗示的保证或条件。详见根据许可证许可下，特定语言的管辖权限和限制。
 */
package com.ajaxjs.framework.filter;

import java.util.Map;

import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.framework.CommonConstant;
import com.ajaxjs.web.mvc.MvcConstant;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

/**
 * 检查实体是否可以公开给前端展示
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class FrontEndOnlyCheck implements FilterAction {

	@Override
	public boolean before(FilterContext ctx) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean after(FilterAfterArgs args) {
		Object info = args.model.get(MvcConstant.INFO);

		if (info == null)
			return true;

		Integer status = null;

		if (info instanceof BaseModel)
			status = ((BaseModel) info).getStat();
		else if (info instanceof Map)
			status = (Integer) ((Map<String, Object>) info).get("stat");

		if (status != null && (CommonConstant.DELTETED == (int) status || CommonConstant.OFF_LINE == (int) status)) {
			args.isbeforeSkip = true; // 好像没什么用

			args.response.resultHandler("html::实体已下线或已不存在", args.request, null, args.method);
			args.response.setSet(true);

			return false;
		}

		return true;
	}
}
