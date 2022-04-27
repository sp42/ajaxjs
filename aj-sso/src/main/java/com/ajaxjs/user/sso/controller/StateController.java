package com.ajaxjs.user.sso.controller;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.common.util.UserUtils;
import com.ajaxjs.user.sso.SsoUtil;
import com.ajaxjs.user.sso.model.AccessToken;
import com.ajaxjs.user.sso.model.ErrorCodeEnum;
import com.ajaxjs.user.sso.service.SsoDAO;
import com.ajaxjs.util.date.LocalDateUtils;

/**
 * 获取用户是否登录，Token 是否合法的控制器
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/state")
public class StateController extends BaseController implements SsoDAO {

	/**
	 * 查询用户是否已经登陆
	 * 
	 * @param req
	 * @return true 表示已经登录
	 */
	@GetMapping("isLogined")
	public String isLogined(HttpServletRequest req) {
		return UserUtils.isLogined(req) ? jsonOk("User Logined") : jsonNoOk("NOT Login");
	}

	/**
	 * 检查 Access Token 是否已经失效
	 * 
	 * @param access_token 访问令牌
	 * @return
	 */
	@GetMapping(value="verifyToken", produces = JSON)
	public String verify(@RequestParam String access_token) {
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

		return jsonOk();
	}
}
