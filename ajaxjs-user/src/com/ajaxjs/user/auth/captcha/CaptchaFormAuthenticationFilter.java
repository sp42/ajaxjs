/**
 * Copyright 2015 Frank Cheung
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
package com.ajaxjs.user.auth.captcha;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import com.ajaxjs.user.BaseUserInfoController;
import com.ajaxjs.util.LogHelper;
import com.ajaxjs.util.StringUtil;
import com.ajaxjs.web.Captcha;

/**
 * 扩展 FormAuthenticationFilter 接下来我们扩展 FormAuthenticationFilter 类， 首先覆盖
 * createToken 方法，以便获取 CaptchaUsernamePasswordToken 实例； 然后增加验证码校验方法
 * doCaptchaValidate； 最后覆盖 Shiro 的认证方法 executeLogin，在原表单认证逻辑处理之前进行验证码校验。
 * 
 * @author frank
 *
 */
public class CaptchaFormAuthenticationFilter extends FormAuthenticationFilter {
	private static final LogHelper LOGGER = LogHelper.getLog(CaptchaFormAuthenticationFilter.class);
 
	private static final String submitedFieldName = "captchaImgCode";
	
	private boolean captchaEnabled = true;// 是否开启验证码支持，默认支持

//	@Override
//	public boolean isAccessAllowed(ServletRequest req, ServletResponse response, Object mappedValue) {
//		System.out.println(getLoginUrl());
//		System.out.println("::::::::"+isLoginSubmission(req, response));
//		System.out.println("::::::::"+isLoginRequest(req, response));
//		req.setAttribute("captchaEbabled", captchaEnabled);
//		return true;
//	}

	/**
	 * 认证
	 * @throws Exception 
	 */
	@Override
	protected boolean executeLogin(ServletRequest request, ServletResponse response) throws Exception {
		LOGGER.info("开始验证码校验……");
		CaptchaUsernamePasswordToken token = null;
		
		try {
			token = createToken(request, response);
		} catch (IllegalArgumentException e) {
			AuthenticationException ae = new AuthenticationException(e.getMessage());// 封装一下
			request.setAttribute(BaseUserInfoController.request_exception_key, e);
			return onLoginFailure(token, ae, request, response);
		}
		
//		System.out.println("login::" + isCaptchaEnabled());

		try {
			// 验证码校验
			// 判断验证码是否禁用，如果是，跳过验证码，直接处理表单
			if(isCaptchaEnabled()) {
				HttpServletRequest req = (HttpServletRequest)request;
				String captcha = (String) req.getSession().getAttribute(Captcha.SESSION_KEY);
				
				if (captcha == null) {
					throw new IncorrectCaptchaException("丢失验证码，请稍后刷新验证码后再试！");
				}
				
				if (captcha != null && !captcha.equalsIgnoreCase(token.getCaptcha())) { // 用户输入的验证码
					throw new IncorrectCaptchaException("验证码错误！");
				}
				
				LOGGER.info("验证码" + captcha + "正确");
			}

			Subject subject = getSubject(request, response);
			subject.login(token);

//			return onLoginSuccess(token, subject, request, response);
			return true; // 返回 true 让过滤器继续（而不是调到默认的 welcome），返回的 controller 那里去，因为我要在那裏返回 json ！
		} catch (AuthenticationException e) {
			request.setAttribute(BaseUserInfoController.request_exception_key, e);
			return onLoginFailure(token, e, request, response);
		}
	}

	/**
	 * 创建 Token
	 */
	@Override
	protected CaptchaUsernamePasswordToken createToken(ServletRequest request, ServletResponse response) {
		String username = getUsername(request);
		String password = getPassword(request);
		
		// 用户输入的验证码
		String captcha = request.getParameter(submitedFieldName);
		
		if(StringUtil.isEmptyString(captcha)) {
			throw new IllegalArgumentException("缺少参数！" + submitedFieldName);
		} else {
			captcha = captcha.trim();
		}
		
		boolean rememberMe = isRememberMe(request);
		String host = getHost(request);

		if(StringUtil.isEmptyString(username)) {
			throw new IllegalArgumentException("缺少 username 参数！");
		}
		
		if(StringUtil.isEmptyString(password)) {
			throw new IllegalArgumentException("缺少 password 参数！");
		}
		
		System.out.println(username);
		System.out.println(password);
		
		return new CaptchaUsernamePasswordToken(username, password.toCharArray(), rememberMe, host, captcha);
	}
	
	public boolean isCaptchaEnabled() {
		return captchaEnabled;
	}

	public void setCaptchaEnabled(boolean captchaEnabled) {
		this.captchaEnabled = captchaEnabled;
	}
}
