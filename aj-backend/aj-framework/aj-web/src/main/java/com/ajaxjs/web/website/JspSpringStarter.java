package com.ajaxjs.web.website;

import com.ajaxjs.framework.spring.CustomPropertySources;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Objects;

public abstract class JspSpringStarter implements WebApplicationInitializer {
    abstract public Class<?>[] getConfigClz();

    /**
     * Servlet 容器启动时会自动运行该方法
     */
    @Override
    public void onStartup(ServletContext servletContext)  {
        System.out.println("启动 Spring");

        AnnotationConfigWebApplicationContext cxt = new AnnotationConfigWebApplicationContext();
        cxt.register(getConfigClz());

        servletContext.addListener(new ContextLoaderListener(cxt));
        ServletRegistration.Dynamic registration = servletContext.addServlet("dispatcher", new DispatcherServlet(cxt));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }

    /**
     * YAML 配置文件
     *
     * @return YAML 配置文件
     */
    public static PropertySourcesPlaceholderConfigurer yaml() {
        PropertySourcesPlaceholderConfigurer cfg = new CustomPropertySources();
        cfg.setIgnoreUnresolvablePlaceholders(true);// Don't fail if @Value is not supplied in properties. Ignore if not found
        YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
        ClassPathResource c = new ClassPathResource("application.yml");

        if (c.exists()) {
            yaml.setResources(c);
            cfg.setProperties(Objects.requireNonNull(yaml.getObject()));
        } else
            System.err.println("未设置 YAML 配置文件");

        return cfg;
    }
}
