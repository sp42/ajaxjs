package com.ajaxjs.developertools.monitor.tomcat_jmx.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

@Data
public class SystemInfo implements IBaseModel {
    /**
     * 待终结对象计数
     */
    private int ObjectPendingFinalizationCount;

    /**
     * 是否启用详细输出
     */
    private boolean Verbose;

    /**
     * 名称
     */
    private String Name;

    /**
     * 类路径
     */
    private String ClassPath;

    /**
     * 引导类路径
     */
    private String BootClassPath;

    /**
     * 库路径
     */
    private String LibraryPath;

    /**
     * 连续运行时间
     */
    private String Uptime;

    /**
     * 虚拟机名称
     */
    private String VmName;

    /**
     * 虚拟机供应商
     */
    private String VmVendor;

    /**
     * 虚拟机版本
     */
    private String VmVersion;

    /**
     * 是否支持引导类路径
     */
    private boolean BootClassPathSupported;

    /**
     * 输入参数
     */
    private String[] InputArguments;

    /**
     * 管理规范版本
     */
    private String ManagementSpecVersion;

    /**
     * 规范名称
     */
    private String SpecName;

    /**
     * 规范供应商
     */
    private String SpecVendor;

    /**
     * 规范版本
     */
    private String SpecVersion;

    /**
     * 启动时间
     */
    private String StartTime;
}
