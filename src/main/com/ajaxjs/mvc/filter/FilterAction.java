/**
 * Copyright 2015 Sp42 frank@ajaxjs.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ajaxjs.mvc.filter;

import com.ajaxjs.mvc.controller.IController;
import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;

/**
 * 过滤器动作
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public interface FilterAction {
	/**
	 * 在 MVC 方法之前调用
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param controller
	 *            控制器对象
	 * @return 是否要中止控制器方法的执行
	 */
	public boolean before(MvcRequest request, MvcOutput response, IController controller);

	/**
	 * 在 MVC 方法之后调用
	 * 
	 * @param request
	 *            请求对象
	 * @param response
	 *            响应对象
	 * @param controller
	 *            控制器对象
	 * @param isSkip
	 *            是否已经中止控制器方法的执行，也就是 before() 返回的值
	 */
	public void after(MvcRequest request, MvcOutput response, IController controller, boolean isSkip);

}
