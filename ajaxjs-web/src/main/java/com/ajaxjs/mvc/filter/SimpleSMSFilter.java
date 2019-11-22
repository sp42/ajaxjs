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

/**
 * 简易的短信驗證碼存儲，存儲在 Session 中
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class SimpleSMSFilter extends SessionValueFilter {
	public static final String SMS_KEY_NAME = "randomSmsCode";

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method, Object[] args) {
		String client = getClientSideArgs(request, SMS_KEY_NAME), server = getServerSideValue(request, SMS_KEY_NAME);

		if (client.equals(server)) {
			request.getSession().removeAttribute(SMS_KEY_NAME);
			return true;
		} else {
			throw new IllegalAccessError("手机验证码不通过");
		}
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
	}
}
