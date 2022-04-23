package com.ajaxjs.user.common.service;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ajaxjs.user.UserAuth;
import com.ajaxjs.user.common.util.CheckStrength;
import com.ajaxjs.user.common.util.CheckStrength.LEVEL;
import com.ajaxjs.util.cryptography.Digest;
import com.ajaxjs.util.cryptography.encryption.SymmetriCipher;

@Component
public class ResetPasswordService {
	@Value("${ResetPassword.encryptKey}")
	private String encryptKey;

	/**
	 * 生成重置密码的 Token（ for 邮件） 这 Token 在有效期内一直有效 TODO，令其无效。 该签名方法不能公开
	 * 
	 * https://www.cnblogs.com/shenliang123/p/3266770.html
	 * https://blog.wamdy.com/archives/1708.html
	 * 
	 * @param email    邮件地址
	 * @param tenantId 租户 id
	 * @return Token 签名
	 */
	public String makeEmailToken(String email, int tenantId) {
		String expireHex = Long.toHexString(System.currentTimeMillis());
		String emailToken = Digest.getSHA1(encryptKey + email), timeToken = SymmetriCipher.AES_Encrypt(expireHex, encryptKey);

		return emailToken + timeToken;
	}

	@Value("${ResetPassword.tokenTimeout}")
	int tokenTimeout;

	/**
	 * 验证重置密码的 token 是否有效
	 * 
	 * @param token
	 * @param email 用户提交用于对比的 email
	 * @return true = 通过
	 */
	public boolean checkEmailToken(String token, String email) {
		String emailToken = token.substring(0, 40), timeToken = token.substring(40, token.length());

		if (!Digest.getSHA1(encryptKey + email).equals(emailToken))
			throw new SecurityException("非法 email 账号！ " + email);

		String expireHex = SymmetriCipher.AES_Decrypt(timeToken, encryptKey);
		long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
		double result = cha * 1.0 / (1000 * 60 * 60);

		if (result <= tokenTimeout)
			// 合法
			return true;
		else
			throw new IllegalAccessError("该请求已经过期，请重新发起！ ");
	}

	@Autowired
	LoginService loginService;

	/**
	 * 更新用户密码
	 * 
	 * @param user        用户信息
	 * @param newPassword 用户输入的新密码
	 * @return 是否修改成功
	 */
	public boolean updatePwd(Map<String, Object> user, String newPassword) {
		// 检测密码强度
		LEVEL passwordLevel = CheckStrength.getPasswordLevel(newPassword);

		if (passwordLevel == LEVEL.EASY)
			throw new UnsupportedOperationException("密码强度太低");

		newPassword = loginService.encodePassword(newPassword); 

		if (newPassword.equalsIgnoreCase(user.get("password").toString()))
			throw new UnsupportedOperationException("新密码与旧密码一致，没有修改");

		UserAuth updateAuth = new UserAuth();
		updateAuth.setId(Long.parseLong(user.get("authId") + ""));
		updateAuth.setCredential(newPassword);

		if (com.ajaxjs.user.common.service.UserDAO.UserAuthDAO.update(updateAuth)) // 密码修改成功
			return true;

		return false; // 密码修改失败
	}

}
