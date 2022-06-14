package com.ajaxjs.easy_controller;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;

import com.ajaxjs.util.logger.LogHelper;

/**
 * 用于 Spring 动态代理注入自定义接口
 * 
 * @author Frank Cheung<sp42@qq.com>
 *
 */
public class ServiceBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor, ResourceLoaderAware, ApplicationContextAware {
	private static final LogHelper LOGGER = LogHelper.getLog(ServiceBeanDefinitionRegistry.class);

	/**
	 * 控制器所在的包
	 */
	private String controllerPackage;

	/**
	 * 
	 * @param controllerPackage 控制器所在的包
	 */
	public ServiceBeanDefinitionRegistry(String controllerPackage) {
		this.controllerPackage = controllerPackage;
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
		LOGGER.info("扫描控制器……");
		Set<Class<?>> scannerPackages = scannerPackages(controllerPackage);

		// 通过反射获取需要代理的接口的 clazz 列表
		for (Class<?> beanClazz : scannerPackages) {
			BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(beanClazz);
			GenericBeanDefinition definition = (GenericBeanDefinition) builder.getRawBeanDefinition();
			// 这里可以给该对象的属性注入对应的实例。mybatis就在这里注入了dataSource和sqlSessionFactory，
			// definition.getPropertyValues().add("interfaceType",
			// beanClazz)，BeanClass需要提供setter
			// definition.getConstructorArgumentValues()，BeanClass需要提供包含该属性的构造方法，否则会注入失败
			definition.getConstructorArgumentValues().addGenericArgumentValue(beanClazz);
			definition.getConstructorArgumentValues().addGenericArgumentValue(applicationContext);

			// 注意，这里的BeanClass是生成Bean实例的工厂，不是Bean本身。
			// FactoryBean是一种特殊的Bean，其返回的对象不是指定类的一个实例，
			// 其返回的是该工厂Bean的getObject方法所返回的对象。
			definition.setBeanClass(ServiceFactory.class);

			// 这里采用的是 byType 方式注入，类似的还有 byName 等
			definition.setAutowireMode(GenericBeanDefinition.AUTOWIRE_BY_TYPE);

//			String simpleName = beanClazz.getSimpleName();
//			LOGGER.info("beanClazz.getSimpleName(): {0}", simpleName);
//			LOGGER.info("GenericBeanDefinition: {0}", definition);

			registry.registerBeanDefinition(beanClazz.getSimpleName(), definition);
		}
	}

	private static final String DEFAULT_RESOURCE_PATTERN = "**/*.class";
	private MetadataReaderFactory metadataReaderFactory;

	/**
	 * 根据包路径获取包及子包下的所有类
	 *
	 * @param basePackage basePackage
	 * @return Set<Class < ?>> Set<Class<?>>
	 */
	private Set<Class<?>> scannerPackages(String basePackage) {
		Set<Class<?>> set = new LinkedHashSet<>();
		String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + resolveBasePackage(basePackage) + '/' + DEFAULT_RESOURCE_PATTERN;

		try {
			Resource[] resources = resourcePatternResolver.getResources(packageSearchPath);

			for (Resource resource : resources) {
				if (resource.isReadable()) {
					String clzName = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();

					try {
						Class<?> clazz = Class.forName(clzName);
						if (clazz.isInterface() && clazz.getAnnotation(InterfaceBasedController.class) != null)
							set.add(clazz);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return set;
	}

	protected String resolveBasePackage(String basePackage) {
		return ClassUtils.convertClassNameToResourcePath(getEnvironment().resolveRequiredPlaceholders(basePackage));
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
	}

	private ResourcePatternResolver resourcePatternResolver;

	private ApplicationContext applicationContext;

	@Override
	public void setResourceLoader(ResourceLoader loader) {
		resourcePatternResolver = ResourcePatternUtils.getResourcePatternResolver(loader);
		metadataReaderFactory = new CachingMetadataReaderFactory(loader);
	}

	@Override
	public void setApplicationContext(ApplicationContext cxt) throws BeansException {
		this.applicationContext = cxt;
	}

	private Environment getEnvironment() {
		return applicationContext.getEnvironment();
	}

}