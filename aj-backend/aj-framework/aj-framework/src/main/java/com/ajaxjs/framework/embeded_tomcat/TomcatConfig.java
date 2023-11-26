package com.ajaxjs.framework.embeded_tomcat;

import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * Tomcat 配置参数
 */
@Data
public class TomcatConfig {
    /**
     * 主机名称
     */
    private String hostName = "localhost";

    /**
     * 访问的端口
     */
    private Integer port = 8082;

    /**
     * Web 上下文目录
     */
    private String contextPath;

    /**
     * Web 目录的磁盘路径，如 D:/1sync/static
     */
    private String docBase;

    /**
     * Tomcat 临时文件的目录，work 目录
     */
    private String baseDir;

    /**
     * 关闭的端口
     */
    private Integer shutdownPort = 8005;

    /**
     * 是否激活 SSI（服务器端嵌入）
     */
    private Boolean enableSsi = false;

    /**
     * 是否激活 JSP
     */
    private Boolean enableJsp = false;

    /**
     * 是否激活本地文件上传
     */
    private Boolean enableLocalFileUpload = true;

    /**
     * 本地文件上传的目录
     */
    private String localFileUploadDir;

    /**
     * 是否激活 JMX 监控
     */
    private boolean enableJMX = false;

    /**
     * 自定义连接器
     */
    private boolean customerConnector = false;

    /**
     * 最大工作线程数 Maximum amount of worker threads.
     */
    private int maxThreads = 0;

    /**
     * 最小工作线程数，默认是 10。Minimum amount of worker threads. if not set, default value is 10
     */
    private int minSpareThreads = 0;

    /**
     * 当客户端从 Tomcat 获取数据时候，距离关闭连接的等待时间
     * When Tomcat expects data from the client, this is the time Tomcat will wait for that data to arrive before closing the connection.
     */
    private int connectionTimeout = 0;

    /**
     * 最大连接数
     * Maximum number of connections that the server will accept and process at any
     * given time. Once the limit has been reached, the operating system may still
     * accept connections based on the "acceptCount" property.
     */
    private int maxConnections = 0;

    /**
     * 当请求超过可用的线程试试，最大的请求排队数
     * Maximum queue length for incoming connection requests when all possible request processing threads are in use.
     */
    private int acceptCount = 0;

    /**
     * Tomcat 临时文件的目录。如果不需要（如不需要 jsp）禁止 work dir。
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
    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public String getContextPath() {
        return StringUtils.hasText(contextPath) ? contextPath : "";
    }
}
