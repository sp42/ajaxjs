package com.ajaxjs.iam.jwt;


import com.ajaxjs.iam.resource_server.Utils;
import lombok.Data;

/**
 * JWT 管理器
 */
@Data
public class JWebTokenMgr {
    /**
     * 密钥
     */
    private String secretKey = "Df87sD#$%#A";

    /**
     * 解析 Token
     *
     * @param tokenStr JWT Token
     */
    public JWebToken parse(String tokenStr) {
        String[] parts = tokenStr.split("\\.");
        if (parts.length != 3)
            throw new IllegalArgumentException("无效 Token 格式");

        if (!JWebToken.encodedHeader.equals(parts[0]))
            throw new IllegalArgumentException("非法的 JWT Header: " + parts[0]);

        String json = JwtUtils.decode(parts[1]);
        Payload payload = Utils.jsonStr2Bean(json, Payload.class);

        if (payload == null)
            throw new RuntimeException("Payload is Empty: ");

        if (payload.getExp() == null)
            throw new RuntimeException("Payload 不包含过期字段 exp：" + payload);

        JWebToken token = new JWebToken(payload);
        token.setSignature(parts[2]);
        token.setPayloadJson(json);

        return token;
    }

    /**
     * 校验是否合法的 Token
     *
     * @param token 待检验的 Token
     * @return 是否合法
     */
    public boolean isValid(JWebToken token) {
        String _token = signature(token);
        boolean isMatch = token.getSignature().equals(_token); // signature matched
        Long exp = token.getPayload().getExp();

        if (exp == 0L) /* 0 表示永不过期，不用检查是否超时 */
            return isMatch;
        else {
            boolean isExp = exp > JwtUtils.now(); // token not expired

            if (!isExp)
                System.out.println("超时");

            return isExp && isMatch;
        }
    }

    /**
     * 校验是否合法的 Token
     *
     * @param tokenStr 待检验的 Token
     * @return 是否合法
     */
    public boolean isValid(String tokenStr) {
        return isValid(parse(tokenStr));
    }

    /**
     * 生成签名
     *
     * @param token Token
     * @return 签名
     */
    public String signature(JWebToken token) {
        return JwtUtils.hmacSha256(token.headerPayload(), secretKey);
    }
}
