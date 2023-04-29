package com.ajaxjs.web;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Map;


import com.ajaxjs.net.http.Tools;
import org.junit.Test;

public class TestServletHelper {
    @Test
    public void testGetIpLocation() throws IOException {
        Map<String, Object> ip = Tools.getIpLocation("35.220.250.107");
        assertNotNull(ip);

        assertTrue(!ServletHelper.isChinaMainlandIp("35.220.250.107"));
    }
}
