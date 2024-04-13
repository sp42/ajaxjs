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
        Config cfg = new Config();
        cfg.setEndPoint("https://a4d2252636e737ac1ced6ec8f0c9c68e.r2.cloudflarestorage.com");
        cfg.setAccessKey("6d3ae3ce9ce81caf42f093a31592e3da");
        cfg.setSecretKey("d388a9fa87fe69990601ffb498c486442657747fc0f00f5ec1f38ffb1df468f3");
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
