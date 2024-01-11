package com.ajaxjs.net;

import com.ajaxjs.net.http.Post;
import com.ajaxjs.util.io.FileHelper;
import org.junit.Test;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class TestPost {
    @Test
    public void testPost() {
        String result = Post.post("http://localhost:8080/post", new HashMap<String, Object>() {
            private static final long serialVersionUID = 1L;

            {
                put("foo", "bar");
            }
        }).toString();

        result = Post.post("http://localhost:8080/post", "a=1&b=2&c=3").toString();
        assertNotNull(result);
    }

    @Test
    public void testPostFile() {
        String url = "http://192.168.1.3:8089/carRental/common_service/upload";
        byte[] b = FileHelper.openAsByte(new File("C:\\temp\\74d8e0a8e8827241fcc6531612eaf6d5.svg"));

        Map<String, Object> map = Post.postFile(url, "file", "any.txt", b, null);

        System.out.println(map.get("data").toString());
    }
}
