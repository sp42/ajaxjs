package com.ajaxjs.s3client;

import com.ajaxjs.s3client.signer_v4.AwsCredentials;
import com.ajaxjs.s3client.signer_v4.CanonicalRequest;
import com.ajaxjs.s3client.signer_v4.SignBuilder;
import com.ajaxjs.util.MessageDigestHelper;
import org.springframework.util.ObjectUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.function.Consumer;

public abstract class BaseS3ClientSigV4 extends BaseS3Client {
    /**
     * 空字符串的 SHA-256 哈希值，用于某些的操作
     */
    public final static String EMPTY_SHA256 = MessageDigestHelper.getSHA256("");

    public SignBuilder initSignatureBuilder(String date, String hash) {
        return initSignatureBuilder(date, hash, null);
    }

    /**
     * @param date
     * @param hash
     * @param extraRequestHeaders 其他自定义的字段，参与签名和实际 HTTP 头请求
     * @return
     */
    public SignBuilder initSignatureBuilder(String date, String hash, Map<String, String> extraRequestHeaders) {
        String accessKey = getConfig().getAccessKey(), secretKey = getConfig().getSecretKey();

        SignBuilder builder = new SignBuilder().setAwsCredentials(new AwsCredentials(accessKey, secretKey))
                .setRegion(getConfig().getRegion())
                .header("x-amz-date", date)
//                .header("host", "s3.us-west-002.backblazeb2.com")
                .header("x-amz-content-sha256", hash);

        if (isSetHost()) {
            String host = getConfig().getEndPoint().replaceAll("http(s?)://", "");
            builder.header("host", host);
        }

        if (!ObjectUtils.isEmpty(extraRequestHeaders)) {
            for (String key : extraRequestHeaders.keySet())
                builder.header(key, extraRequestHeaders.get(key));
        }

        return builder;
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

    public Consumer<HttpURLConnection> setRequestHead(String date, String signature, String hash) {
        return setRequestHead(date, signature, hash, null);
    }

    public Consumer<HttpURLConnection> setRequestHead(String date, String signature, String hash, Map<String, String> extraRequestHeaders) {
        return conn -> {
            conn.setRequestProperty("x-amz-date", date); // 设置请求头 Date
            conn.setRequestProperty("x-amz-content-sha256", hash); // 设置请求头
            conn.setRequestProperty(AUTHORIZATION, signature); // 设置请求头 Authorization

            if (isSetHost()) {
                String host = getConfig().getEndPoint().replaceAll("http(s?)://", "");
                conn.setRequestProperty("host", host);
            }

            if (!ObjectUtils.isEmpty(extraRequestHeaders)) {
                for (String key : extraRequestHeaders.keySet())
                    conn.setRequestProperty(key, extraRequestHeaders.get(key));
            }
        };
    }
}
