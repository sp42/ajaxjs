package com.ajaxjs.spring;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import com.ajaxjs.util.WebHelper;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 通用的配置，可供 classic/little 使用
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public abstract class BaseSpringWebInitializer implements WebApplicationInitializer {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseSpringWebInitializer.class);

	/**
	 * 初始化文件上传
	 * 
	 * @param cxt
	 * @param registration
	 */
	protected static void initUpload(ServletContext cxt, Dynamic registration) {
		String tempDir = WebHelper.mappath(cxt, "upload_temp");
		// 如果不存在则创建
		FileHelper.mkDir(tempDir);
		registration.setMultipartConfig(new MultipartConfigElement(tempDir, 50000000, 50000000, 0));// 文件上传
	}

	/**
	 * 文件上传
	 * 
	 * @return
	 */
	@Bean(name = "multipartResolver") // 此处 id 为固定写法，不能随便取名
	public MultipartResolver multipartResolver() {
		StandardServletMultipartResolver resolver = new StandardServletMultipartResolver();
		resolver.setResolveLazily(true);// resolveLazily 属性启用是为了推迟文件解析，以在在 UploadAction 中捕获文件大小异常

		return resolver;
	}

	/**
	 * YAML 配置文件
	 * 
	 * @return YAML 配置文件
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer cfger = new PropertySourcesPlaceholderConfigurer();
		cfger.setIgnoreUnresolvablePlaceholders(true);// Don't fail if @Value is not supplied in properties. Ignore if not found
		YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
		ClassPathResource c = new ClassPathResource("application.yml");

		if (c.exists()) {
			yaml.setResources(c);
			cfger.setProperties(yaml.getObject());
		} else
			LOGGER.warning("未设置 YAML 配置文件");

		return cfger;
	}

	/**
	 * 全局异常拦截器
	 * 
	 * @return 全局异常拦截器
	 */
	@Bean
	public GlobalExceptionHandler GlobalExceptionHandler() {
		return new GlobalExceptionHandler();
	}

	/**
	 * Spring IoC 工具
	 * 
	 * @return IoC 工具
	 */
	@Bean
	public DiContextUtil DiContextUtil() {
		return new DiContextUtil();
	}
}
