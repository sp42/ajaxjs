package com.ajaxjs.load_balance;

import java.util.List;

/**
 * 负载均衡接口
 */
public interface LoadBalance {
    //根据请求选择一个服务实例
    ServiceInstance chooseServerInstance();

    /**
     * 设置服务的信息
     *
     * @param serviceName 服务名
     * @param version     服务版本
     */
    void setService(String serviceName, String version);

    /**
     * 初始化
     */
    void init();

    /**
     * 获取全部服务列表
     *
     * @return
     */
    List<ServiceInstance> getServiceInstanceList();

    /**
     * 更新服务实例列表
     */
    void updateServerInstanceList();

    /**
     * 隔离一个服务实例
     *
     * @param server
     */
    void isolateServerInstance(String server);

    /**
     * 恢复一个服务实例
     *
     * @param server
     */
    void resumeServerInstance(String server);
}