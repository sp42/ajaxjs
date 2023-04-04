package com.ajaxjs.framework.spring;

import java.util.Locale;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 提供一种方法快速获取 Spring 注入的 bean，均为静态方法
 * 高级版本：https://blog.csdn.net/qq_20597727/article/details/80996190
 */
public class IocContext implements ApplicationContextAware {
    private static final LogHelper LOGGER = LogHelper.getLog(IocContext.class);

    /**
     * Spring 上下文
     */
    public static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
    	IocContext.context = context;
    }

    /**
     * 获取上下文
     *
     * @return 上下文对象
     */
    public static ApplicationContext getContext() {
        return context;
    }

    /**
     * 获取注册者 context->bean factory->registry
     *
     * @return
     */
    public static BeanDefinitionRegistry getRegistry() {
        ConfigurableApplicationContext c = (ConfigurableApplicationContext) context;
        return (DefaultListableBeanFactory) c.getBeanFactory();
    }

    /**
     * 手动注入 Bean
     *
     * @param clz    Bean 类引用
     * @param beanId Bean 标识
     */
    public static void registryBean(Class<?> clz, String beanId) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clz);
        getRegistry().registerBeanDefinition(beanId, builder.getBeanDefinition());
    }

    /**
     * 手动注入 Bean，随机标识
     *
     * @param clz Bean 类引用
     */
    public static void registryBean(Class<?> clz) {
        registryBean(clz, StrUtil.getRandomString(6));
    }

    /**
     * 获取已注入的 bean 对象
     *
     * @param <T> 对象类型
     * @param clz 对象类型引用
     * @return bean 对象
     */
    public static <T> T getBean(Class<T> clz) {
        if (context == null) {
            LOGGER.warning("Spring Bean 未准备好");
            return null;
        }

        try {
            return context.getBean(clz);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * 获取已注入的 bean 对象
     *
     * @param beanName bean 对象之 id
     * @return bean 对象
     */
    public static Object getBean(String beanName) {
        try {
            return context.getBean(beanName);
        } catch (NoSuchBeanDefinitionException e) {
            return null;
        }
    }

    /**
     * @param key
     * @return
     */
    public static String getMessage(String key) {
        return context.getMessage(key, null, Locale.getDefault());
    }

    /**
     * 根据接口获取已注入的对象
     *
     * @param <T> 接口类型
     * @param clz Bean 的接口引用
     * @return key 为 bean 的 id，value 为 bean 实例
     */
    public static <T> Map<String, T> findByInterface(Class<T> clz) {
        return context.getBeansOfType(clz);
    }
}
