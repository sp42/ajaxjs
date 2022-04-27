package com.ajaxjs.user.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.user.User;
import com.ajaxjs.user.common.service.LoginService;
import com.ajaxjs.util.filter.DataBaseFilter;

/**
 * 用户注册
 * 
 * @author Frank Cheung
 *
 */
@RestController
@RequestMapping("/user/login")
public class LoginController extends BaseController {
	@Autowired
	private LoginService loginService;

	/**
	 * 用户登录
	 * 
	 * TODO 是否需要考虑 CaptchaFilter
	 * 
	 * @param userID   用户标识，可以是 username/email/phone 中的一种，后台自动判断
	 * @param password 用户密码
	 * @param tenantId 租户 id
	 * @param req      请求对象
	 * @return
	 */
	@PostMapping(produces = JSON)
	@DataBaseFilter
	public String login(@RequestParam String userID, @RequestParam String password, @RequestParam int tenantId, HttpServletRequest req) {
		User user = loginService.loginByPassword(userID, password, tenantId, req);

		if (user == null)
			return jsonNoOk();
		else
			return jsonOk();
	}

	/**
	 * 用户登出
	 * 
	 * @param session
	 * @return
	 */
	@GetMapping(value = "logout", produces = JSON)
	public String logout(HttpSession session) {
		session.invalidate();
		// TODO 清除 SSO 登录状态
		return jsonOk("用户登出成功");
	}

	/**
	 * 查看登录日志
	 * 
	 * @param start
	 * @param limit
	 * @return
	 */
	@GetMapping("/log")
	String listLog(int start, int limit) {
		return "";
	}
}
