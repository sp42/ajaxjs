package com.ajaxjs.user.password;

import java.util.Date;

import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.user.BaseUserService;
import com.ajaxjs.user.User;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetriCipher;
import com.ajaxjs.util.logger.LogHelper;

public class RestPasswordService extends BaseUserService {
	private static final LogHelper LOGGER = LogHelper.getLog(RestPasswordService.class);

	/**
	 * 
	 */
	private static final String encryptKey = "ErZwd#@$#@D32";

	/**
	 * 通过邮件重置密码
	 * 
	 * @param user 必须包含 email 字段
	 * @return 返回修改密码的 Token
	 * @throws ServiceException
	 */
	public String resetPasswordByEmail(User user) throws ServiceException {
		LOGGER.info("重置密码");

		String email = user.getEmail();

		if (email == null)
			throw new ServiceException("请提交邮件地址");

		user = DAO.findByEmail(email);

		if (user == null)
			throw new ServiceException("该 email：" + email + " 的用户不存在！");

		String expireHex = Long.toHexString(System.currentTimeMillis());
		String emailToken = Encode.md5(encryptKey + email),
				timeToken = SymmetriCipher.AES_Encrypt(expireHex, encryptKey);

		return emailToken + timeToken;
	}

	/**
	 * 验证重置密码的 token 是否有效
	 * 
	 * @param token
	 * @param email 用户提交用于对比的 email
	 * @return
	 * @throws ServiceException
	 */
	public boolean validResetPasswordByEmail(String token, String email) throws ServiceException {
		String emailToken = token.substring(0, 32), timeToken = token.substring(32, token.length());

		if (!Encode.md5(encryptKey + email).equals(emailToken)) {
			throw new ServiceException("非法 email 账号！ " + email);
		}

		String expireHex = SymmetriCipher.AES_Decrypt(timeToken, encryptKey);
		long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
		double result = cha * 1.0 / (1000 * 60 * 60);

		if (result <= 12) {
			// 合法
		} else {
			throw new ServiceException("该请求已经过期，请重新发起！ ");
		}

		return true;
	}
}
