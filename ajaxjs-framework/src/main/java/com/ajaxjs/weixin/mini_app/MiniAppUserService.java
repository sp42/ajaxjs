package com.ajaxjs.weixin.mini_app;

import java.util.Map;
import java.util.Objects;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.controller.LoginLogController;
import com.ajaxjs.user.login.UserLoginLog;
import com.ajaxjs.user.login.UserOauth;
import com.ajaxjs.user.login.UserOauthService;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.token.TokenInfo;
import com.ajaxjs.user.token.TokenService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.weixin.mini_app.model.UserLoginToken;

/**
 * 小程序用户开放能力
 * 参见：https://developers.weixin.qq.com/miniprogram/dev/framework/open-ability/login.html
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
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

	/**
	 * 登录凭证校验， 获取 session_key 和 openid 等
	 */
	private final static String code2Session = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";

//	@Resource("UserService")
//	private UserService userService;

//	@Resource("UserOauthService")
	private UserOauthService userOauthService = new UserOauthService();

	/**
	 * 获取用户 openId
	 * 
	 * @param jsCode       小程序调用 wx.login() 获取“临时登录凭证”的密钥
	 * @param userInfoJson
	 * @return 小程序用户登录的凭证
	 */
	public UserLoginToken wxLogin(String jsCode, String userInfoJson) {
		UserLoginToken token = intiToken(jsCode);
		User user = dao.findUserByOauthId(token.getOpenId());

		if (user == null) {
			LOGGER.info("没有会员，新注册 " + userInfoJson);
			user = register(userInfoJson, token.getOpenId());
		} else {
			LOGGER.info("用户已经注册过");
		}

		token.setUserId(user.getId());
		doLog(user);
		getSessionId(token);

		return token;
	}

	/**
	 * 注册新用户
	 * 
	 * @param userInfoJson 用户信息，微信后台提供
	 * @param string       OpenId
	 * @return 用户对象
	 */
	private User register(String userInfoJson, String openId) {
		Map<String, Object> map = JsonHelper.parseMap(userInfoJson);
		User user = new User();
		user.setUsername(map.get("nickName").toString());
		user.setSex((int) map.get("gender"));
		user.setAvatar(map.get("avatarUrl").toString());
		user.setAvatar(map.get("avatarUrl").toString());
		user.setLocation(map.get("country").toString() + " " + map.get("province").toString() + " "
				+ map.get("city").toString());
//		Long userId = userService.create(user);
		Long userId = create(user);

		UserOauth oauth = new UserOauth();
		oauth.setUserId(userId);
		oauth.setOauthId(openId);
		oauth.setLoginType(UserDict.WECHAT_MINI);
		userOauthService.create(oauth);

		return user;
	}

	/**
	 * 记录用户的登录日志
	 * 
	 * @param user 用户对象
	 */
	private static void doLog(User user) {
		UserLoginLog userLoginLog = new UserLoginLog();
		userLoginLog.setUserId(user.getId());
		userLoginLog.setLoginType(UserDict.WECHAT_MINI);
		LoginLogController.initBean(userLoginLog, MvcRequest.getMvcRequest());
		LoginLogController.service.create(userLoginLog);

		if (LoginLogController.service.create(userLoginLog) <= 0)
			LOGGER.warning("更新会员登录日志出错");
	}

	/**
	 * 获取 session_key 和 openid。临时登录凭证 code 只能使用一次 会话密钥 session_key
	 * 是对用户数据进行加密签名的密钥。为了应用自身的数据安全，开发者服务器不应该把会话密钥下发到小程序，也不应该对外提供这个密钥
	 * 
	 * @param jsCode 小程序调用 wx.login() 获取“临时登录凭证”的密钥
	 * @return 小程序用户登录的凭证
	 */
	private static UserLoginToken intiToken(String jsCode) {
		String appId = MiniApp.getAppId(), appSecret = MiniApp.getAppSecret();
		String url = String.format(code2Session, appId, appSecret, jsCode);
		String json = NetUtil.simpleGET(url);
		
		Map<String, Object> map = JsonHelper.parseMap(json);

		UserLoginToken token = new UserLoginToken();
		token.setSessionKey(map.get("session_key").toString());
		token.setOpenId(map.get("openid").toString()); // 获取 openid

		return token;
	}

	/**
	 * 凭证验证服务
	 */
	private final static TokenService service = new TokenService(new TokenInfo());

	static {
		service.getInfo().setKeyByConfig("mini_program.SessionId_AesKey");
	}

	/**
	 * 
	 * @param token
	 * @return
	 */
	private static UserLoginToken getSessionId(UserLoginToken token) {
		Objects.requireNonNull(token.getOpenId(), "没有 open id");

		String tokenStr = service.getToken(token.getOpenId() + token.getUserId().toString());
		token.setSessionId(tokenStr);

		return token;
	}

	/**
	 * 解密 header 头中的 openid
	 * 
	 * @param str
	 * @return 0=openid,1 = userid
	 */
	public static Object[] decodeSessionId(String str) {
		String s = service.decrypt(str);
		Object[] r = new Object[2];

		try {
			r[0] = s.substring(0, 27);
			r[1] = Long.parseLong(s.substring(28, s.length()));
		} catch (Throwable e) {
			LOGGER.warning(e);
		}

		return r;
	}
}