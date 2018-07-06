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
import com.ajaxjs.simpleApp.Constant;

/**
 * 图形验证码的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CaptchaFilter implements FilterAction {
	/**
	 * SESSION 的键值
	 */
	public static final String SESSION_KEY = "rand";

	public static final String submitedFieldName = "captchaCode";
	
	@Override
	public boolean before(MvcRequest request, MvcOutput response, IController controller) {
		String captchaCode = request.getParameter(submitedFieldName);
		
		String rand = (String) request.getSession().getAttribute(SESSION_KEY);

		System.out.println("rand:" + rand);
		System.out.println("CaptchaCode:" + captchaCode);
		
		boolean isCaptchaPass = false;

		if (rand == null)
			throw new UnsupportedOperationException("请刷新验证码。");
		else if (captchaCode == null || captchaCode.equals("")) {
			throw new IllegalArgumentException("没提供验证码参数");
		} else {
			isCaptchaPass = rand.equalsIgnoreCase(captchaCode); // 判断用户输入的验证码是否通过
			if (!isCaptchaPass)
				throw new IllegalAccessError("验证码不正确");
		}

		if (isCaptchaPass) 
			request.getSession().removeAttribute(SESSION_KEY);// 通过之后记得要 清除验证码
		
		return isCaptchaPass;
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, IController controller, boolean isSkip, Throwable filterEx) {
		if(isSkip || filterEx != null) {
			response.resultHandler(String.format(Constant.json_not_ok, filterEx.getMessage()), request, null);
		}
	}
}
