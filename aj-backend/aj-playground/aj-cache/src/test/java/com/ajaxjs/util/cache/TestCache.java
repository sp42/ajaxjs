package com.ajaxjs.util.cache;

import org.junit.Test;

public class TestCache {
    @Test
    public void test() throws InterruptedException {
        CacheMap<String, String> map = new CacheMap<>();
        map.put("1", "1", 3);
        map.put("2", "2", 2);
        map.put("3", "3");

        System.out.println("超时前1-》：" + map.get("1"));
        System.out.println("超时前2-》：" + map.get("2"));
        System.out.println("超时3-》：" + map.get("3"));
        System.err.println("超时前长度-》：" + map.size());
        Thread.sleep(4000);
        System.err.println("超时后长度-》：" + map.size());
        System.out.println("超时后1-》：" + map.get("1"));
        System.out.println("超时后2-》：" + map.get("2"));
        System.out.println("超时后3-》：" + map.get("3"));

        Thread.sleep(3000);

        System.out.println("整个map超时后3:-》：" + map.get("3"));

//        Thread.sleep(2000);
//        //当前获取到的全是没有超时的，但是在使用这个Entry时，有数据超时了，这部分超时数据，不负责处理
//        //entrySet相当于获取的是当前map集合的快照
//        Set<Entry<String, String>> entries = map.entrySet();
//
//        map.remove("1");
//
//        for (Entry<String, String> entry : entries) {
//            System.err.println(entry.getKey());
//        }

    }
}
