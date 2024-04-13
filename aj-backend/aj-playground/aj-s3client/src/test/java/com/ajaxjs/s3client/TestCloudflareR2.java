package com.ajaxjs.s3client;

import com.ajaxjs.s3client.factory.CloudflareR2;
import com.ajaxjs.s3client.model.Config;
import com.ajaxjs.util.io.FileHelper;
import org.junit.Test;

import java.io.File;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TestCloudflareR2 {
    CloudflareR2 client = new CloudflareR2();

    {
        Map<String, Object> _cfg = TestBase.getConfigFromYml("application.yml");
        System.out.println(_cfg);

        Config cfg = new Config();
        cfg.setEndPoint((String) _cfg.get("S3Storage_R2_endpoint"));
        cfg.setAccessKey((String) _cfg.get("S3Storage_R2_accessKeyId"));
        cfg.setSecretKey((String) _cfg.get("S3Storage_R2_secretAccessKey"));
        cfg.setBucketName((String) _cfg.get("S3Storage_R2_bucket"));

        client.setConfig(cfg);
    }

    @Test
    public void testListBucket() {
        String s = client.listBucket();
        assertNotNull(s);
        Map<String, String> stringStringMap = client.listBucketXml();
        System.out.println(stringStringMap);
    }

    @Test
    public void testBucket() {
        assertTrue(client.createBucket("test"));
        assertTrue(client.deleteBucket("test"));
    }

    @Test
    public void testPutObject() {
        File file = new File(("D:\\code\\aj\\aj-business\\aj-base\\src\\test\\resources\\img.png"));
        byte[] content = FileHelper.openAsByte(file);

        assertTrue(client.putObject("ajaxjs", "s22.png", content));
    }
}
