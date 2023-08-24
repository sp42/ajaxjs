package com.ajaxjs.developertools.monitor.tomcat_jmx.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

@Data
public class ThreadPool implements IBaseModel {
    /**
     * 当前正在执行任务的线程数
     */
    private int currentThreadsBusy;

    /**
     * SSL 实现的名称
     */
    private String sslImplementationName;

    /**
     * 是否暂停
     */
    private boolean paused;

    /**
     * Selector 的超时时间
     */
    private long selectorTimeout;

    /**
     * 模型类型
     */
    private String modelerType;

    /**
     * 当前连接数
     */
    private long connectionCount;

    /**
     * 监听器接受的连接数
     */
    private int acceptCount;

    /**
     * 线程优先级
     */
    private int threadPriority;

    /**
     * Executor 终止的超时时间
     */
    private long executorTerminationTimeoutMillis;

    /**
     * 是否正在运行
     */
    private boolean running;

    /**
     * 当前线程数
     */
    private int currentThreadCount;

    /**
     * 是否启用 SSL
     */
    private boolean sSLEnabled;

    /**
     * SNI 解析限制
     */
    private int sniParseLimit;

    /**
     * 最大线程数
     */
    private int maxThreads;

//    /**
//     * SSL 实现对象
//     */
//    private org.apache.tomcat.util.net.SSLImplementation sslImplementation;

    /**
     * 连接超时时间
     */
    private int connectionTimeout;

    /**
     * 是否启用 TCP 不延迟
     */
    private boolean tcpNoDelay;

    /**
     * 最大连接数
     */
    private int maxConnections;

    /**
     * 连接延迟时间
     */
    private int connectionLinger;

    /**
     * Keep-Alive 计数
     */
    private int keepAliveCount;

    /**
     * Keep-Alive 超时时间
     */
    private int keepAliveTimeout;

    /**
     * 最大 Keep-Alive 请求次数
     */
    private int maxKeepAliveRequests;

    /**
     * 本地端口
     */
    private int localPort;

    /**
     * 是否延迟接受连接
     */
    private boolean deferAccept;

    /**
     * 是否使用 sendfile
     */
    private boolean useSendfile;

    /**
     * 接收线程数
     */
    private int acceptorThreadCount;

    /**
     * 轮询线程数
     */
    private int pollerThreadCount;

    /**
     * 是否是守护进程
     */
    private boolean daemon;

    /**
     * 最小空闲线程数
     */
    private int minSpareThreads;

    /**
     * 是否使用继承通道
     */
    private boolean useInheritedChannel;

    /**
     * 是否支持 ALPN
     */
    private boolean alpnSupported;

    /**
     * 接收线程优先级
     */
    private int acceptorThreadPriority;

    /**
     * 是否在初始化时绑定
     */
    private boolean bindOnInit;

    /**
     * 轮询线程优先级
     */
    private int pollerThreadPriority;

    /**
     * 监听端口
     */
    private int port;

    /**
     * 域名
     */
    private String domain;

    /**
     * 名称
     */
    private String name;

    /**
     * 默认 SSL 主机配置名称
     */
    private String defaultSSLHostConfigName;
}
