#AJAXJS Base
------------
框架基础包，纯 Java 项目（不限定 Web/Android/Swing 环境），包括以下内容：

- util 工具包（链式风格文件处理包、Map 结构工具类、反射工具包、日志工具类、简易 ioc 依赖注射器、基于 Java Rhino 的 JSON 解析器）
- jdbc JDBC 数据工具包
- net 网络通讯包（http 链式风格请求包、mail 基于 telnet 的简易邮件发送器）
- framework 数据层服务，泛型封装 Service/DAO/Model 提供快速增删改查服务，包含分页，查询条件，整合 Validator 后端验证

如果发现缺少 HttpServletRequest 等的类，请手动把 javax-servlet.jar 加入到 classpath 中。

IOC 注入
-------
支持简易的反转控制，参见[《极简版 Java 依赖注射 》](http://blog.csdn.net/zhangxin09/article/details/43161215)。

Controller 中 service 需要被注入：

	import com.ajaxjs.util.ioc.Bean;
	import com.ajaxjs.util.ioc.Resource;
	@Controller
	@Path("/version")
	@Bean("versionController") 
	public class VersionController extends ReadOnlyController<Version> {
		@Resource("versionService") // 指定 service id
		private VersionService service;
	 
	}
	
Service

	import com.ajaxjs.util.ioc.Bean;

	/**
	 * 版本更新
	 * @author frank
	 *
	 */
	@Bean("versionService")
	public class VersionService extends BaseCrudService<Version, VersionDAO> {
	 
	}

测试注入：

	import com.ajaxjs.util.ioc.BeanContext;
	import com.ajaxjs.util.ioc.Scanner;
	
	public class TestIOC {
	
		@Test
		public void test() {
			// 指定扫描的包名
			// 下面 service 被注入到 controller 中
			Set<Class<?>> classes = new Scanner().scanPackage("com.ajaxjs.app.controller", new Scanner().scanPackage("com.ajaxjs.app.service"));
			
			// 注入
			BeanContext.me().init(classes);
	
			// 按照 bean 名称获取实例
			VersionController versionController = (VersionController) BeanContext.me().getBean("versionController");
	
			// 依赖的 versionService 注入到 versionController
			System.out.println(versionController.getService());
			assertNotNull(versionController.getService());
		}
	}
	
