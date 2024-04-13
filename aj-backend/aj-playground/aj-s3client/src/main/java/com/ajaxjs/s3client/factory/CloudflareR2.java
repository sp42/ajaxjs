package com.ajaxjs.s3client.factory;

import com.ajaxjs.net.http.*;
import com.ajaxjs.s3client.BaseS3ClientSigV4;
import com.ajaxjs.s3client.util.S3SigV4Utils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

/**
 * S3 客户端
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloudflareR2 extends BaseS3ClientSigV4 {
    @Override
    public String listBucket() {
        String date = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String signature = initSignatureBuilder()
                .header("x-amz-date", date)
                .header("x-amz-content-sha256", EMPTY_SHA256)
                .getS3Signature(getCanonicalRequest(HttpConstants.GET, getConfig().getEndPoint()), EMPTY_SHA256);

        ResponseEntity result = Get.get(getConfig().getEndPoint(), conn -> {  // 执行 PUT 请求上传文件
            conn.setRequestProperty("x-amz-date", date); // 设置请求头 Date
            conn.setRequestProperty("x-amz-content-sha256", EMPTY_SHA256); // 设置请求头 Date
            conn.setRequestProperty(AUTHORIZATION, signature); // 设置请求头 Authorization
        });

        return result.toString();
    }

    @Override
    public Map<String, String> listBucketXml() {
        return null;
    }

    @Override
    public boolean createBucket(String bucketName) {
        String date = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String url = getConfig().getEndPoint() + "/" + bucketName;
        String signature = initSignatureBuilder()
                .header("x-amz-date", date)
                .header("x-amz-content-sha256", EMPTY_SHA256)
                .getS3Signature(getCanonicalRequest(HttpConstants.PUT, url), EMPTY_SHA256);

        ResponseEntity result = Post.put(url, null, conn -> {  // 执行 PUT 请求上传文件
            conn.setRequestProperty("x-amz-date", date); // 设置请求头 Date
            conn.setRequestProperty("x-amz-content-sha256", EMPTY_SHA256); // 设置请求头 Date
            conn.setRequestProperty(AUTHORIZATION, signature); // 设置请求头 Authorization
        });

        if (result.getHttpCode() != 200) {
            System.err.println("异常: " + result);
            return false;
        }

        return true;
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        String date = S3SigV4Utils.now();// 获取当前 GMT 时间，用于请求头 Date 字段
        String url = getConfig().getEndPoint() + "/" + bucketName;
        String signature = initSignatureBuilder()
                .header("x-amz-date", date)
                .header("x-amz-content-sha256", EMPTY_SHA256)
                .getS3Signature(getCanonicalRequest(HttpConstants.DELETE, url), EMPTY_SHA256);

        ResponseEntity result = Delete.del(url, conn -> {
            conn.setRequestProperty("x-amz-date", date); // 设置请求头 Date
            conn.setRequestProperty("x-amz-content-sha256", EMPTY_SHA256); // 设置请求头 Date
            conn.setRequestProperty(AUTHORIZATION, signature); // 设置请求头 Authorization
        });

        if (result.getHttpCode() != 200 && result.getHttpCode() != 204) {
            System.err.println("异常: " + result);
            return false;
        }

        return true;
    }

    @Override
    public boolean putObject(String bucketName, String objectName, byte[] fileBytes) {
        String date = S3SigV4Utils.now();
        String url = getConfig().getEndPoint() + "/" + bucketName + "/" + objectName;
        String contentSha256 = S3SigV4Utils.calcFileSHA256(fileBytes);

        String signature = initSignatureBuilder()
                .header("x-amz-date", date)
                .header("x-amz-content-sha256", contentSha256)
                .getS3Signature(getCanonicalRequest(HttpConstants.PUT, url), contentSha256);

        ResponseEntity result = Post.put(url, fileBytes, conn -> {
            conn.setRequestProperty("x-amz-date", date); // 设置请求头 Date
            conn.setRequestProperty("x-amz-content-sha256", contentSha256); // 设置请求头 Date
            conn.setRequestProperty(AUTHORIZATION, signature); // 设置请求头 Authorization
        });

        if (result.getHttpCode() != 200) {
            System.err.println("异常: " + result);
            return false;
        }

        return true;
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
