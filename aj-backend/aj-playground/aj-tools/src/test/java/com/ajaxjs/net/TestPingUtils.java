package com.ajaxjs.net;

import org.junit.Test;

import static com.ajaxjs.net.PingUtils.*;

public class TestPingUtils {
    @Test
    public void test() {
        String ipAddress = "127.0.0.1";
        System.out.println(ping(ipAddress, 5, 3));
        System.out.println(pingForLinux(ipAddress, 5, 3));
//        String host = "192.168.1.192";
//        int port = 4001;
//        System.out.println(connect(host, port, 3000));

        String pingStr = "64 bytes from 127.0.0.1: icmp_seq=1 tttl=64 time=0.015 ms";
        System.out.println(getCheckResultForLinux(pingStr));
    }
}
