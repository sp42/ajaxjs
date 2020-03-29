package com.ajaxjs.user.login;

import java.util.Map;

import com.ajaxjs.net.http.NetUtil;
import com.ajaxjs.user.UserDict;
import com.ajaxjs.user.model.User;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Weibo {
	private static final LogHelper LOGGER = LogHelper.getLog(Weibo.class);

	private final static String CLIENT_ID = "360568732";
	private final static String CLIENT_SERCRET = "f6b37692f8efbdda5496fd6b18b9e534";
	private final static String GET_TOKEN_URL = "https://api.weibo.com/oauth2/access_token";
	private final static String REDIRECT_URI = "http://localhost:8080/shop/callback.jsp";
	private final static String GET_USER_INFO = "https://api.weibo.com/2/users/show.json";
//	private final static String GET_TOKEN_INFO_URL = "https://api.weibo.com/oauth2/get_token_info";
//	private final static String STATE = "register";

//	public String weiboLogin() {
//		String result = null;
//
//		String error_code = request.getParameter("error_code");
//		if (!CommonUtil.isEmptyString(error_code)) {
//			result = "微博授权失败，原因" + error_code;
//		} else {
//			String code = request.getParameter("code");
//			String state = request.getParameter("state");
//			String access_token = "";
//			String expires_in = "";
//			String uid = "";
//
//			if (STATE.equals(state)) {
//				// 获取token
//				JSONObject token = getAccessToken(code);
//				access_token = token.getString("access_token");
//				uid = token.getString("uid");
//				expires_in = String.valueOf(token.getInt("expires_in"));
//				logger.info("token:" + token);
//			} else {
//				result = "微博授权失败";
//			}
//			// 查询该用户信息
////		 本地验证 uid 是否存在，如果存在则自动登入绑定账户
//			OauthUser oauthUser = oauthUserService.findWeiboByIdentifier(uid);
//			Master master = null;
//
//			if (oauthUser != null) {
//				master = masterInfoDao.findById(oauthUser.getMaster_id());
//			} else {
////		 未绑定账号，进行绑定或者注册
//				// 获取用户信息
//				JSONObject userInfo = getUserInfo(access_token, uid);
//				logger.info("用户信息" + userInfo);
//				String nickname = userInfo.getString("screen_name");
//				String profile_image_url = userInfo.getString("profile_image_url");
//				String gender = "f".equals(userInfo.getString("gender")) ? "1" : "0";
//
//				// 向第三方登录表中添加数据
//				OauthUser user = new OauthUser();
//				user.setId(UUID.randomUUID().toString());
//				String master_id = UUID.randomUUID().toString();
//				user.setMaster_id(master_id);
//				user.setIdentity_type("weibo");
//				user.setIdentifier(uid);
//				user.setCredential(access_token);
//				user.setExpires_in(expires_in);
//				user.setStatus("0");
//				oauthUserService.insert(user);
//
//				// 向用户表中添加默认数据
//				Master masterUser = new Master();
//				masterUser.setId(master_id);
//				masterUser.setNickname(nickname);
//				masterUser.setHead_portrait(profile_image_url);
//				masterUser.setSex(gender);
//				// 由于第三方登录没有用户名密码，而且该字段在数据库中不为空，在此设置默认用户名密码
//				masterUser.setUser_name("wbu" + access_token.substring(0, 9));
//				masterUser.setPassword("wbp" + access_token.substring(0, 9));
//				masterInfoService.insertDefault(masterUser);
//
//				master = masterUser;
//			}
//
//			result = CMS_Result.ok();
//
//		}
//
//		return result;
//	}

	/**
	 * 获取AccessToken
	 */
	public static Map<String, Object> getAccessToken(String code) {
		StringBuilder sb = new StringBuilder();
		sb.append("grant_type=authorization_code");
		sb.append("&client_id=" + CLIENT_ID);
		sb.append("&client_secret=" + CLIENT_SERCRET);
		sb.append("&redirect_uri=" + REDIRECT_URI);
		sb.append("&code=" + code);

		LOGGER.info("获取 AccessToken ：" + sb.toString());
		String result = NetUtil.post(GET_TOKEN_URL, sb.toString());
		LOGGER.info("AccessToken 接口返回：" + result);

		/**
		 * 返回数据 { "access_token": "ACCESS_TOKEN", "expires_in": 1234,
		 * "remind_in":"798114", "uid":"12341234" }
		 */
		return JsonHelper.parseMap(result);
	}

	/**
	 * 获取用户信息。 返回参数文档 http://open.weibo.com/wiki/2/users/show
	 * 
	 * @param accessToken
	 * @param uid         查询的用户ID
	 * @return
	 */
	public static Map<String, Object> getUserInfo(String accessToken, String uid) {
		StringBuilder sb = new StringBuilder();
		sb.append("?access_token=" + accessToken);
		sb.append("&uid=" + uid);
		String result = NetUtil.simpleGET(GET_USER_INFO + sb.toString());

		if (CommonUtil.isEmptyString(result)) {
			throw new NullPointerException("访问微博 GET_USER_INFO 接口失败");
		} else {
			Map<String, Object> map = JsonHelper.parseMap(result);

			Object errCode = map.get("error_code");
			if (errCode != null && (int) errCode != 0) {
				String msg = "Error： " + errCode + " " + map.get("error");

				if (map.get("error_description") != null)
					msg += map.get("error_description");

				throw new NullPointerException(msg);
			}

			return JsonHelper.parseMap(result);
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param map
	 * @return
	 */
	public static User getUserInfo(Map<String, Object> map) {
		User user = new User();
		user.setName((String) map.get("screen_name"));
		user.setAvatar((String) map.get("profile_image_url"));
		user.setSex("f".equals(map.get("gender")) ? UserDict.FEMALE : UserDict.MALE);

		return user;
	}
}
