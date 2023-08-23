package com.ajaxjs.framework.spring.util;

import java.beans.PropertyDescriptor;
import java.util.*;
import java.util.stream.Collectors;

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;

/**
 * 用于调优的处理器
 */
@Component
public class ApplicationStartupTimeMetricPostProcessor implements InstantiationAwareBeanPostProcessor, PriorityOrdered {
    private static final LogHelper LOGGER = LogHelper.getLog(ApplicationStartupTimeMetricPostProcessor.class);

    private final Map<String, BeanPostProcessStatistics> beanName2BeanPostProcessStatisticsMap = new TreeMap<>();

    private final List<String> beforeInitializationOrderList = new ArrayList<>();

    private final List<String> afterInitializationOrderList = new ArrayList<>();

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法 在方法实例化之前执行 Bean对象还没有
     */
    @Override

    public Object postProcessBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
        String beanClassName = beanClass.getName();
        BeanPostProcessStatistics beanPostProcessStatistics = BeanPostProcessStatistics.builder().beanName(beanClassName).build();
        beanPostProcessStatistics.setBeforeInstantiationTime(System.currentTimeMillis());
        beanName2BeanPostProcessStatisticsMap.put(beanClassName, beanPostProcessStatistics);

        return null;

    }

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法 在方法实例化之后执行 Bean对象已经创建出来了
     */
    @Override

    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        String beanClassName = bean.getClass().getName();
        BeanPostProcessStatistics beanPostProcessStatistics = beanName2BeanPostProcessStatisticsMap.get(beanClassName);

        if (beanPostProcessStatistics != null)
            beanPostProcessStatistics.setAfterInstantiationTime(System.currentTimeMillis());

        return true;

    }

    /**
     * InstantiationAwareBeanPostProcessor中自定义的方法 可以用来修改Bean中属性的内容
     */
    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        return pvs;
    }

    /**
     * BeanPostProcessor接口中的方法 在Bean的自定义初始化方法之前执行 Bean对象已经存在了
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        String beanClassName = bean.getClass().getName();
        BeanPostProcessStatistics beanPostProcessStatistics = beanName2BeanPostProcessStatisticsMap.getOrDefault(beanClassName, BeanPostProcessStatistics.builder().beanName(beanClassName).build());
        beanPostProcessStatistics.setBeforeInitializationTime(System.currentTimeMillis());
        beanName2BeanPostProcessStatisticsMap.putIfAbsent(beanClassName, beanPostProcessStatistics);
        beforeInitializationOrderList.add(beanClassName);

        return bean;
    }

    /**
     * BeanPostProcessor接口中的方法 在Bean的自定义初始化方法执行完成之后执行 Bean对象已经存在了
     */

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        String beanClassName = bean.getClass().getName();
        BeanPostProcessStatistics beanPostProcessStatistics = beanName2BeanPostProcessStatisticsMap.get(beanClassName);

        if (beanPostProcessStatistics != null)
            beanPostProcessStatistics.setAfterInitializationTime(System.currentTimeMillis());

        afterInitializationOrderList.add(beanClassName);

        List<BeanPostProcessStatistics> beanPostProcessStatisticsList = beanName2BeanPostProcessStatisticsMap.values().stream()
                .sorted(Comparator.comparing(BeanPostProcessStatistics::calculateTotalCostTime).reversed())
                .collect(Collectors.toList());

        StringBuilder statisticsLog = new StringBuilder();
        beanPostProcessStatisticsList.forEach(_beanPostProcessStatistics -> statisticsLog.append(_beanPostProcessStatistics.toConsoleString()));

        LOGGER.info("ApplicationStartupTimeMetric:\n{0}", statisticsLog.toString());
//        LOGGER.info("beanName2BeanPostProcessStatisticsMap:\n{0}", beanName2BeanPostProcessStatisticsMap);
//        LOGGER.info("beforeInitializationOrderList:\n{0}", beforeInitializationOrderList);
//        LOGGER.info("afterInitializationOrderList:\n{0}", afterInitializationOrderList);

        return bean;
    }

    @Override
    public int getOrder() {
        return PriorityOrdered.HIGHEST_PRECEDENCE;
    }

    public void run() {
        List<BeanPostProcessStatistics> beanPostProcessStatisticsList = beanName2BeanPostProcessStatisticsMap.values().stream()
                .sorted(Comparator.comparing(BeanPostProcessStatistics::calculateTotalCostTime).reversed())
                .collect(Collectors.toList());

        StringBuilder statisticsLog = new StringBuilder();
        beanPostProcessStatisticsList.forEach(beanPostProcessStatistics -> statisticsLog.append(beanPostProcessStatistics.toConsoleString()));

        LOGGER.info("ApplicationStartupTimeMetric:\n{}", statisticsLog.toString());
        LOGGER.info("beanName2BeanPostProcessStatisticsMap:\n{}", beanName2BeanPostProcessStatisticsMap);
        LOGGER.info("beforeInitializationOrderList:\n{}", beforeInitializationOrderList);
        LOGGER.info("afterInitializationOrderList:\n{}", afterInitializationOrderList);
    }


}
