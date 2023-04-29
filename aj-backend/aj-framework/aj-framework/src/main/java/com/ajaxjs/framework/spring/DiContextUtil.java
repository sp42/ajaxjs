package com.ajaxjs.framework.spring;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ajaxjs.util.StrUtil;
import com.ajaxjs.framework.TestHelper;

/**
 * 方便静态方法、JSP 调用注入的组件*
 * 高级版本：<a href="https://blog.csdn.net/qq_20597727/article/details/80996190">...</a>
 *
 * @author Frank Cheung
 *
 */
public class DiContextUtil implements ApplicationContextAware {
	/**
	 * Spring 上下文
	 */
	public static ApplicationContext context;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		DiContextUtil.context = context;
	}

	/**
	 * 获取上下文
	 * 
	 * @return 上下文对象
	 */
	public static ApplicationContext getApplicationContext() {
		return context;
	}

	/**
	 * 获取已注入的对象
	 * 
	 * @param <T> 对象类型
	 * @param clz 对象类型引用
	 * @return 组件对象
	 */
	public static <T> T getBean(Class<T> clz) {
		if (context == null) {
			System.out.println("Spring Bean 未准备好");
			return null;
		}

		try {
			return context.getBean(clz);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}

	/**
	 * 获取已注入的对象
	 * 
	 * @param beanName 组件对象 id
	 * @return 组件对象
	 */
	public static Object getBean(String beanName) {
		try {
			return context.getBean(beanName);
		} catch (NoSuchBeanDefinitionException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return context.getMessage(key, null, Locale.getDefault());
	}

	/**
	 * 
	 * @param <T>
	 * @param clz
	 * @return key 为 bean 的 id，value 为 bean 实例
	 */
	public static <T> Map<String, T> findByInterface(Class<T> clz) {
		return context.getBeansOfType(clz);
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
	 * @param <T>
	 * @param clz
	 * @param beanId
	 */
	public static <T> void registryBean(Class<T> clz, String beanId) {
		BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(clz);
		getRegistry().registerBeanDefinition(beanId, builder.getBeanDefinition());
	}

	public static <T> void registryBean(Class<T> clz) {
		registryBean(clz, StrUtil.getRandomString(6));
	}

	/**
	 * 手动注入 Bean 并立即返回
	 * 
	 * @param <T>
	 * @param clz
	 * @param beanId
	 * @return
	 */
	public static <T> T registryBeanInstance(Class<T> clz, String beanId) {
		registryBean(clz, beanId);
		return getBean(clz);
	}

	public static <T> T registryBeanInstance(Class<T> clz) {
		registryBean(clz);
		return getBean(clz);
	}

	public static void initTest(String xml) {
		context = new FileSystemXmlApplicationContext(xml);
	}

	public static void closeTest() {
		((ConfigurableApplicationContext) context).close();
	}

	public static HttpServletRequest getRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

		if (requestAttributes == null) {
			if (TestHelper.isRunningTest())
				return TestHelper.request;
			else
				return null;
		}

		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	public static HttpSession getSession() {
		return Objects.requireNonNull(getRequest()).getSession();
	}

	/**
	 * Environment.resolveRequiredPlaceholders 方法是 Spring Framework 中的一个工具方法，用于解析配置文件中的占位符（placeholder）。
	 * 该方法会根据配置文件中的 ${...} 占位符，替换成对应的属性值，如果无法解析，则抛出 IllegalArgumentException 异常
	 * 
	 * @return
	 */
	public static String resolveRequiredPlaceholders(String str) {
		return context.getEnvironment().resolveRequiredPlaceholders(str);
	}
}