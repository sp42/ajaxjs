package com.ajaxjs.s3client;

import com.ajaxjs.net.http.Post;
import com.ajaxjs.net.http.ResponseEntity;
import com.ajaxjs.s3client.factory.AliyunOSS;
import com.ajaxjs.s3client.factory.NeteaseOSS;
import com.ajaxjs.s3client.model.Config;
import com.ajaxjs.util.MessageDigestHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.junit.Assert.assertTrue;

public class TestFactory {
    Config nsoCfg = new Config();

    NeteaseOSS nso = new NeteaseOSS();

    Config aliCfg = new Config();
    AliyunOSS ali = new AliyunOSS();

    {
//        String someValue = (String) config.get("key1");
//        int anotherValue = (int) config.get("key2");
        Map<String, Object> cfg = getConfigFromYml("config.yml");
        System.out.println(cfg);
        nsoCfg.setEndPoint("nos-eastchina1.126.net");
        nsoCfg.setAccessKey("4c2396f706744a74ba6b8319e1099b60");
        nsoCfg.setSecretKey("67f070468939410491c54d6979614980");
        nsoCfg.setBucketName("leidong");
        nsoCfg.setRemark("NOS");
        nso.setConfig(nsoCfg);

        aliCfg.setEndPoint("oss-cn-beijing.aliyuncs.com");
        aliCfg.setAccessKey("LTAI4FtWXD5P4cCAxPcb7apP");
        aliCfg.setSecretKey("i6HPqz4AlTZUPkVQSj7Pr74yQsK69Q");
        aliCfg.setBucketName("leyou-cxk");
        aliCfg.setRemark("OSS");

        ali.setConfig(aliCfg);
    }

    static Map<String, Object> getConfigFromYml(String configFile) {
        Yaml yaml = new Yaml();

        try (InputStream resourceAsStream = Resources.getResource(configFile)) {
            Map<String, Object> m = yaml.load(resourceAsStream);
            return m;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
