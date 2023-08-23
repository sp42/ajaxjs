package com.ajaxjs.framework.spring.util;

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class LoggerBeanLoadCostPostProcessor implements BeanPostProcessor {
    private static final LogHelper LOGGER = LogHelper.getLog(SpringBeanAnalyse.class);

    private static Map<String, Long> cost = new HashMap<>(10000);

    private static AtomicInteger beanCount = new AtomicInteger(0);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        LOGGER.info("first load_spring_bean_cost_info,bean init beanName:{0}, begintime : {1}", beanName, System.currentTimeMillis());
        cost.put(beanName, System.currentTimeMillis());

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        beanCount.incrementAndGet();

        if (cost.get(beanName) == null) {
            LOGGER.info("first load_spring_bean_cost_info, beanCount : {0}, cost.get(beanName : {1} ) is null", beanCount.get(), beanName);
        } else {
            LOGGER.info("first load_spring_bean_cost_info,bean after beanCount : {0}, beanName:{1}, beanType :{2} before: {3}, cost : {4}",
                    beanCount.get(), beanName, bean.getClass().getName(), cost.get(beanName), (System.currentTimeMillis() - cost.get(beanName)));
        }

        return bean;
    }
}
