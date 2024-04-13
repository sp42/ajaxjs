package com.ajaxjs.s3client.factory;

import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.s3client.BaseS3ClientSigV2;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.MessageDigestHelper;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class NeteaseOSS extends BaseS3ClientSigV2 {
    @Override
    public String getAuthSignature(String data) {
        return getAuthSignature(MessageDigestHelper::getHmacSHA256AsBase64, data);
    }

    @Override
    public boolean putObject(String bucketName, String objectName, byte[] fileBytes) {
        String md5 = MessageDigestHelper.calcFileMD5(null, fileBytes);
        String now = DateUtil.getGMTDate();
        String data = "PUT\n" + md5 + getCanonicalResource(now, bucketName, objectName);
        String url = getFullEndPoint(bucketName) + "/" + objectName;

        ResponseEntity result = Post.put(url, fileBytes, conn -> {
            conn.addRequestProperty(DATE, now);
            conn.addRequestProperty(AUTHORIZATION, getAuthSignature(data));
            conn.addRequestProperty("Content-MD5", md5);
//            conn.addRequestProperty("Content-Length", String.valueOf(fileBytes.length));
//            conn.addRequestProperty("x-nos-entity-type", "json");
        });

        return eTagCheck(result, md5);
    }
}
