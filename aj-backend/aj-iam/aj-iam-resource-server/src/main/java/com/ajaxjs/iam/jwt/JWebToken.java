package com.ajaxjs.iam.jwt;

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
    public static final String encodedHeader = JwtUtils.encode(JWT_HEADER);

    /**
     * 载荷
     */
    private Payload payload;

    /**
     * 载荷(json)
     * <p>
     * 不同的 json 反序列化，对 key 顺序不一致。所以直接拿 json 字符串加密
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
        String p = JwtUtils.encode(payloadJson);

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