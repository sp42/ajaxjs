
package com.ajaxjs.cms.filter;

import java.lang.reflect.Method;
import java.util.Map;

import com.ajaxjs.cms.app.CommonConstant;
import com.ajaxjs.framework.BaseModel;
import com.ajaxjs.mvc.Constant;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;

/**
 * 检查实体是否可以公开给前端展示
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class FrontEndOnlyCheck implements FilterAction {

	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void after(ModelAndView model, MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
		Object info = model.get(Constant.info);

		if (info == null)
			return;

		Integer status = null;

		if (info instanceof BaseModel)
			status = ((BaseModel) info).getStat();
		else if (info instanceof Map)
			status = (Integer) ((Map<String, Object>) info).get("stat");

		if (status != null && (CommonConstant.DELTETED == (int) status || CommonConstant.OFF_LINE == (int) status)) {
			isSkip = true; // 好像没什么用

			response.resultHandler("html::实体已下线或已不存在", request, null, method);
			response.setSet(true);
		}
	}

}
