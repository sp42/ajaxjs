package com.ajaxjs.load_balance;

import lombok.Data;

import java.util.List;
import java.util.Random;

import javax.annotation.Resource;


/**
 * 抽象的负载均衡基类 提供基本的服务信息和相关服务实例列表的管理
 */
@Data
public abstract class AbstractLoadBalance implements LoadBalance {
    @Resource(name = "dynamicUploadRule")
    private transient DynamicUploadRule dynamicUploadRule;

    protected String serviceName;

    protected String version;

    // 特定版本的服务的标识
    private transient String serviceKey;

    protected List<ServiceInstance> serviceInstanceList;

    @Override
    public void setService(String serviceName, String version) {
        this.serviceName = serviceName;
        this.version = version;
        this.serviceKey = genKey(serviceName, version);
    }

    // 随机算法获取可利用的服务列表
    @Override
    public ServiceInstance chooseServerInstance() {
        List<ServiceInstance> allServiceList = getAllServiceInstanceList();
        if (null == allServiceList)
            return null;

        ServiceInstance serviceInstance = null;
        int indexOfLoop = 0;
        Random random = new Random();

        if (allServiceList.size() > 0) {
            int serviceCount = allServiceList.size();

            while (null == serviceInstance && indexOfLoop < serviceCount * 5) {// 由于是随机选取，不能在serverCount内选出
                int index = random.nextInt(serviceCount);
                serviceInstance = allServiceList.get(index);
//                logger.info("随机选择算法获取可用的服务:" + serviceInstance.getServerName());

                if (serviceInstance.isIsolated()) {
//                    logger.info("选择的服务暂时不可用:" + serviceInstance.getServerName()  + ",重新选择");
                    indexOfLoop++;
                    serviceInstance = null;
                }
            }
        }

        return serviceInstance;
    }

    @Override
    public void init() {
        // 拿到所有的服务器列表
        setServiceInstanceList(getAllServiceInstanceList());
    }

    @Override
    public List<ServiceInstance> getServiceInstanceList() {
        return serviceInstanceList;
    }

    @Override
    public void updateServerInstanceList() {
        // 这里实际上应该重新获取注册的服务信息更新,此处默认
        setServiceInstanceList(getAllServiceInstanceList());
    }

    @Override
    public void isolateServerInstance(String serverName) {
        for (ServiceInstance serverInstance : serviceInstanceList) {
            if (serverName.equals(serverInstance.getServerName())) {
                serverInstance.setIsolated(true);
                break;
            }
        }
    }

    @Override
    public void resumeServerInstance(String serverName) {
        for (final ServiceInstance serverInstance : serviceInstanceList) {
            if (serverName.equals(serverInstance.getServerName())) {
                serverInstance.setIsolated(false);
                break;
            }
        }
    }

    //通过服务名获取服务实例
    protected ServiceInstance getServiceInstanceByServiceName(String serviceName) {
        ServiceInstance serviceInstance = null;
        List<ServiceInstance> serviceInstances = getAllServiceInstanceList();

        if (null == serviceInstances)
            return null;

        for (final ServiceInstance instance : serviceInstances) {
            if (instance.getServerName().equals(serviceName)) {
                serviceInstance = instance;
                break;
            }
        }

        return serviceInstance;
    }

    private String genKey(String serviceName, String version) {
        return serviceName + '#' + version;
    }

    private List<ServiceInstance> getAllServiceInstanceList() {
        return dynamicUploadRule.getServiceInstanceRule(); // 模拟服务器注册后的服务实例
    }

    protected String getServiceNameByServiceKey(String serviceKey) {
        return serviceKey.substring(0, serviceKey.indexOf('#'));
    }

}