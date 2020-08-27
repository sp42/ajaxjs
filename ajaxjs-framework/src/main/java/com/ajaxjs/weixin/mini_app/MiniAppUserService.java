package com.ajaxjs.weixin.mini_app;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.config.ConfigService;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.sql.annotation.Insert;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.UserConstant;
import com.ajaxjs.user.login.LogLoginController;
import com.ajaxjs.user.login.LogLogin;
import com.ajaxjs.user.login.UserOauth;
import com.ajaxjs.user.login.UserOauthService;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.web.mvc.controller.MvcRequest;
import com.ajaxjs.weixin.mini_app.model.UserInfo;
import com.ajaxjs.weixin.mini_app.model.UserLoginToken;
import com.ajaxjs.weixin.mini_app.model.UserSession;

/**
 * 小程序用户开放能力
 * 参见：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class MiniAppUserService extends BaseService<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(MiniAppUserService.class);

	@TableName(value = "user", beanClass = User.class)
	public static interface UserDao extends IBaseDao<User> {
		@Select("SELECT u.* FROM ${tableName} u INNER JOIN user_oauth o ON u.id = o.userId WHERE loginType = 3 AND o.oauthId = ?")
		public User findUserByOauthId(String oauthId);

		@Insert(tableName = "user_oauth")
		public int saveOpenId(UserOauth bean);
	}

	public static UserDao dao = new Repository().bind(UserDao.class);

	{
		setUiName("微信小程序用户服务");
		setShortName("miniAppUserService");
		setDao(dao);
	}


//	@Resource("UserService")
//	private UserService userService;

//	@Resource("UserOauthService")
	private UserOauthService userOauthService = new UserOauthService();

	/**
	 * 登录凭证校验， 获取 session_key 和 openid 等
	 */
	private final static String CODE2SESSION = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

	/**
	 * 获取用户 openId
	 * 
	 * @param jsCode       小程序调用 wx.login() 获取“临时登录凭证”的密钥
	 * @param userInfoJson
	 * @return 小程序用户登录的凭证
	 */
	public UserSession wxLogin(String jsCode, String userInfoJson) {
		String url = String.format(CODE2SESSION, ConfigService.getValueAsString("mini_program.appId"),
				ConfigService.getValueAsString("mini_program.appSecret"), jsCode);
		String json = NetUtil.simpleGET(url);

		LOGGER.info(json);
		UserLoginToken token = JsonHelper.parseMapAsBean(json, UserLoginToken.class);
		token.check();
		
		User user = dao.findUserByOauthId(token.getOpenid());

		if (user == null) {
			LOGGER.info("没有会员，新注册 " + userInfoJson);
			user = register(userInfoJson, token.getOpenid());
		} else {
			LOGGER.info("用户已经注册过");
		}

		token.setUserId(user.getId());
		doLog(user);

		return token.getSessionId();
	}


	/**
	 * 获取 session_key 和 openid。临时登录凭证 code 只能使用一次 会话密钥 session_key 是对用户数据进行加密签名的密钥。
	 * 为了应用自身的数据安全，开发者服务器不应该把会话密钥下发到小程序，也不应该对外提供这个密钥
	 * 
	 * @param jsCode 小程序调用 wx.login() 获取“临时登录凭证”的密钥
	 * @return 小程序用户登录的凭证
	 */
	

	/**
	 * 注册新用户
	 * 
	 * @param userInfoJson 用户信息，微信后台提供
	 * @param string       OpenId
	 * @return 用户对象
	 */
	private User register(String userInfoJson, String openId) {
		UserInfo wxUser = JsonHelper.parseMapAsBean(userInfoJson, UserInfo.class);
		User user = wxUser.toSystemUser();
//		Long userId = userService.create(user);
		Long userId = create(user);

		UserOauth oauth = new UserOauth();
		oauth.setUserId(userId);
		oauth.setOauthId(openId);
		oauth.setLoginType(UserConstant.WECHAT_MINI);
		userOauthService.create(oauth);

		return user;
	}
//	private User register(String userInfoJson, String openId) {
//		Map<String, Object> map = JsonHelper.parseMap(userInfoJson);
//		User user = new User();
//		user.setUsername(map.get("nickName").toString());
//		user.setSex((int) map.get("gender"));
//		user.setAvatar(map.get("avatarUrl").toString());
//		user.setLocation(map.get("country").toString() + " " + map.get("province").toString() + " "
//				+ map.get("city").toString());
////		Long userId = userService.create(user);
//		Long userId = create(user);
//		
//		UserOauth oauth = new UserOauth();
//		oauth.setUserId(userId);
//		oauth.setOauthId(openId);
//		oauth.setLoginType(UserDict.WECHAT_MINI);
//		userOauthService.create(oauth);
//		
//		return user;
//	}

	/**
	 * 记录用户的登录日志
	 * 
	 * @param user 用户对象
	 */
	private static void doLog(User user) {
		LogLogin userLoginLog = new LogLogin();
		userLoginLog.setUserId(user.getId());
		userLoginLog.setLoginType(UserConstant.WECHAT_MINI);
		LogLoginController.initBean(userLoginLog, MvcRequest.getMvcRequest());
		LogLoginController.service.create(userLoginLog);

		if (LogLoginController.service.create(userLoginLog) <= 0)
			LOGGER.warning("更新会员登录日志出错");
	}
}
