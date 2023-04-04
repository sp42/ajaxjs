package com.ajaxjs.framework.spring;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import com.ajaxjs.framework.spring.filter.CleanUpMySql;
import com.ajaxjs.spring.BaseSpringWebInitializer;
import org.springframework.web.context.ContextLoaderListener;

import com.ajaxjs.spring.easy_controller.ServiceBeanDefinitionRegistry;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 配置 Spring MVC 实现该接口的类会在 Servlet 容器启动时自动加载并运行
 * <p>
 * 这是依赖 Tomcat（非嵌入式）的使用场景
 *
 * @author Frank Cheung
 */
public abstract class BaseWebInitializer extends BaseSpringWebInitializer implements BaseWebInitializerExtender {
    private static final LogHelper LOGGER = LogHelper.getLog(BaseWebInitializer.class);

    /**
     * 可以不用子类实现
     */
    @Override
    public String getMainConfig() {
//		System.out.println(getClass().getName());
//		return "com.ajaxjs.adp.ADPWebInit.ScanComponent";
        return getClass().getName() + ".ScanComponent";
    }

    @Override
    public void onStartup(ServletContext cxt) throws ServletException {
        if (cxt == null) // 可能在测试
            return;

        super.onStartup(cxt);

        String mainConfig = getMainConfig();

//		servletCxt.setInitParameter("contextConfigLocation", "classpath:applicationContext.xml");
        cxt.setInitParameter("contextClass", "org.springframework.web.context.support.AnnotationConfigWebApplicationContext");
        cxt.setInitParameter("contextConfigLocation", mainConfig);
        cxt.addListener(new ContextLoaderListener()); // 监听器
        cxt.addListener(new CleanUpMySql());

        if (!"".equals(mainConfig)) {// 防呆设计
            try {
                Class.forName(mainConfig);
            } catch (ClassNotFoundException e) {
                String reverse = new StringBuffer(mainConfig).reverse().toString();
                reverse = reverse.replaceFirst("\\.", "\\$"); // 对于内部类，我们需要像下面这样写代码
                String _mainConfig = new StringBuffer(reverse).reverse().toString();

                try {
                    Class.forName(_mainConfig);
                } catch (ClassNotFoundException e1) {
                    LOGGER.warning("找不到 Component Scan 的配置类 " + mainConfig);
                }
            }
        }

//		initWeb(cxt, webCxt); // 如果不想在 WebApplicationInitializer 当前类中注入，可以另设一个类专门注入组件
        LOGGER.info("Web 程序启动完毕");
    }

    public ServiceBeanDefinitionRegistry ServiceBeanDefinitionRegistry(Class<? extends BaseWebInitializer> clz) {
        return new ServiceBeanDefinitionRegistry(clz.getPackage().getName());
    }
}