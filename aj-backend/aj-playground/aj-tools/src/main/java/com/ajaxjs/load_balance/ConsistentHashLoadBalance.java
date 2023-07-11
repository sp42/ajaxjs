package com.ajaxjs.load_balance;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

@Service("consistentHashLoadBalance")
//基于一致性hash算法实现负载均衡
public class ConsistentHashLoadBalance extends AbstractLoadBalance {
    // 虚拟节点总数,用来计算各个服务器的虚拟节点总数
    public static final int VITRUAL_NODE_NUMBER = 1000;

    // 默认的每个服务器的虚拟节点个数
    public static final int DEFAULT_NODE_NUMBER = 30;

    //原子更新引用
    private AtomicReference<ConsistentHash<ServiceInstance>> hashRing = new AtomicReference<>();

    // 虚拟节点个数,初始化时根据服务节点数计算,不随服务节点变化而变化
    // 每个服务实例在哈希环上的复制次数
    private int numberOfReplicas;

    public ServiceInstance chooseServerInstance(String serviceKey) {
        this.serviceName = getServiceNameByServiceKey(serviceKey);
        //从哈希环中找到对应的节点以及后续的节点
        List<ServiceInstance> instances = hashRing.get().getNUniqueBinsFor(serviceName, getServiceInstanceList().size());
        ServiceInstance ServiceInstance = null;

        //循环每个阶段，直至找出没有被隔离的服务器
        for (ServiceInstance instance : instances) {
            if (instance.isIsolated()) {
//                logger.info("服务被隔离了,暂时不可用:" + serviceName);
            } else {
                //顺时针找到第一个后就返回
                ServiceInstance = instance;
                break;
            }
        }

        return ServiceInstance;
    }

    @Override
    public void init() {
        super.init();
        numberOfReplicas = getServiceInstanceList().isEmpty() ? DEFAULT_NODE_NUMBER : VITRUAL_NODE_NUMBER / getServiceInstanceList().size();
//        logger.info("开始构建hash环");
        hashRing.set(new ConsistentHash<ServiceInstance>(getServiceInstanceList().get(0), numberOfReplicas));
    }

}