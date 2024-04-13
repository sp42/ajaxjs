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
public class AliyunOSS extends BaseS3ClientSigV2 {
    @Override
    public String getAuthSignature(String data) {
        return getAuthSignature(MessageDigestHelper::getHmacSHA1AsBase64, data);
    }

    @Override
    public boolean putObject(String bucketName, String objectName, byte[] fileBytes) {
        String now = DateUtil.getGMTDate();
        String data = "PUT\n" + getCanonicalResource(now, bucketName, objectName);
        String url = getFullEndPoint(bucketName) + "/" + objectName;

        ResponseEntity result = Post.put(url, fileBytes, setRequestHead(now, data));

        return eTagCheck(result, null);
    }
}
