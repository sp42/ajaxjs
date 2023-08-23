package com.ajaxjs.sql;

import com.ajaxjs.sql.util.IdWorker;
import org.junit.Test;

import com.ajaxjs.sql.util.SnowflakeId;

public class TestSnowflakeId {
    @Test
    public void test1() {
        System.out.println(SnowflakeId.get());
    }

    @Test
    public void test2() {
        IdWorker worker = new IdWorker(1, 1, 1);

        for (int i = 0; i < 30; i++)
            System.out.println(worker.nextId());
    }
}
