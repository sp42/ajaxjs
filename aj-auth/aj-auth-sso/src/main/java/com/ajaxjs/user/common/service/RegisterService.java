package com.ajaxjs.user.common.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.user.UserAuth;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.UserConstant.Login.LoginType;
import com.ajaxjs.user.common.util.CheckStrength;
import com.ajaxjs.user.common.util.CheckStrength.LEVEL;
import com.ajaxjs.util.WebHelper;

@Service
public class RegisterService implements IRegisterService {
	@Autowired
	private LoginService loginService;

	@Override
	public Boolean register(Map<String, Object> params, HttpServletRequest req) {
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

		int tenantId = Integer.parseInt(params.get("tenantId").toString());

		// 是否重复
		if (!hasNoUsername && isRepeat("username", params.get("username").toString(), tenantId))
			throw new IllegalArgumentException("用户名 username: " + params.get("username").toString() + " 重复");

		if (!hasNoEmail && isRepeat("email", params.get("email").toString(), tenantId))
			throw new IllegalArgumentException("邮箱: " + params.get("email").toString() + " 重复");

		if (!hasNoPhone && isRepeat("phone", params.get("phone").toString(), tenantId))
			throw new IllegalArgumentException("手机: " + params.get("phone").toString() + " 重复");

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
		return true;
	}

	@Override
	public Boolean checkRepeat(@RequestParam String field, @RequestParam String value, @RequestParam int tenantId) {
		return isRepeat(field, value, tenantId);
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
