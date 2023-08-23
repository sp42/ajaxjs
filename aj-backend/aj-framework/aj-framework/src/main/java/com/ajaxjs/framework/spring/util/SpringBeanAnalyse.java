package com.ajaxjs.framework.spring.util;


import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class SpringBeanAnalyse implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent> {
    private static final LogHelper LOGGER = LogHelper.getLog(SpringBeanAnalyse.class);

    private final Map<String, Long> mapBeanTime = new HashMap<>();

    private static final AtomicBoolean started = new AtomicBoolean(false);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        mapBeanTime.put(beanName, System.currentTimeMillis());

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Long begin = mapBeanTime.get(beanName);

        if (begin != null)
            mapBeanTime.put(beanName, System.currentTimeMillis() - begin);

        return bean;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (started.compareAndSet(false, true)) {
            for (Map.Entry<String, Long> entry : mapBeanTime.entrySet()) {
//                if (entry.getValue() > 1000)
//                LOGGER.info("slowSpringBean => {0}:{1}", entry.getKey(), (entry.getValue()));
            }
        }
    }
}
