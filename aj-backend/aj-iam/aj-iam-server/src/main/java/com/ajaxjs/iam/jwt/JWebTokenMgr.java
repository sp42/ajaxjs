package com.ajaxjs.iam.jwt;

import com.ajaxjs.util.convert.EntityConvert;

/**
 * JWT 管理器
 */
public class JWebTokenMgr {
    private String secretKey = "Df87sD#$%#A";
    private String issuer = "foo@bar.net";


    public JWebTokenMgr(String secretKey, String issuer) {
        this.secretKey = secretKey;
        this.issuer = issuer;
    }

    public JWebTokenMgr() {
    }

    /**
     * 创建 JWT Token
     *
     * @param payload Payload 实例或其子类
     * @return JWT Token
     */
    public JWebToken tokenFactory(Payload payload) {
        payload.setIat(Utils.now());
        payload.setIss(issuer);

        JWebToken token = new JWebToken(payload);
        token.setSignature(signature(token));

        return token;
    }

    /**
     * 创建 JWT Token
     *
     * @param sub
     * @param aud
     * @param expires
     * @return JWT Token
     */
    public JWebToken tokenFactory(String sub, String aud, long expires) {
        Payload payload = new Payload();
        payload.setSub(sub);
        payload.setAud(aud);
        payload.setExp(expires);

        return tokenFactory(payload);
    }

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

        String json = Utils.decode(parts[1]);
        Payload payload = EntityConvert.json2bean(json, Payload.class);

        if (payload == null)
            throw new RuntimeException("Payload is Empty: ");

        if (payload.getExp() == null)
            throw new RuntimeException("Payload 不包含过期字段 exp：" + payload);

        JWebToken token = new JWebToken(payload);
        token.setSignature(parts[2]);

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
        System.out.println(">>>" + token.getSignature());
        System.out.println(":::" + _token);

        return token.getPayload().getExp() > Utils.now() //token not expired
                && token.getSignature().equals(_token); //signature matched
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
        return Utils.hmacSha256(token.headerPayload(), secretKey);
    }
}
