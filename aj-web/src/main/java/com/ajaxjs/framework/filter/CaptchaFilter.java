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
package com.ajaxjs.framework.filter;

import java.util.Map;

import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.mvc.filter.FilterAction;
import com.ajaxjs.web.mvc.filter.FilterAfterArgs;
import com.ajaxjs.web.mvc.filter.FilterContext;

/**
 * 图形验证码的拦截器
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class CaptchaFilter implements FilterAction {
	private final static String PARAM_NAME = "grecaptchaToken";
	private final static String SITE_VERIFY = "https://www.recaptcha.net/recaptcha/api/siteverify";
	private final static String PARAMS = "secret=%s&response=%s";

	@Override
	public boolean before(FilterContext ctx) {
		if (ConfigService.getBol("security.disableCaptcha"))
			return true;

		String token = ctx.request.getParameter(PARAM_NAME);

		if (CommonUtil.isEmptyString(token))
			throw new IllegalArgumentException("非法攻击！客户端缺少必要的参数 ");

		token = token.trim();

		String secret = ConfigService.get("security.GoogleReCAPTCHA.secret");
		String params = String.format(PARAMS, secret, token);
		String json = NetUtil.post(SITE_VERIFY, params);

		if (CommonUtil.isEmptyString(json))
			throw new IllegalAccessError("谷歌验证码服务失效，请联系技术人员");

		Map<String, Object> map = JsonHelper.parseMap(json);

		if (map == null)
			throw new IllegalAccessError("谷歌验证码服务失效，请联系技术人员");

		if ((boolean) map.get("success")) // 判断用户输入的验证码是否通过
			return true;
		else {
			// 是异常但不记录到 FileHandler，例如密码错误之类的
			ctx.model.put(NOT_LOG_EXCEPTION, true);

			if ("timeout-or-duplicate".equals(map.get("error-codes").toString()))
				throw new NullPointerException("验证码已经过期，请刷新");

			throw new IllegalAccessError("验证码不正确");
		}
	}

	@Override
	public boolean after(FilterAfterArgs args) {
		return true;
	}
}
