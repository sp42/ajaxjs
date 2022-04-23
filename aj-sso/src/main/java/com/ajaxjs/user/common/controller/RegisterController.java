package com.ajaxjs.user.common.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.UserAuth;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.UserConstant.Login.LoginType;
import com.ajaxjs.user.common.service.LoginService;
import com.ajaxjs.user.common.util.CheckStrength;
import com.ajaxjs.user.common.util.CheckStrength.LEVEL;
import com.ajaxjs.util.WebHelper;

/**
 * 用户注册
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/user/register")
public class RegisterController implements LoginType {
	@Autowired
	LoginService loginService;

	/**
	 * 用户注册 TODO 数据库事务 和 验证码
	 * 
	 * @param params
	 * @return
	 */
	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public String register(@RequestParam(required = true) Map<String, Object> params, HttpServletRequest req) {
		// 所有字符串 trim 一下
		for (String key : params.keySet()) {
			Object obj = params.get(key);
			if (obj instanceof String)
				params.put(key, obj.toString().trim());
		}

		// 校验
		if (isNull(params, "tenantId"))
			throw new IllegalArgumentException("租户 id 不能为空");

		if (isNull(params, "password"))
			throw new IllegalArgumentException("注册密码不能为空");

		boolean hasNoUsername = isNull(params, "username"), hasNoEmail = isNull(params, "email"), hasNoPhone = isNull(params, "phone");
		if (hasNoUsername && hasNoEmail && hasNoPhone)
			throw new IllegalArgumentException("没有用户标识， username/email/phone 至少填一种");

		int tenantId = (int) params.get("tenantId");

		// 是否重复
		if (!hasNoUsername && isRepeat("username", params.get("username").toString(), tenantId))
			throw new IllegalArgumentException("用户名 username: " + params.get("username").toString() + " 重复");

		if (!hasNoEmail && isRepeat("email", params.get("email").toString(), tenantId))
			throw new IllegalArgumentException("邮箱: " + params.get("email").toString() + " 重复");

		if (!hasNoPhone && isRepeat("phone", params.get("phone").toString(), tenantId))
			throw new IllegalArgumentException("手机: " + params.get("phone").toString() + " 重复");

		// TODO 检测密码强度

		// 有些字段不要
		String psw = params.get("password").toString();
		params.remove("password");

		// 检测密码强度
		LEVEL passwordLevel = CheckStrength.getPasswordLevel(psw);

		if (passwordLevel == LEVEL.EASY)
			throw new UnsupportedOperationException("密码强度太低");

		long userId = (long) UserCommonDAO.UserDAO.create(params); // 写入数据库
		UserAuth auth = new UserAuth();
		auth.setUserId(userId);
		auth.setCredential(loginService.encodePassword(psw));
		auth.setRegisterType(LoginType.PASSWORD);
		auth.setRegisterIp(WebHelper.getIp(req));

		// TODO SSO 返回的
		return BaseController.jsonNoOk();
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field    字段名，当前只能是 username/email/phone 中的任意一种
	 * @param value    字段值，要校验的值
	 * @param tenantId 租户 id
	 * @return
	 */
	@GetMapping(value = "/checkRepeat", produces = MediaType.APPLICATION_JSON_VALUE)
	public String checkRepeat(@RequestParam(required = true) String field, @RequestParam(required = true) String value, @RequestParam(required = true) int tenantId) {
		boolean isRepeat = isRepeat(field, value, tenantId);

		return BaseController.jsonOk_Extension("检查某个值是否已经存在一样的值", "\"isRepeat\":" + isRepeat);
	}

	private static boolean isNull(Map<String, Object> params, String key) {
		return !params.containsKey(key) || !StringUtils.hasText(params.get(key).toString());

	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field 数据库里面的字段名称
	 * @param value 欲检查的值
	 * @return true=值重复
	 */
	public static boolean isRepeat(String field, String value, int tenantId) {
		String whereSql = String.format(" %s = '%s' AND tenantId = %s ", field.trim(), value.trim(), tenantId);

		return UserCommonDAO.UserDAO.setWhereQuery(whereSql).findOne() != null; // 有这个数据表示重复
	}
}
