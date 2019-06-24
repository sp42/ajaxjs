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
package com.ajaxjs.user.controller;

import java.lang.reflect.Method;

import com.ajaxjs.mvc.controller.MvcOutput;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.mvc.filter.FilterAction;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.user.UserCommonAuthService;

/**
 * 需要输入用户密码之后才能下一步的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserPasswordFilter implements FilterAction {

	@Override
	public boolean before(MvcRequest request, MvcOutput response, Method method) {
		String password = request.getParameter("password");
		UserCommonAuth auth = UserCommonAuthService.dao.findByUserId(BaseUserController.getUserId());
		request.setAttribute("UserCommonAuthId", auth);

		if (auth.getPassword().equalsIgnoreCase(UserCommonAuthService.encode(password))) {
			return true;
		} else {
			throw new IllegalAccessError("用户名或密码错误");
		}
	}

	@Override
	public void after(MvcRequest request, MvcOutput response, Method method, boolean isSkip) {
	}
}