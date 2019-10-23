package com.ajaxjs.user.controller;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.mvc.filter.DataBaseFilter;
import com.ajaxjs.mvc.filter.MvcFilter;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserService;
import com.ajaxjs.user.UserUtil;
import com.ajaxjs.user.service.VerifyToken;
import com.ajaxjs.util.Encode;

@Path("/user/verifyEmail")
public class VerifyEmail extends BaseUserController {

	@POST
	@MvcFilter(filters = { LoginCheck.class, DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String save(@NotNull @FormParam("email") String email) {
		if (!UserUtil.isVaildEmail(email))
			throw new IllegalArgumentException(email + "不是合法的邮件地址");

		User user = getUserDetail();
		User saveEmail = new User(); // 复制一个新的用户，保存部分相关字段
		saveEmail.setId(user.getId());
		saveEmail.setEmail(email);

		int v = user.getVerify();
		if ((VerifyToken.EMAIL & v) == VerifyToken.EMAIL) {
			// 发现之前的 已验证的标识,现在重新验证,所以改为未验证状态.
			v -= VerifyToken.EMAIL;
		}

		saveEmail.setVerify(v);
		userService.update(saveEmail);
		return jsonOk("等待审核");
	}

	@GET
	@MvcFilter(filters = { DataBaseFilter.class })
	@Produces(MediaType.APPLICATION_JSON)
	public String verifyCallBack(@NotNull @QueryParam("token") String token) {
		return jsonOk("等待审核");
	}

	UserService userService = new UserService();

	@Override
	public UserService getService() {
		return userService;
	}

	public static class VerifyEmailService extends BaseService<Map<String, Object>> {

		/**
		 * 是否已经验证
		 * 
		 * @return true = 已验证
		 */
		public static boolean isVerifed(String email) {
			return false;
		}

		/**
		 * 邮件验证链接的有限时间，单位：小时
		 */
		public static final int TIMEOUT = 8;

		public static boolean verifyCallBack(String token) {
			Map<String, Object> data = VerifyToken.dao.findByToken(token);
			Objects.requireNonNull(data, "没有找到该 token: " + token + "。 非法请求！");

			Object date = data.get("createDate");
			long createDate;

			if (date instanceof Date) {
				createDate = ((Date) date).getTime();
			} else
				createDate = (long) date;

			long diff = new Date().getTime() - createDate;

			if (diff > (TIMEOUT * 3600000))
				throw new IllegalArgumentException("该 Token 已超时");

			long userUid = (long) data.get("entityUid");
			User user = UserService.dao.findByUid(userUid);
			Objects.requireNonNull(data, "程序异常，没有找到对应的用户，用户 uid 为 " + userUid);

			int v = user.getVerify();
			if ((VerifyToken.EMAIL & v) == VerifyToken.EMAIL)
				throw new IllegalArgumentException("用户之前已经验证邮件，这次校验无效。");

			// 正式判断
			String email = user.getEmail();
			String str = userUid + email + data.get("randomStr") + createDate, _token = Encode.md5(str);

			if (token.equalsIgnoreCase(_token))
				return true;

			return false;
		}

	}
}
