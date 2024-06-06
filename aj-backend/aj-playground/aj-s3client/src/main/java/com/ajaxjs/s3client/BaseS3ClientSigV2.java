package com.ajaxjs.s3client;

import com.ajaxjs.net.http.Delete;
import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.util.DateUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.net.HttpURLConnection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Consumer;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseS3ClientSigV2 extends BaseS3Client {
    /**
     * 生成验证的签名
     *
     * @param data 数据
     * @return 验证的签名字符串
     */
    abstract public String getAuthSignature(String data);

    @Override
    public boolean createBucket(String bucketName) {
        String now = DateUtil.getGMTDate();
        String data = "PUT\n" + getCanonicalResource(now, bucketName, "");
        String url = getFullEndPoint(bucketName);

        return check(Post.put(url, null, setRequestHead(now, data)));
    }

    @Override
    public boolean deleteBucket(String bucketName) {
        String now = DateUtil.getGMTDate();
        String data = "DELETE\n" + getCanonicalResource(now, bucketName, "");
        String url = getFullEndPoint(bucketName);

        return check(Delete.del(url, setRequestHead(now, data)));
    }

    @Override
    public String listBucket() {
        String now = DateUtil.getGMTDate();
        String canonicalHeaders = "", canonicalResource = "/";
        String data = "GET\n\n\n" + now + "\n" + canonicalHeaders + canonicalResource;
        String url = getEndPoint();

        return Get.get(url, setRequestHead(now, data)).toString();
    }

    @Override
    public Map<String, String> listBucketXml() {
        String now = DateUtil.getGMTDate();
        String canonicalHeaders = "", canonicalResource = "/";
        String data = "GET\n\n\n" + now + "\n" + canonicalHeaders + canonicalResource;
        String url = getEndPoint();

        return Get.apiXML(url, setRequestHead(now, data));
    }

    @Override
    public boolean getObject(String bucketName, String objectName) {
        String now = DateUtil.getGMTDate();
        String data = "GET\n" + getCanonicalResource(now, bucketName, objectName);
        String url = getFullEndPoint(bucketName) + "/" + objectName;

        Get.get(url, setRequestHead(now, data)).toString(); // TODO save disk

        return true;
    }

    @Override
    public boolean deleteObject(String bucketName, String objectName) {
        String now = DateUtil.getGMTDate();// 获取当前时间，用于请求头
        String data = "DELETE\n" + getCanonicalResource(now, bucketName, objectName);
        String url = getFullEndPoint(bucketName) + "/" + objectName;

        return check(Delete.del(url, setRequestHead(now, data)));
    }

    protected String getAuthSignature(BiFunction<String, String, String> callback, String data) {
        String signature = callback.apply(getConfig().getSecretKey(), data);

        return getSignValue(signature);
    }

    protected String getSignValue(String signature) {
        return getConfig().getRemark() + " " + getConfig().getAccessKey() + ":" + signature;
    }

    /**
     * 构建资源签名路径
     * 该方法用于构建一个包含当前时间、规范化的头信息和资源路径的字符串，主要用于授权访问AWS S3对象。
     *
     * @param now        表示当前时间的字符串，格式为特定的日期时间格式，用于签名中记录请求的时间。
     * @param bucketName S3存储桶的名称，是签名中必须包含的路径部分。
     * @param objectName S3对象（文件）的名称，是签名中指定的具体资源。
     * @return 返回一个字符串，该字符串包括换行符、当前时间、空的规范化头信息以及规范化的资源路径，用于构建签名。
     */
    protected static String getCanonicalResource(String now, String bucketName, String objectName) {
        String canonicalHeaders = "", canonicalResource = "/" + bucketName + "/" + objectName;

        // 拼接字符串返回，包括换行符、当前时间、空的规范化头以及资源路径
        return "\n\n" + now + "\n" + canonicalHeaders + canonicalResource;
    }

    // 判断上传是否成功
    // 根据 HTTP 返回码和 ETag 是否存在来判断上传结果
    protected static boolean eTagCheck(ResponseEntity result, String hash) {
        boolean isOk = true;
        String ETag = result.getConnection().getHeaderField("ETag");

        if (ETag == null)
            isOk = false;

        if (result.getHttpCode() != 200)
            isOk = false;

        if (hash != null && ETag != null && !ETag.equalsIgnoreCase("\"" + hash + "\""))
            isOk = false;

        if (!isOk)
            System.err.println(result);

        return isOk;
    }

    public Consumer<HttpURLConnection> setRequestHead(String now, String data) {
        return conn -> {
            conn.addRequestProperty(DATE, now);
            conn.addRequestProperty(AUTHORIZATION, getAuthSignature(data));   // 设置请求授权头和日期头
        };
    }

    public String getEndPoint() {
        return HTTPS + getConfig().getEndPoint();
    }

    public String getFullEndPoint(String bucketName) {
        return HTTPS + bucketName + "." + getConfig().getEndPoint();
    }
}
