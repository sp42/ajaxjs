package com.ajaxjs.s3client.signer_v4;

import com.ajaxjs.s3client.util.S3SigV4Utils;
import com.ajaxjs.util.MessageDigestHelper;
import lombok.Data;
import lombok.experimental.Accessors;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class SignBuilder {
    private AwsCredentials awsCredentials;

    private String region = "us-east-1";

    private static final String X_AMZ_DATE = "X-Amz-Date";

    private static final String AUTH_TAG = "AWS4";

    private static final String ALGORITHM = AUTH_TAG + "-HMAC-SHA256";

    private Map<String, String> map = new HashMap<>();

    /**
     * 添加一个请求头部。
     *
     * @param name  头部名称。
     * @param value 头部的值。
     * @return 当前签名构建器对象。
     */
    public SignBuilder header(String name, String value) {
        map.put(name, value);

        return this;
    }

    /**
     * 构建签名字符串。
     * 使用给定的密钥和凭证范围对指定字符串进行签名。
     *
     * @param canonicalRequest HTTP 请求对象。
     * @param contentSha256    请求内容的 SHA256 散列值。
     * @return 签名后的字符串，以 Base16 编码格式返回。
     */
    private String build(CanonicalRequest canonicalRequest, String contentSha256) {
        // 构建规范化的头部信息
        CanonicalHeaders canonicalHeaders = CanonicalHeaders.build(map);
        // 从头部中提取日期信息
        String date = canonicalHeaders.getFirstValue(X_AMZ_DATE).orElseThrow(() -> new RuntimeException("headers missing '" + X_AMZ_DATE + "' header"));
        // 构建认证范围对象
        CredentialScope scope = new CredentialScope(date, canonicalRequest.getService(), region);
        String _scope = scope.get();
        String canonicalRequestString = canonicalRequest.getMethod() +
                "\n" + canonicalRequest.getNormalizePath() +
                "\n" + canonicalRequest.getNormalizeQuery() +
                "\n" + canonicalHeaders.getCanonicalizedHeaders() +
                "\n" + canonicalHeaders.getNames() +
                "\n" + contentSha256;
        String stringToSign = ALGORITHM + "\n" + date + "\n" + _scope + "\n" + MessageDigestHelper.getSHA256(canonicalRequestString);
        byte[] kSecret = (AUTH_TAG + awsCredentials.getSecretKey()).getBytes(StandardCharsets.UTF_8);
        byte[] kDate = S3SigV4Utils.hmacSha256(kSecret, scope.getDateWithoutTimestamp());
        byte[] kRegion = S3SigV4Utils.hmacSha256(kDate, scope.getRegion());
        byte[] kService = S3SigV4Utils.hmacSha256(kRegion, scope.getService());
        byte[] kSigning = S3SigV4Utils.hmacSha256(kService, CredentialScope.TERMINATION_STRING);
        String signature = S3SigV4Utils.base16Encode(S3SigV4Utils.hmacSha256(kSigning, stringToSign)).toLowerCase();

        return ALGORITHM + " Credential=" + awsCredentials.getAccessKey() + "/" + _scope + ", " + "SignedHeaders=" + canonicalHeaders.getNames() + ", " + "Signature=" + signature;
    }

    public String getS3Signature(CanonicalRequest request, String contentSha256) {
        request.setService("s3");
        return build(request, contentSha256);
    }

    public String getGlacierSignature(CanonicalRequest request, String contentSha256) {
        request.setService("glacier");
        return build(request, contentSha256);
    }
}