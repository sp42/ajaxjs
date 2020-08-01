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
package com.ajaxjs.web.mvc.filter;

/**
 * 过滤器动作
 * 
 * @author sp42 frank@ajaxjs.com
 */
public interface FilterAction {
	/**
	 * 是异常但不记录到 FileHandler，例如密码错误之类的。放在 ModelAndView 中传递，例如
	 * model.put(NOT_LOG_EXCEPTION, true);
	 */
	public static final String NOT_LOG_EXCEPTION = "NOT_LOG_EXCEPTION";

	/**
	 * 在 MVC 方法之前调用
	 * 
	 * @return 是否要中止控制器方法的执行，true 表示为不中断
	 */
	public boolean before(FilterContext ctx);

	/**
	 * 在 MVC 方法之后调用
	 * 
	 * @param model    页面数据中间件
	 * @param request  请求对象
	 * @param response 响应对象
	 * @param method   方法对象
	 * @param isSkip   是否已经中止控制器方法的执行，也就是 before() 返回的值
	 * @return 是否要中止控制器方法默认返回的执行，一般返回 true 表示按原来的执行（大多数情况）
	 */
	public boolean after(FilterAfterArgs ctx);
}
