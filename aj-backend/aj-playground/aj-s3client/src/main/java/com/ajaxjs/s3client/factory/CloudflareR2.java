package com.ajaxjs.s3client.factory;

import com.ajaxjs.net.http.*;
import com.ajaxjs.s3client.BaseS3ClientSigV4;
import com.ajaxjs.s3client.util.S3SigV4Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * Cloudflare 的 R2 客户端
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloudflareR2 extends BaseS3ClientSigV4 {
    @Override
    public String listBucket() {
        String now = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String url = getConfig().getEndPoint();
        String signature = initSignatureBuilder(now, EMPTY_SHA256).getS3Signature(getCanonicalRequest(HttpConstants.GET, url), EMPTY_SHA256);
        ResponseEntity result = Get.get(url, setRequestHead(now, signature, EMPTY_SHA256));

        return result.toString();
    }

    @Override
    public Map<String, String> listBucketXml() {
        String now = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String url = getConfig().getEndPoint();
        String signature = initSignatureBuilder(now, EMPTY_SHA256).getS3Signature(getCanonicalRequest(HttpConstants.GET, url), EMPTY_SHA256);

        return Get.apiXML(url, setRequestHead(now, signature, EMPTY_SHA256));
    }

    @Override
    public boolean createBucket(String bucketName) {
        String now = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String url = getConfig().getEndPoint() + "/" + bucketName;
        String signature = initSignatureBuilder(now, EMPTY_SHA256).getS3Signature(getCanonicalRequest(HttpConstants.PUT, url), EMPTY_SHA256);

        return check(Post.put(url, null, setRequestHead(now, signature, EMPTY_SHA256)));
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        String now = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String url = getConfig().getEndPoint() + "/" + bucketName;
        String signature = initSignatureBuilder(now, EMPTY_SHA256).getS3Signature(getCanonicalRequest(HttpConstants.DELETE, url), EMPTY_SHA256);

        return check(Delete.del(url, setRequestHead(now, signature, EMPTY_SHA256)));
    }

    @Override
    public boolean putObject(String bucketName, String objectName, byte[] fileBytes) {
        String now = S3SigV4Utils.now();
        String url = getConfig().getEndPoint() + "/" + bucketName + "/" + objectName;
        String contentSha256 = S3SigV4Utils.calcFileSHA256(fileBytes);
        String signature = initSignatureBuilder(now, contentSha256).getS3Signature(getCanonicalRequest(HttpConstants.PUT, url), contentSha256);

        return check(Post.put(url, fileBytes, setRequestHead(now, signature, contentSha256)));
    }

    @Override
    public boolean getObject(String bucketName, String objectName) {
        return false;
    }

    @Override
    public boolean deleteObject(String bucketName, String objectName) {
        return false;
    }
}
