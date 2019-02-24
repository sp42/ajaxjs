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
package com.ajaxjs.web.captcha;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.SessionValueFilter;

/**
 * 图形验证码的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CaptchaFilter extends SessionValueFilter {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		String captchaCode = getClientSideArgs(request, CaptchaController.submitedFieldName), 
			sessionValue = getServerSideValue(request, CaptchaController.SESSION_KEY);

		// 判断用户输入的验证码是否通过
		if (captchaCode.equalsIgnoreCase(sessionValue)) {
			request.getSession().removeAttribute(CaptchaController.SESSION_KEY);// 通过之后记得要 清除验证码
			return true;
		} else {
			throw new IllegalAccessError("验证码不正确");
		}
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
	}
}
