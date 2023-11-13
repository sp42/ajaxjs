package com.ajaxjs.iam.jwt;

import com.ajaxjs.util.convert.ConvertToJson;
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
     * 签名部分
     */
    private String signature;

    public JWebToken(Payload payload) {
        this.payload = payload;
    }

    /**
     * 头部 + Payload
     *
     * @return 头部 + Payload
     */
    public String headerPayload() {
        String json = ConvertToJson.toJson(payload);
        String p = Utils.encode(json);

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