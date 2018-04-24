package com.ajaxjs.mvc.filter;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;

public interface FilterAction {
	public boolean before(MvcRequest request, MvcOutput response, IController controller);

	/**
	 * 
	 * @param request
	 * @param response
	 * @param controller
	 * @param isSkip
	 *            是否已经中止控制器方法的执行，也就是 before() 返回的值
	 */
	public void after(MvcRequest request, MvcOutput response, IController controller, boolean isSkip, Throwable filterEx);
}
