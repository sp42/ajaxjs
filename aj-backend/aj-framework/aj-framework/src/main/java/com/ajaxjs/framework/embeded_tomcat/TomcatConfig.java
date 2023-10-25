package com.ajaxjs.framework.embeded_tomcat;

import lombok.Data;

@Data
public class TomcatConfig {
    private String hostName = "localhost";

    private Integer port = 8080;

    private String contextPath = "/";

    private String docBase;

    private String tomcatBaseDir;

    private Integer shutdownPort = 8005;

    private Boolean enableSsi = false;

    private Boolean enableJsp = true;

    /**
     * Maximum amount of worker threads.
     */
    private int maxThreads = 0;

    /**
     * Minimum amount of worker threads. if not set, default value is 10
     */
    private int minSpareThreads = 0;

    /**
     * When Tomcat expects data from the client, this is the time Tomcat will wait for that data to arrive before closing the connection.
     */
    private int connectionTimeout = 0;

    /**
     * Maximum number of connections that the server will accept and process at any
     * given time. Once the limit has been reached, the operating system may still
     * accept connections based on the "acceptCount" property.
     */
    private int maxConnections = 0;

    /**
     * Maximum queue length for incoming connection requests when all possible request processing threads are in use.
     */
    private int acceptCount = 0;

    /**
     * Tomcat needs a directory for temp files. This should be the first method called.
     *
     * <p>
     * By default, if this method is not called, we use:
     * <ul>
     *  <li>system properties - catalina.base, catalina.home</li>
     *  <li>$PWD/tomcat.$PORT</li>
     * </ul>
     * (/tmp doesn't seem a good choice for security).
     *
     * <p>
     * TODO: disable work dir if not needed ( no jsp, etc ).
     */
    public void setTomcatBaseDir(String tomcatBaseDir) {
        this.tomcatBaseDir = tomcatBaseDir;
    }

    /**
     * When Tomcat expects data from the client, this is the time Tomcat will wait for that data to arrive before closing the connection.
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }
}
