package com.ajaxjs.user.token;

import com.ajaxjs.config.ConfigService;

/**
 * 支持时间戳、加盐、UserId 的 token。与具体数据库无关
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
public class TokenInfo {
	/**
	 * 参数名称
	 */
	public static final String TOKEN = "token";

	/**
	 * Token 的前缀，当前为 token_
	 */
	public static final String TOKEN_PERFIX = TOKEN + "_";

	/**
	 * 默认的超时，为 15分钟
	 */
	public static final int TIMEOUT = 15 * 60000;

	/**
	 * 盐值长度，这个 int 很重要，千万不能让别人知道！
	 */
	public static final int SALT_SIZE = 8;

	/**
	 * 密钥
	 */
	private String key;

	/**
	 * 是否超时判断
	 */
	private boolean usingTimeout;

	/**
	 * 是否加盐
	 */
	private boolean usingSalt;

	/**
	 * 是否加入 UserId
	 */
	private boolean usingUserId;

	/**
	 * 
	 */
	private int timeout;

	public boolean isUsingSalt() {
		return usingSalt;
	}

	public void setUsingSalt(boolean usingSalt) {
		this.usingSalt = usingSalt;
	}

	public boolean isUsingUserId() {
		return usingUserId;
	}

	public void setUsingUserId(boolean usingUserId) {
		this.usingUserId = usingUserId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 读取哪个配置，可以返密钥的。
	 * 
	 * @param config 配置路径
	 */
	public void setKeyByConfig(String config) {
		String key = ConfigService.getValueAsString(config);
		//Objects.requireNonNull(key, "缺少 " + config + " 配置");

		setKey(key);
	}

	public int getTimeout() {
		if (timeout == 0)
			timeout = TIMEOUT;

		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	/**
	 * 读取哪个配置，可以返超时的。
	 * 
	 * @param config 配置路径
	 */
	public void setTimeoutByConfig(String config) {
		setTimeout(ConfigService.getValueAsInt("Symmetric.apiTimeout"));
	}

	public boolean isUsingTimeout() {
		return usingTimeout;
	}

	public void setUsingTimeout(boolean usingTimeout) {
		this.usingTimeout = usingTimeout;
	}
}
