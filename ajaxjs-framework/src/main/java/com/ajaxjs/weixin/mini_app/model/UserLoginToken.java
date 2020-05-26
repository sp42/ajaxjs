package com.ajaxjs.weixin.mini_app.model;

import java.util.Objects;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.user.service.TokenMaker;
import com.ajaxjs.util.cryptography.Symmetri_Cipher;
import com.ajaxjs.weixin.open_account.model.BaseModel;

/**
 * 小程序用户登录的凭证
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class UserLoginToken extends BaseModel {
	/**
	 * 用户 id
	 */
	private Long userId;

	private String openid;

	/**
	 * 服务端会话密钥（不能外泄）
	 */
	private String session_key;

	/**
	 * 客户端密钥（业务登录凭证）
	 * 
	 * @param token
	 * @return
	 */
	public UserSession getSessionId() {
		Objects.requireNonNull(openid, "没有 open id");

		String sessionId = TokenMaker.encryptAES(ConfigService.getValueAsString("mini_program.SessionId_AesKey"))
				.apply(openid + userId);

		UserSession session = new UserSession();
		session.setUserId(userId);
		session.setSessionId(sessionId);

		return session;
	}

	/**
	 * 解密 header 头中的 openid
	 * 
	 * @param str
	 * @return 0=openid,1 = userid
	 */
	public static Object[] decodeSessionId(String str) {
		String s = Symmetri_Cipher.AES_Decrypt(str, ConfigService.getValueAsString("mini_program.SessionId_AesKey"));
		Object[] r = new Object[2];

		try {
			r[0] = s.substring(0, 27);
			r[1] = Long.parseLong(s.substring(28, s.length()));
		} catch (Throwable e) {
			e.printStackTrace();
		}

		return r;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
}
