/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.web.google_captcha;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.ajaxjs.framework.spring.DiContextUtil;
import com.ajaxjs.net.http.Post;

/**
 * 校验核心
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class GoogleFilter {
	@Autowired
	private GoolgeCaptchaConfig cfg;

	/**
	 * 校验表单时候客户端传过来的 token 参数名
	 */
	public final static String PARAM_NAME = "grecaptchaToken";

	/**
	 * 谷歌校验 API
	 */
	private final static String SITE_VERIFY = "https://www.recaptcha.net/recaptcha/api/siteverify";

	/**
	 * 校验
	 *
	 * @return 是否通过验证，若为 true 表示通过，否则抛出异常
	 */
	public boolean check() {
		return check(DiContextUtil.getRequest());
	}

	/**
	 * 校验
	 *
	 * @param request 请求对象
	 * @return 是否通过验证，若为 true 表示通过，否则抛出异常
	 */
	public boolean check(HttpServletRequest request) {
		return check(request.getParameter(PARAM_NAME));
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	public boolean check(String token) {
		if (!cfg.isEnable())
			return true;

		if (!StringUtils.hasText(token))
			throw new SecurityException("非法攻击！客户端缺少必要的参数");

		Map<String, Object> map = Post.api(SITE_VERIFY, String.format("secret=%s&response=%s", cfg.getAccessSecret(), token.trim()));

		if (map == null)
			throw new IllegalAccessError("谷歌验证码服务失效，请联系技术人员");

		if ((boolean) map.get("success")) {// 判断用户输入的验证码是否通过
			if (map.get("score") != null) {
				// 评分0 到 1。1：确认为人类，0：确认为机器人
				double score = (double) map.get("score");

				if (score < 0.5)
					throw new SecurityException("验证码不通过，非法请求");
			}

			return true;
		} else {
			if ("timeout-or-duplicate".equals(map.get("error-codes")))
				throw new NullPointerException("验证码已经过期，请刷新");

			throw new SecurityException("验证码不正确");
		}
	}
}
