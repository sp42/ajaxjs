package com.ajaxjs.developertools.monitor.tomcat_jmx.model;

import com.ajaxjs.framework.IBaseModel;
import lombok.Data;

@Data
public class Session implements IBaseModel {
    /**
     * 会话属性值类名过滤器
     */
    private String sessionAttributeValueClassNameFilter;

    /**
     * 模型类型
     */
    private String modelerType;

    /**
     * 会话属性过滤器失败时是否发出警告
     */
    private boolean warnOnSessionAttributeFilterFailure;

    /**
     * 类名
     */
    private String className;

    /**
     * 安全随机算法
     */
    private String secureRandomAlgorithm;

    /**
     * 安全随机类
     */
    private String secureRandomClass;

    /**
     * 会话平均存活时间
     */
    private int sessionAverageAliveTime;

    /**
     * 被拒绝的会话数
     */
    private int rejectedSessions;

    /**
     * 处理过期会话的频率
     */
    private int processExpiresFrequency;

    /**
     * 状态名称
     */
    private String stateName;

    /**
     * 是否持久化身份验证
     */
    private boolean persistAuthentication;

    /**
     * 重复会话数
     */
    private int duplicates;

    /**
     * 最大活动会话数
     */
    private int maxActiveSessions;

    /**
     * 会话最大存活时间
     */
    private int sessionMaxAliveTime;

    /**
     * 处理时间
     */
    private long processingTime;

    /**
     * 路径名
     */
    private String pathname;

    /**
     * 会话过期速率
     */
    private int sessionExpireRate;

    /**
     * 会话属性名称过滤器
     */
    private String sessionAttributeNameFilter;

    /**
     * 活动会话数
     */
    private int activeSessions;

    /**
     * 会话创建速率
     */
    private int sessionCreateRate;

    /**
     * 名称
     */
    private String name;

    /**
     * 安全随机提供者
     */
    private String secureRandomProvider;

    /**
     * JVM 路由
     */
    private String jvmRoute;

    /**
     * 已过期的会话数
     */
    private long expiredSessions;

    /**
     * 最大活动数
     */
    private int maxActive;

    /**
     * 会话计数器
     */
    private long sessionCounter;
}
