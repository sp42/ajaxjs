package com.ajaxjs.data;


import org.junit.Test;

import com.ajaxjs.data.util.SnowflakeId;

public class TestSnowflakeId {
//    @Test
//    public void test1() {
//        for (int i = 0; i < 30; i++)
//            System.out.println(IdWorker.get());
//    }
//
//    @Test
//    public void test2() {
//        IdWorker worker = new IdWorker(1, 1, 1);
//
//        for (int i = 0; i < 30; i++)
//            System.out.println(worker.nextId());
//    }

    @Test
    public void test3() {
        for (int i = 0; i < 30; i++)
            System.out.println(SnowflakeId.get());
    }
}
