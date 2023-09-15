package com.ajaxjs.data.sql_controller;

import com.ajaxjs.util.logger.LogHelper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.*;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 用于 Spring 动态代理注入自定义接口
 *
 * @author Frank Cheung sp42@qq.com
 */
public class ServiceBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware {
    private static final LogHelper LOGGER = LogHelper.getLog(ServiceBeanDefinitionRegistry.class);

    /**
     * 控制器所在的包
     */
    private final String[] controllerPackage;

    /**
     * 创建一个 ServiceBeanDefinitionRegistry
     *
     * @param controllerPackage 控制器所在的包
     */
    public ServiceBeanDefinitionRegistry(String... controllerPackage) {
        this.controllerPackage = controllerPackage;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        for (String p : controllerPackage)
            postProcessBeanDefinitionRegistry(registry, p);
    }

    private void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry, String controllerPackage) throws BeansException {
        LOGGER.info("扫描 SQL-Bind 控制器……" + controllerPackage);
        Set<Class<RestController>> scannerPackages = scannerPackages(controllerPackage);

        // 通过反射获取需要代理的接口的 clazz 列表
        for (Class<?> beanClazz : scannerPackages) {
            AbstractBeanDefinition def = BeanDefinitionBuilder.genericBeanDefinition(beanClazz).getRawBeanDefinition();

            /*
             * 这里可以给该对象的属性注入对应的实例。mybatis 就在这里注入了 dataSource 和 sqlSessionFactory，
             * definition.getPropertyValues().add("interfaceType", beanClazz)，BeanClass 需要提供
             * setter definition.getConstructorArgumentValues()，BeanClass
             * 需要提供包含该属性的构造方法，否则会注入失败
             */
            def.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);

            /*
             * 注意，这里的 BeanClass 是生成 Bean 实例的工厂，不是 Bean 本身。 FactoryBean 是一种特殊的
             * Bean，其返回的对象不是指定类的一个实例，其返回的是该工厂 Bean 的 getObject 方法所返回的对象。
             */
            def.setBeanClass(ControllerFactory.class);
            def.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);// 这里采用的是 byType 方式注入，类似的还有 byName 等

//			LOGGER.info("beanClazz.getSimpleName(): {0}, GenericBeanDefinition:{1}", beanClazz.getSimpleName(), def);

            registry.registerBeanDefinition(beanClazz.getName(), def);
        }
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
    }

    private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";

    private MetadataReaderFactory metadataReaderFactory;

    private ResourcePatternResolver resolver;

    @Override
    public void setResourceLoader(ResourceLoader loader) {
        resolver = ResourcePatternUtils.getResourcePatternResolver(loader);
        metadataReaderFactory = new CachingMetadataReaderFactory(loader);
    }

    /**
     * 根据包路径获取包及子包下的所有类
     *
     * @param basePackage basePackage
     * @return 类集合
     */
    private Set<Class<RestController>> scannerPackages(String basePackage) {
        Set<Class<RestController>> set = new LinkedHashSet<>();
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + ClassUtils.convertClassNameToResourcePath(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;

        try {
            Resource[] resources = resolver.getResources(packageSearchPath);

            for (Resource resource : resources) {
                if (resource.isReadable()) {
                    String clzName = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
                    Class<?> clazz = Class.forName(clzName);

                    if (clazz.isInterface() && clazz.getAnnotation(RestController.class) != null)
                        set.add((Class<RestController>) clazz);
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            LOGGER.warning(e);
        }

        return set;
    }

    /**
     * @param clz
     * @return
     */
    public static ServiceBeanDefinitionRegistry init(Class<?> clz) {
        return new ServiceBeanDefinitionRegistry(clz.getPackage().getName());
    }
}