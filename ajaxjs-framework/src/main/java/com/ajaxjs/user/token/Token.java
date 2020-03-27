package com.ajaxjs.user.token;

import java.util.Date;
import java.util.Random;

import com.ajaxjs.util.cryptography.SymmetricCipher;
import com.ajaxjs.util.Encode;

/**
 * 该类的作用： 1）一些密码字段的基类，涉及 AES 密钥和盐值 2）如何生成指定长度的随机密码
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class Token {
	/**
	 * 前后端约定好的 AES 密钥
	 */

	/**
	 * 盐值长度，这个 int 很重要，千万不能让别人知道！
	 */
	private static final int saltSize = 8;

	/**
	 * 生成随机密码的字符和数字（从0-9a-z中选）
	 */
	private static final String text = "0123456789abcdefghijklmnopqrstuvwxyz";

	/**
	 * 生成随机密码。可以控制生成的密码长度， 密码由数字和字母组成。
	 * 
	 * @return 随机密码
	 */
	public synchronized static String initSalt() {
		StringBuffer sb = new StringBuffer();
		Random random = new Random();

		for (int i = 0; i < saltSize; i++)
			sb.append(text.charAt(random.nextInt(text.length())));

		return sb.toString();
	}

	/**
	 * 生成 Token
	 * 
	 * @param realPassword 明文密码
	 * @param aesKey AES 密钥
	 * @return 加密结果
	 */
	public static String getToken(String realPassword, String aesKey) {
		String hashedPassword = Encode.getSHA1(realPassword);
		return SymmetricCipher.AES_Encrypt(initSalt() + hashedPassword, aesKey);
	}

	/**
	 * 注册成功后，服务器保存的用户密码是 StoreToken
	 * 
	 * @param clientToken 客户端提交的 token，注册时客户端在注册接口里提交的
	 * @param aesKey 客户端、服务端公用 AESkey
	 * @param serverAESkey 服务器存储专用密钥
	 * @return 服务器保存的 token
	 */
	public static String getStoreToken(String clientToken, String aesKey, String serverAESkey) {
		String hashedPassword = removeSalt(clientToken, aesKey);
		return SymmetricCipher.AES_Encrypt(initSalt() + hashedPassword, serverAESkey);
	}

	/**
	 * 返回 HashedPassword
	 * 
	 * @param token Token
	 * @param key AES 密钥
	 * @return HashedPassword
	 */
	private static String removeSalt(String token, String key) {
		String salted = SymmetricCipher.AES_Decrypt(token, key);
		return salted.substring(saltSize, salted.length());
	}

	/**
	 * 密码是否正确
	 * 
	 * @param clientToken 客户端提交的 token
	 * @param storeToken 服务器保存的 token
	 * @param aesKey 客户端、服务端公用 AESkey
	 * @param serverAESkey 服务器存储专用密钥
	 * @return true 表示为合法密码
	 */
	public static boolean auth(String clientToken, String storeToken, String aesKey, String serverAESkey) {
		String client = removeSalt(clientToken, aesKey), server = removeSalt(storeToken, serverAESkey);
		return client.equals(server);
	}

	/**
	 * 生成客户端自动登录 token
	 * 
	 * @param storeToken 服务器保存的 token
	 * @param serverAESkey 服务器存储专用密钥
	 * @return 客户端 slat 和自动登录 token
	 */
	public static String[] getAutoLoginToken(String storeToken, String serverAESkey) {
		String hashedPassword = removeSalt(storeToken, serverAESkey);
		String slat = initSalt();
		
		return new String[] { slat, SymmetricCipher.AES_Encrypt(slat + hashedPassword, serverAESkey) };
	}

	/**
	 * 创建时间戳 Token
	 * 
	 * @param aesKey AES 密钥
	 * @return 时间戳 Token
	 */
	public static String getTimeStampToken(String aesKey) {
		long timeStamp = System.currentTimeMillis() / 1000;
		return SymmetricCipher.AES_Encrypt(timeStamp + "", aesKey);
	}

	/**
	 * 从加密的 Token 中还原时间戳
	 * 
	 * @param token 时间戳 Token
	 * @param aesKey AES 密钥
	 * @return
	 */
	public static Date decryptTimeStampToken(String token, String aesKey) {
		String timeStamp = SymmetricCipher.AES_Decrypt(token + "", aesKey);
		long t = Long.parseLong(timeStamp);
		
		return new Date(t);
	}
}
