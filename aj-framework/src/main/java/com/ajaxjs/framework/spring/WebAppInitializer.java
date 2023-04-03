package com.ajaxjs.framework.spring;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration.Dynamic;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import com.ajaxjs.framework.spring.boot.EmbeddedTomcatStarter;
import com.ajaxjs.framework.spring.filter.GlobalExceptionHandler;
import com.ajaxjs.framework.spring.filter.UTF8CharsetFilter;
import com.ajaxjs.spring.DiContextUtil;
import com.ajaxjs.util.logger.LogHelper;

public class WebAppInitializer implements WebApplicationInitializer {
	private static final LogHelper LOGGER = LogHelper.getLog(WebAppInitializer.class);
	
	private Class<?>[] clz;

	public WebAppInitializer() {
	}

	public WebAppInitializer(Class<?>[] clz) {
		this.clz = clz;
	}

	@Override
	public void onStartup(ServletContext ctx) {
		// 通过注解的方式初始化 Spring 的上下文，注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
		AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
		ac.setServletContext(ctx);
		if (!ObjectUtils.isEmpty(clz))
			ac.register(clz);
		ac.refresh();
		ac.registerShutdownHook();
		
		ctx.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
//		cxt.setInitParameter("contextConfigLocation", mainConfig);
		ctx.addListener(new ContextLoaderListener()); // 监听器
		ctx.setAttribute("ctx", ctx.getContextPath()); // 为 JSP 提供 shorthands

		// 绑定 servlet
		Dynamic registration = ctx.addServlet("dispatcher", new DispatcherServlet(ac));
		registration.setLoadOnStartup(1);// 设置 tomcat 启动立即加载 servlet
		registration.addMapping("/"); // 浏览器访问 uri。注意不要设置 /*

		// 字符过滤器
//        new CharacterEncodingFilter("UTF-8")
		FilterRegistration.Dynamic filterReg = ctx.addFilter("InitMvcRequest", new UTF8CharsetFilter());
		filterReg.addMappingForUrlPatterns(null, true, "/*");

		EmbeddedTomcatStarter.springTime = System.currentTimeMillis() - EmbeddedTomcatStarter.startedTime;
	}
	
    /**
     * YAML 配置文件
     *
     * @return YAML 配置文件
     */
    @Bean
    public static PropertySourcesPlaceholderConfigurer properties() {
    	System.out.println("ddddddddd");
        PropertySourcesPlaceholderConfigurer cfger = new PropertySourcesPlaceholderConfigurer();
        cfger.setIgnoreUnresolvablePlaceholders(true);// Don't fail if @Value is not supplied in properties. Ignore if not found
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        ClassPathResource c = new ClassPathResource("application.yml");

        if (c.exists()) {
            yaml.setResources(c);
            cfger.setProperties(yaml.getObject());
        } else LOGGER.warning("未设置 YAML 配置文件");

        return cfger;
    }

    /**
     * 全局异常拦截器
     *
     * @return 全局异常拦截器
     */
    @Bean
    public GlobalExceptionHandler GlobalExceptionHandler() {
    	System.out.println("ddddddddd");
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