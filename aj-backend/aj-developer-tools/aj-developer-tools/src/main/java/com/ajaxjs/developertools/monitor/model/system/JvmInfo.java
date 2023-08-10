package com.ajaxjs.developertools.monitor.model.system;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

/**
 * JVM 信息
 */
@Data
public class JvmInfo implements IBaseModel {
    /**
     * jdk版本
     */
    private String jdkVersion;

    /**
     * jdk Home
     */
    private String jdkHome;

    /**
     * jak name
     */
    private String jdkName;

    /**
     * JVM 厂商
     */
    private String vendor;

    /**
     * 系统架构
     */
    private String arch;

    /**
     * 总内存
     */
    private String jvmTotalMemory;

    /**
     * Java虚拟机将尝试使用的最大内存量
     */
    private String maxMemory;

    /**
     * 空闲内存
     */
    private String freeMemory;

    /**
     * 已使用内存
     */
    private String usedMemory;

    /**
     * 内存使用率
     */
    private double usePercent;

    /**
     * 返回Java虚拟机的启动时间（毫秒）。此方法返回Java虚拟机启动的大致时间。
     */
    private long startTime;

    /**
     * 返回Java虚拟机的正常运行时间（毫秒）
     */
    private long uptime;
}
