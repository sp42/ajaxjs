package com.ajaxjs.util.cache;

import com.ajaxjs.util.cache.expiry.ExpiryCache;
import org.junit.Test;

public class TestCache {
    @Test
    public void testExpiry() throws InterruptedException {
        ExpiryCache<String, Object> instance = ExpiryCache.getInstance();
        instance.put("key1", "key1", 1000);
        instance.put("key2", "key2", 2000);
        instance.put("key3", "key3", 3000);

        System.out.println(instance.get("key1"));
        System.out.println(instance.get("key2"));
        System.out.println(instance.get("key3"));
        Thread.sleep(1000);
        System.out.println("-----------------");
        System.out.println(instance.get("key1"));
        System.out.println(instance.get("key2"));
        System.out.println(instance.get("key3"));
        Thread.sleep(1000);
        System.out.println("-----------------");
        System.out.println(instance.get("key1"));
        System.out.println(instance.get("key2"));
        System.out.println(instance.get("key3"));
        Thread.sleep(1000);
        System.out.println("-----------------");
        System.out.println(instance.get("key1"));
        System.out.println(instance.get("key2"));
        System.out.println(instance.get("key3"));
    }
}
