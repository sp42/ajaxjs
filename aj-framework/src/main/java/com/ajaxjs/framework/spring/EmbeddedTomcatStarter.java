package com.ajaxjs.framework.spring;

import java.io.File;
import java.util.HashSet;

import javax.servlet.Filter;

import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.descriptor.web.FilterDef;
import org.apache.tomcat.util.descriptor.web.FilterMap;
import org.apache.tomcat.util.scan.StandardJarScanFilter;
import org.springframework.web.SpringServletContainerInitializer;

import com.ajaxjs.Version;
import com.ajaxjs.util.io.FileHelper;
import com.ajaxjs.util.io.Resources;
import com.ajaxjs.util.logger.LogHelper;

/**
 * 嵌入式使用 Tomcat
 */
public class EmbeddedTomcatStarter {
	private static final LogHelper LOGGER = LogHelper.getLog(EmbeddedTomcatStarter.class);

	Tomcat tomcat;

	StandardContext context;

	public static long startedTime;

	public static long springTime;

	/**
	 * LifecycleState.STARTING_PREP 会执行两次，不知为何
	 */
	public boolean isStatedSpring;

	public void init(int port, Class<?>... clz) {
		initTomcat();
		initContext();
//        initFilterByTomcat(UTF8CharsetFilter.class);

		context.addLifecycleListener((LifecycleEvent event) -> {
			if (isStatedSpring || (event.getLifecycle().getState() != LifecycleState.STARTING_PREP))
				return;

			BaseWebInitializer.coreStartup(context.getServletContext(), clz);
//			anotherWayToStartStrping();

			isStatedSpring = true;
			springTime = System.currentTimeMillis() - startedTime;
		});

		context.addLifecycleListener(new Tomcat.FixContextListener());// required if you don't use web.xml
		// 通过注解的方式初始化 Spring 的上下文，注册 Spring 的配置类（替代传统项目中 xml 的 configuration）
//        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
//        ac.setServletContext(context.getServletContext());
//        context.addApplicationEventListener(new ContextLoaderListener(ac));

		// 注册前端控制器
//        Wrapper servlet = Tomcat.addServlet(context, "dispatcherServlet", new DispatcherServlet(ac));
//        servlet.setLoadOnStartup(1);
//        servlet.addMapping("/");

		setTomcatDisableScan();
		initConnector(port);

		try {
			tomcat.start(); // tomcat 启动
		} catch (LifecycleException e) {
			LOGGER.warning(e);
			throw new RuntimeException(e);
		}

//        ac.register(clz);
//        ac.refresh();
//        ac.registerShutdownHook();

		String tpl = "Web 服务启动完毕。Spring 耗时：%sms，总耗时：%sms";
		tpl = String.format(tpl, springTime, System.currentTimeMillis() - startedTime);
		LOGGER.info(tpl);

		tomcat.getServer().await(); // 保持主线程不退出，让其阻塞，不让当前线程结束，等待处理请求
	}

	/**
	 * 另外一种方式启动 Spring。但不能加入配置类 clz，且更繁琐。 WebAppInitializer 需要实现
	 * WebApplicationInitializer
	 */
	@SuppressWarnings("unused")
	private void anotherWayToStartStrping() {
		try {
			new SpringServletContainerInitializer().onStartup(new HashSet<Class<?>>() {
				private static final long serialVersionUID = 1L;

				{
					add(BaseWebInitializer.class);
				}
			}, context.getServletContext());
		} catch (Throwable e) {
			LOGGER.warning(e);
		}
	}

	/**
	 * 在 Tomcat 初始化阶段设置 Filter
	 */
	@SuppressWarnings("unused")
	private void initFilterByTomcat(Class<? extends Filter> filterClz) {
		FilterDef filter1definition = new FilterDef();
		filter1definition.setFilterName(filterClz.getSimpleName());
		filter1definition.setFilterClass(filterClz.getName());
		context.addFilterDef(filter1definition);

		FilterMap filter1mapping = new FilterMap();
		filter1mapping.setFilterName(filterClz.getSimpleName());
		filter1mapping.addURLPattern("/*");
		context.addFilterMap(filter1mapping);
	}

	/**
	 * 初始化 Tomcat 实例
	 */
	private void initTomcat() {
		startedTime = System.currentTimeMillis();

		tomcat = new Tomcat();
		if (Version.isDebug)
			tomcat.setBaseDir(Resources.getJarDir()); // 设置 JSP 编译目录
		tomcat.enableNaming();
		tomcat.getHost().setAutoDeploy(false);
		tomcat.getHost().setAppBase("webapp");

		close();
	}

	public static String getDevelopJspFolder() {
		return Resources.getResourcesFromClasspath("META-INF\\resources");// 开放调试阶段，直接读取源码的
	}

	/**
	 * 读取项目路径
	 */
	private void initContext() {
		String jspFolder = getDevelopJspFolder();

		if (jspFolder == null) {
			jspFolder = Resources.getJarDir() + "/../webapp"; // 部署阶段。这个并不会实际保存 jsp。因为 jsp 都在 META-INF/resources 里面。但因为下面的 addWebapp() 又需要
			FileHelper.mkDir(jspFolder);
		}

//        System.out.println("jspFolder::::::" + Resources.getJarDir());
//        StandardContext ctx = (StandardContext) tomcat.addWebapp("/", new File("/mycar/mycar-service-4.0/security-oauth2-uam/sync/jsp").getAbsolutePath());
		context = (StandardContext) tomcat.addWebapp("", jspFolder);
		context.setReloadable(false);// 禁止重新载入

		// seems not work
		WebResourceRoot resources = new StandardRoot(context);// 创建 WebRoot
		String classDir = new File("target/classes").getAbsolutePath();
		resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", classDir, "/"));// tomcat 内部读取 Class 执行
	}

	/**
	 * 创建连接器，并且添加对应的连接器，同时连接器指定端口 设置 IO 协议
	 *
	 * @param port
	 */
	private void initConnector(int port) {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setPort(port);
		connector.setThrowOnFailure(true);

		tomcat.getService().addConnector(connector);// 只能设置一个 service,直接拿默认的
		tomcat.setConnector(connector); // 设置执行器
	}

	/**
	 * 禁止 Tomcat 自动扫描 jar 包，那样会很慢
	 */
	private void setTomcatDisableScan() {
		StandardJarScanFilter filter = (StandardJarScanFilter) context.getJarScanner().getJarScanFilter();
//      filter.setDefaultTldScan(false);
		/*
		 * 这个对启动 tomcat 时间影响很大 又 很多 Servlet 3.0 新特性，不能禁掉，比如在 jar 里面放
		 * jsp（部署时候就会这样，但开放阶段不用）。 故，用 isDebug 判断下
		 */
		if (Version.isDebug)
			filter.setDefaultPluggabilityScan(false);
//      String oldTldSkip = filter.getTldSkip();
//      System.out.println("-------" + oldTldSkip);
//      String newTldSkip = oldTldSkip == null || oldTldSkip.trim().isEmpty() ? "pdq.jar" : oldTldSkip + ",pdq.jar";
//      filter.setTldSkip(newTldSkip);
	}

	public void close() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				LOGGER.info("关闭 Tomcat");
				tomcat.destroy();
			} catch (LifecycleException e) {
				LOGGER.warning(e);
			}
		}));
	}

	public static void start(int port, Class<?>... clz) {
		new EmbeddedTomcatStarter().init(port, clz);
	}
}
