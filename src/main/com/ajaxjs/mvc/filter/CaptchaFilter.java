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

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.simpleApp.CaptchaController;

/**
 * 图形验证码的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CaptchaFilter implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		String captchaCode = request.getParameter(CaptchaController.submitedFieldName);

		String rand = (String) request.getSession().getAttribute(CaptchaController.SESSION_KEY);

		Throwable e = null;

		if (rand == null)
			e = new UnsupportedOperationException("请刷新验证码。");
		else if (captchaCode == null || captchaCode.equals("")) {
			e = new IllegalArgumentException("没提供验证码参数");
		} else {
			if (!rand.equalsIgnoreCase(captchaCode))// 判断用户输入的验证码是否通过
				e = new IllegalAccessError("验证码不正确");
		}

		if (e == null)
			request.getSession().removeAttribute(CaptchaController.SESSION_KEY);// 通过之后记得要 清除验证码
		else
			request.setAttribute("CaptchaException", e);
		
		return true;// 为了处理输出结果，这里一律返回 true ，但是控制器一定要捕获这抛出的异常
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
	}
}
