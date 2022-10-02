package com.ajaxjs.user.sso.service;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.user.sso.SsoUtil;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.ErrorCodeEnum;
import com.ajaxjs.util.date.LocalDateUtils;

@Service
public class StateService implements IStateService {
	@Override
	public Boolean isLogined(HttpServletRequest req) {
		return UserUtils.isLogined(req);
	}

	@Override
	public Boolean verify(String access_token) {
		AccessToken accessToken = findByAccessToken(access_token);// 查询数据库中的 Access Token

		if (accessToken == null)
			return SsoUtil.oauthError(ErrorCodeEnum.INVALID_GRANT);

		long savedExpiresAt = accessToken.getExpiresIn();// 过期日期
		LocalDateTime expiresDateTime = LocalDateUtils.ofEpochSecond(savedExpiresAt);

		System.out.println(expiresDateTime);
		// 如果 Access Token 已经失效，则返回错误提示
		if (LocalDateUtils.isTimeout(expiresDateTime)) {
			// TODO 是否要删除过期 token？
			return SsoUtil.oauthError(ErrorCodeEnum.EXPIRED_TOKEN);
		}

		return true;
	}
}
