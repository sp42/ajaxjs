package com.ajaxjs.framework.embeded_tomcat;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

public class TomcatManage {
    public static void shutdown() {
        shutdown(null, null);
    }

    public static void shutdown(String serverHost, Integer serverPort) {
        if (serverHost == null)
            serverHost = "localhost";

        if (serverPort == null)
            serverPort = 8005;

        SocketClientImpl sc = new SocketClientImpl();

        sc.setDistHost(serverHost);
        System.out.println("localIP: " + Objects.requireNonNull(getSpecialHostAddress(sc.getDistHost())).getHostAddress());
        sc.setDistPort(serverPort);
        sc.send("SHUTDOWN");
    }

    private static InetAddress getSpecialHostAddress(String hostName) {
        try {
            return InetAddress.getByName(hostName);
        } catch (UnknownHostException e) {
            return null;
        }
    }
}
