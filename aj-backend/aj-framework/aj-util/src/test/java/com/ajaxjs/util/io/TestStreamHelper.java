package com.ajaxjs.util.io;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestStreamHelper {
    @Test
    public void testParseHexStr2Byte() {
        byte[] bs = StreamHelper.parseHexStr2Byte("1A2B3C");
        assert bs != null;
        assertEquals(0x1A, bs[0]);
    }
}
