package com.ajaxjs.iam.client.model;

import com.ajaxjs.iam.Utils;

import lombok.Data;

/**
 * JWT Token
 */
@Data
public class JWebToken {
	/**
	 * 头部
	 */
	public static final String JWT_HEADER = "{\"alg\":\"HS256\",\"typ\":\"JWT\"}";

	/**
	 * 头部的 Base64 编码
	 */
	public static final String encodedHeader = Utils.encode(JWT_HEADER);

	/**
	 * 载荷
	 */
	private Payload payload;

	/**
	 * 载荷(json)
	 */
	private String payloadJson;

	/**
	 * 签名部分
	 */
	private String signature;

	public JWebToken(Payload payload) {
		this.payload = payload;
	}

	/**
	 * 头部 + payload
	 *
	 * @return 头部 + Payload
	 */
	public String headerPayload() {
		String p = Utils.encode(payloadJson);

		return encodedHeader + "." + p;
	}

	/**
	 * 返回 Token 的字符串形式
	 *
	 * @return Token
	 */
	@Override
	public String toString() {
		return headerPayload() + "." + signature;
	}

}