/**
 * Copyright Sp42 frank@ajaxjs.com Licensed under the Apache License, Version
 * 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package com.ajaxjs.oss;

import com.ajaxjs.net.http.Get;
import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.net.http.SetConnection;
import com.ajaxjs.util.ListUtils;
import com.ajaxjs.util.Digest;
import com.ajaxjs.util.DateUtil;
import com.ajaxjs.util.ObjectHelper;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;

/**
 * 阿里云 OSS Api 版本（不使用 sdk）工具类
 * <p>
 * 阿里云官方文档地址：<a href="https://helpcdn.aliyun.com/document_detail/31947.html">...</a>
 */
public class OssUpload implements IFileUpload {
    @Value("${S3Storage.Oss.bucket}")
    private String ossBucket = "leyou-cxk";

    @Value("${S3Storage.Oss.accessKeyId}")
    private String accessKeyId;

    @Value("${S3Storage.Oss.secretAccessKey}")
    private String secretAccessKey;

    @Value("${S3Storage.Oss.endpoint}")
    private String endpoint;

    /**
     * 上传
     *
     * @param filename 文件名
     * @param content  文件内容
     * @return 是否成功
     */
    @Override
    public boolean upload(String filename, byte[] content) {
        String date = DateUtil.getGMTDate();
        String signResourcePath = "/" + ossBucket + "/" + filename;
        String signature = Digest.doHmacSHA1(secretAccessKey, buildPutSignData(date, signResourcePath));
        String url = "http://" + ossBucket + "." + endpoint + "/" + filename;

        ResponseEntity result = Post.put(url, content, conn -> {
            conn.setRequestProperty("Date", date);
            conn.setRequestProperty("Authorization", "OSS " + accessKeyId + ":" + signature);
        });

        // 判定是否上传成功
        return result.getHttpCode() == 200 && result.getConnection().getHeaderField("ETag") != null;
    }

    /**
     * 读取
     *
     * @param key KEY
     * @return 文件
     */
    public String getOssObj(String key) {
        String signResourcePath = "/" + ossBucket + key;
        String date = DateUtil.getGMTDate();
        String signature = Digest.doHmacSHA1(secretAccessKey, buildGetSignData(date, signResourcePath));

        Map<String, String> head = ObjectHelper.hashMap("Date", date, "Authorization", "OSS " + accessKeyId + ":" + signature);
        String url = "http://" + ossBucket + "." + endpoint;

        return Get.get(url + key, SetConnection.map2header(head)).toString();
    }

    private static String buildGetSignData(String date, String canonicalResource) {
        return "GET" + "\n" + "\n" + "\n" + date + "\n" + canonicalResource;
    }

    private static String buildPutSignData(String date, String canonicalResource) {
        return "PUT" + "\n" + "\n" + "\n" + date + "\n" + canonicalResource;
    }

}