package com.ajaxjs.s3client;

import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.s3client.factory.AliyunOSS;
import com.ajaxjs.s3client.factory.NeteaseOSS;
import com.ajaxjs.s3client.model.Config;
import com.ajaxjs.util.MessageDigestHelper;
import com.ajaxjs.util.io.FileHelper;
import org.junit.Test;

import java.io.File;
import java.util.Map;

public class TestFactory {
    Config nsoCfg = new Config();

    NeteaseOSS nso = new NeteaseOSS();

    Config aliCfg = new Config();
    AliyunOSS ali = new AliyunOSS();

    {
        Map<String, Object> cfg = TestBase.getConfigFromYml("application.yml");
        nsoCfg.setEndPoint((String) cfg.get("S3Storage_Nso_api"));
        nsoCfg.setAccessKey((String) cfg.get("S3Storage_Nso_accessKeyId"));
        nsoCfg.setSecretKey((String) cfg.get("S3Storage_Nso_accessSecret"));
        nsoCfg.setBucketName((String) cfg.get("S3Storage_Nso_bucket"));
        nsoCfg.setRemark("NOS");
        nso.setConfig(nsoCfg);

        aliCfg.setEndPoint((String) cfg.get("S3Storage_Oss_endpoint"));
        aliCfg.setAccessKey((String) cfg.get("S3Storage_Oss_accessKeyId"));
        aliCfg.setSecretKey((String) cfg.get("S3Storage_Oss_secretAccessKey"));
        aliCfg.setBucketName((String) cfg.get("S3Storage_Oss_bucket"));
        aliCfg.setRemark("OSS");

        ali.setConfig(aliCfg);
    }


    File file = new File(("D:\\code\\aj\\aj-business\\aj-base\\src\\test\\resources\\img.png"));
    byte[] content = FileHelper.openAsByte(file);

    @Test
    public void testNso() {
//        assertTrue(nso.createBucket("test6767ffg"));
//        assertTrue(nso.deleteBucket("test6767ffg"));
        nso.listBucket();
//        nso.listBucketXml();
//        assertTrue(nso.putObject("test2.png", content));
//        nso.getObject("test2.png");
//        nso.deleteObject("test2.png");
//        nso.createEmptyFile("test.txt");
    }

    @Test
    public void testAli() {
//        assertTrue(ali.createBucket("test6765ffg"));
//        assertTrue(ali.deleteBucket("test6767ffg"));
        ali.listBucket();
//        ali.listBucketXml();
//        assertTrue(ali.putObject("test.png", content));
//        ali.getObject("test.png");
//        ali.deleteObject( "test.png");

    }

    static void upload_SigV2() {
        String accessKey = "6d3ae3ce9ce81caf42f093a31592e3da";
        String secretKey = "d388a9fa87fe69990601ffb498c486442657747fc0f00f5ec1f38ffb1df468f3";
        String method = "PUT";

        File file = new File(("D:\\code\\aj\\aj-business\\aj-base\\src\\test\\resources\\img.png"));
        byte[] content = FileHelper.openAsByte(file);
        String contentMD5 = MessageDigestHelper.calcFileMD5(file, null);

        String contentType = "application/octet-stream";
        String date = "Wed, 28 Oct 2021 15:00:00 GMT";
        String resource = "/your-bucket-name/your-object-key";

        String stringToSign = method + "\n" + contentMD5 + "\n" + contentType + "\n" + date + "\n" + resource;

        String url = "https://a4d2252636e737ac1ced6ec8f0c9c68e.r2.cloudflarestorage.com";

        String signature = MessageDigestHelper.getHmacSHA1AsBase64(secretKey, stringToSign);
        String authorizationHeader = "AWS " + accessKey + ":" + signature;

        ResponseEntity result = Post.put(url, content, conn -> {  // 执行 PUT 请求上传文件
            conn.setRequestProperty("Date", date); // 设置请求头 Date
            conn.setRequestProperty("Authorization", authorizationHeader); // 设置请求头 Authorization
        });

        System.out.println(result);
    }
}
