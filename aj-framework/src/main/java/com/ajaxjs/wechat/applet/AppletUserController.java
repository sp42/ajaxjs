package com.ajaxjs.wechat.applet;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.framework.BaseController;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserAuth;
import com.ajaxjs.user.UserCommonDAO;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.filter.DataBaseFilter;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.MapTool;
import com.ajaxjs.wechat.applet.model.LoginSession;
import com.ajaxjs.wechat.applet.model.UserInfo;
import com.ajaxjs.wechat.applet.model.WeChatAppletConfig;
import com.ajaxjs.wechat.user.UserMgr;

/**
 * 小程序用户接口
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
@RestController
@RequestMapping("/applet/user")
public class AppletUserController {
	private static final LogHelper LOGGER = LogHelper.getLog(AppletUserController.class);

	@Autowired
	private WeChatAppletConfig cfg;

	/**
	 * 登录 or 注册
	 * 
	 * @param code 授权码
	 * @param req
	 * @return
	 */
	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE + ";charset=utf-8")
	@DataBaseFilter
	public String login(@RequestParam(required = true) String code, HttpServletRequest req) {
		LoginSession session = login(cfg, code);
		User user = UserCommonDAO.UserDAO.findUserByOauthId(session.getOpenid(), UserConstant.Login.LoginType.WECHAT_APPLET);

		if (user == null) {
			Map<String, Object> userInfo = WebHelper.getRawBodyAsJson(req);

			if (userInfo != null) {
				LOGGER.info("没有会员，新注册 " + userInfo);
				user = register(userInfo, session.getOpenid());
			} else
				throw new IllegalArgumentException("缺少 userInfoJson 参数");
		} else {
			LOGGER.info("用户已经注册过");
		}

		Map<String, Object> map = new HashMap<>();
		map.put("isOk", true);
		map.put("msg", "登录成功");
		map.put("sessionId", session.getSession_id());
		map.put("userId", user.getId());
		map.put("userName", user.getUsername());

		return BaseController.toJson(map, true, false);
	}

	private final static String LOGIN_API = "https://api.weixin.qq.com/sns/jscode2session";

	/**
	 * 小程序登录
	 * 
	 * @param cfg
	 * @param code
	 */
	private static LoginSession login(WeChatAppletConfig cfg, String code) {
		LOGGER.info("小程序登录");

		String params = String.format("?grant_type=authorization_code&appid=%s&secret=%s&js_code=%s", cfg.getAccessKeyId(), cfg.getAccessSecret(), code);
		Map<String, Object> map = Get.api(LOGIN_API + params);
		LoginSession session = null;

		if (map.containsKey("openid")) {
//			cfg.setAccessToken(map.get("access_token").toString());
			LOGGER.warning("小程序登录成功！ AccessToken [{0}]", map.containsKey("openid"));

			String rndStr = StrUtil.getRandomString(8);
			session = new LoginSession();
			session.setOpenid(map.get("openid").toString());
			session.setSession_key(map.get("session_key").toString());
			session.setSession_id(rndStr);

			UserMgr.SESSION.put(rndStr, session);

		} else if (map.containsKey("errcode")) {
			LOGGER.warning("小程序登录失败！ Error [{0}:{1}]", map.get("errcode"), map.get("errmsg"));
			throw new SecurityException(String.format("小程序登录失败，Error [%s]", map.get("errmsg")));
		} else {
			LOGGER.warning("小程序登录失败，未知异常 [{0}]", map);
			throw new SecurityException("小程序登录失败，未知异常");
		}

		return session;
	}

	/**
	 * 注册新用户
	 * 
	 * @param userInfoJson 用户信息，微信后台提供
	 * @param string       OpenId
	 * @return 用户对象
	 */
	private User register(Map<String, Object> userInfoJson, String openId) {
		UserInfo wxUser = MapTool.map2Bean(userInfoJson, UserInfo.class);
		User user = wxUser.toSystemUser();
		long userId = (long) UserCommonDAO.UserDAO.create(user);

		UserAuth oauth = new UserAuth();
		oauth.setUserId(userId);
		oauth.setIdentifier(openId);
		oauth.setLoginType(UserConstant.Login.LoginType.WECHAT_APPLET);
		UserCommonDAO.UserAuthDAO.create(oauth);

		return user;
	}
}
