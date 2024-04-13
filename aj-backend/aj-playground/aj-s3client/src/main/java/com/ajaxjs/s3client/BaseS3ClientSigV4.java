package com.ajaxjs.s3client;

import com.ajaxjs.s3client.signer_v4.AwsCredentials;
import com.ajaxjs.s3client.signer_v4.CanonicalRequest;
import com.ajaxjs.s3client.signer_v4.SignBuilder;
import com.ajaxjs.util.MessageDigestHelper;

import java.net.URI;
import java.net.URISyntaxException;

public abstract class BaseS3ClientSigV4 extends BaseS3Client {
    /**
     * 空字符串的 SHA-256 哈希值，用于某些的操作
     */
    public final static String EMPTY_SHA256 = MessageDigestHelper.getSHA256("");

    public SignBuilder initSignatureBuilder() {
        String accessKey = getConfig().getAccessKey(), secretKey = getConfig().getSecretKey();

        return new SignBuilder().setAwsCredentials(new AwsCredentials(accessKey, secretKey)).setRegion("auto");
    }

    /**
     * 创建一个指向指定端点的 HTTP 请求对象。
     *
     * @param method   HTTP 请求方法，例如 GET、POST 等。
     * @param endPoint HTTP 请求的端点（URL）。
     * @return HttpRequest 对象，用于发送 HTTP 请求。
     * @throws RuntimeException 如果端点的 URI 语法有误。
     */
    public static CanonicalRequest getCanonicalRequest(String method, String endPoint) {
        try {
            return new CanonicalRequest(method, new URI(endPoint));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
