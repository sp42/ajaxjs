package com.ajaxjs.spring;

import java.io.File;

import org.apache.catalina.Context;
import org.apache.catalina.Engine;
import org.apache.catalina.Host;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardEngine;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.startup.ContextConfig;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * 嵌入式 Tomcat
 * 
 * @author Frank Cheung sp42@qq.com
 *
 */
public class EmbedTomcat {

	/**
	 * 一般不用设置，使用默认即可
	 *
	 * @param tomcat
	 */
	public static void setEngineHost(Tomcat tomcat) {
		Engine engine = new StandardEngine();// 创建一个引擎,放入 service 中
		engine.setDefaultHost("localhost");
		engine.setName("myTomcat");

		Host host = new StandardHost(); // 添加 host
		host.setName("localhost");

		engine.addChild(host);
		tomcat.getService().setContainer(engine);
	}

	public static void setDir(Tomcat tomcat, AnnotationConfigWebApplicationContext ac) {
		// 设置 tomcat 启动后的工作目录
		Context context1 = tomcat.addContext("/", new File(System.getProperty("java.io.tmpdir")).getAbsolutePath());
		Tomcat.addServlet(context1, "my", new DispatcherServlet(ac)).setLoadOnStartup(1);
		context1.addServletMappingDecoded("/", "my");

		// 初始化 ContextConfig 配置
		StandardContext ctx = new StandardContext();
		ctx.addLifecycleListener(new ContextConfig());
		ctx.addApplicationEventListener(new ContextLoaderListener(ac));
	}
}
