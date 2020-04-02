package com.ajaxjs.user.token;

import java.util.Date;
import java.util.Objects;
import java.util.Random;

import com.ajaxjs.util.cryptography.AES_Cipher;

public class TokenService {
	private TokenInfo info;

	public TokenService(TokenInfo info) {
		this.info = info;
	}

	public TokenInfo getInfo() {
		return info;
	}

	public void setInfo(TokenInfo info) {
		this.info = info;
	}

	/**
	 * 从加密的 Token 中还原明文 token
	 * 
	 * @param token
	 * @return
	 */
	public String decrypt(String token) {
		String clearText = AES_Cipher.decrypt(token, info.getKey());

		if (clearText == null)
			throw new IllegalAccessError("合法性请求解密的密码不正确");

		clearText = clearText.replaceAll(TokenInfo.TOKEN_PERFIX, "");

		return clearText;
	}

	public boolean verify(String token) {
		Objects.requireNonNull(token, "缺少 token 参数");

		String clearText = decrypt(token);

		if (info.isUsingTimeout())
			checkTimeout(clearText);

		return true; // 所有问题都没有，表示合法的、通过的
	}

	/**
	 * 时间戳检查
	 * 
	 * @param time
	 */
	private void checkTimeout(String time) {
		long datestamp = 0L;

		try {
			datestamp = Long.parseLong(time);
		} catch (NumberFormatException e) {
			throw new NumberFormatException("转换时间戳格式不正确！");
		}

		long diff = new Date().getTime() - datestamp;

		if (diff > info.getTimeout()) {
			throw new IllegalAccessError("请求超时！");
		}
	}

	/**
	 * 生成 Token
	 * 
	 * @param str 要加密的内容
	 * @return 加密后的 token
	 */
	public String getToken(String str) {
		String clearText = TokenInfo.TOKEN_PERFIX;

		if (info.isUsingSalt())
			clearText += getRandomString(TokenInfo.SALT_SIZE);

		if (info.isUsingTimeout())
			clearText += System.currentTimeMillis();

		clearText += str;
		return AES_Cipher.encrypt(TokenInfo.TOKEN_PERFIX + clearText, info.getKey());
	}

	/**
	 * 生成 Token
	 * 
	 * @return 加密后的 token
	 */
	public String getToken() {
		return getToken(""); // 没有要加密的内容
	}

	private static final String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

	/**
	 * 生成指定长度的随机字符，可能包含数字
	 * 
	 * @param length 户要求产生字符串的长度
	 * @return 随机字符
	 */
	public static String getRandomString(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < length; i++) {
			int number = random.nextInt(62);
			sb.append(str.charAt(number));
		}

		return sb.toString();
	}
}
