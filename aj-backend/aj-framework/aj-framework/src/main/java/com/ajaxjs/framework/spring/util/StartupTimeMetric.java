package com.ajaxjs.framework.spring.util;

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.PriorityOrdered;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * 用于调优的处理器
 */
public class StartupTimeMetric implements InstantiationAwareBeanPostProcessor, PriorityOrdered, ApplicationListener<ContextRefreshedEvent>, MergedBeanDefinitionPostProcessor {
    private static final LogHelper LOGGER = LogHelper.getLog(StartupTimeMetric.class);

    private final Map<String, Statistics> statisticsMap = new TreeMap<>();

    /**
     * InstantiationAwareBeanPostProcessor 中自定义的方法 在方法实例化之前执行 Bean 对象还没有
     */
    @Override
    public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
        String beanClassName = beanClass.getName();
        Statistics s = Statistics.builder().beanName(beanClassName).build();
        s.setBeforeInstantiationTime(System.currentTimeMillis());

        statisticsMap.put(beanClassName, s);

        return null;
    }

    /**
     * InstantiationAwareBeanPostProcessor 中自定义的方法 在方法实例化之后执行 Bean 对象已经创建出来了
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        String beanClassName = bean.getClass().getName();
        Statistics s = statisticsMap.get(beanClassName);

        if (s != null)
            s.setAfterInstantiationTime(System.currentTimeMillis());

        return true;

    }

    /**
     * BeanPostProcessor 接口中的方法 在 Bean 的自定义初始化方法之前执行 Bean 对象已经存在了
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        String beanClassName = bean.getClass().getName();
        Statistics s = statisticsMap.getOrDefault(beanClassName, Statistics.builder().beanName(beanClassName).build());
        s.setBeforeInitializationTime(System.currentTimeMillis());

        statisticsMap.putIfAbsent(beanClassName, s);

        return bean;
    }

    /**
     * BeanPostProcessor 接口中的方法 在 Bean 的自定义初始化方法执行完成之后执行 Bean 对象已经存在了
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String beanClassName = bean.getClass().getName();
        Statistics s = statisticsMap.get(beanClassName);

        if (s != null)
            s.setAfterInitializationTime(System.currentTimeMillis());

        return bean;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    private static final AtomicBoolean START_LOCK = new AtomicBoolean(false);

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOGGER.info("Spring 容器启动完成");

        if (START_LOCK.compareAndSet(false, true)) {
            List<Statistics> sList = statisticsMap.values().stream()
                    .sorted(Comparator.comparing(Statistics::calculateTotalCostTime).reversed())
                    .collect(Collectors.toList());

            StringBuilder sb = new StringBuilder();
            sList.forEach(_s -> sb.append(_s.toConsoleString()));

            LOGGER.info("ApplicationStartupTimeMetric:\n" + sb);
        }
    }

    @Override
    public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {
    }
}
