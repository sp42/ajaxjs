/**
 * Copyright 2015 Sp42 frank@ajaxjs.com Licensed under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.web.captcha;

import java.lang.reflect.Method;

import com.ajaxjs.web.mvc.ModelAndView;
import com.ajaxjs.web.mvc.controller.MvcOutput;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.SessionValueFilter;

/**
 * 图形验证码的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CaptchaFilter extends SessionValueFilter {
	@Override
	public boolean before(ModelAndView model, MvcRequest request, MvcOutput response, Method method, Object[] args) {
		try {
			String captchaCode = getClientSideArgs(request, CaptchaController.CAPTCHA_CODE),
					sessionValue = getServerSideValue(request, CaptchaController.CAPTCHA_CODE);

			// 判断用户输入的验证码是否通过
			if (captchaCode.equalsIgnoreCase(sessionValue)) {
				request.getSession().removeAttribute(CaptchaController.CAPTCHA_CODE);// 通过之后记得要 清除验证码
				return true;
			} else {
				// 是异常但不记录到 FileHandler，例如密码错误之类的
				model.put(NOT_LOG_EXCEPTION, true);
				throw new IllegalAccessError("验证码不正确");
			}

		} catch (Throwable e) {
			if (e instanceof NullPointerException) {
				model.put(NOT_LOG_EXCEPTION, true);
				throw new NullPointerException("验证码已经过期，请刷新");
			}

			throw e;
		}
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
