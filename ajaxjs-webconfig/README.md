通用的网站配置系统
=================================== 
基于 JSON 格式的配置系统，无限扩展配置内容，可视为全局默认配置文件。原理上 JSON 的解析使用了 JRE 自带的 JavaScript 引擎 Rhino/Nashorn，无须其他第三方 JAR 包。原理参见[《Java Web：JSON 作为配置文件，简单读写的方法》](http://blog.csdn.net/zhangxin09/article/details/46241449)。

如果你不需要这项特性，删掉 classpath 的 site_config.js 就可以了。

配置系统
----------------
JSON 文件 site_config.js 必须位于 classpath 下，必须定义一个名为 bf_Config 在内：


    bf_Config = {
	  site : {
		titlePrefix : "炜爵爷•川式料理",
		keywords : "炜爵爷",
		description : "炜爵爷饮食有限公司于2015年成立，本公司在服务出品环节上，团队以ISO9000为蓝本建立标准化餐饮体系，务求以崭新的姿态面向社会各界人仕，提供更优质的服务以及出品。",
		footCopyright:"dsds" 
	  },
	  dfd:{
		dfd:'fdsf',
		dfdff:{
			dd:'fd'
		}
	  },
	  clientFullName:"炜爵爷•川式料理",
	  clientShortName:"炜爵爷"
	};
    
    
所有 JSON 的配置经过“扁平化转换”保存到 InitConfig.allConfig 对象中，形如：

    {
	  "site_titlePrefix":"炜爵爷•川式料理",
	  "site_keywords":"炜爵爷",
	  "site_description":"炜爵爷饮食有限公司于2015年成立，本公司在服务出品环节上，团队以ISO9000为蓝本建立标准化餐饮体系，务求以崭新的姿态面向社会各界人仕，提供更优质的服务以及出品。",
	  "site_footCopyright":"dsds",
	  "dfd_dfd":"fdsf",
	  "dfd_dfdff_dd":"fd",
	  "clientFullName":"炜爵爷•川式料理",
	  "clientShortName":"炜爵爷"
    }
    
这时，我们可以调用下面 API：

- 调用 InitConfig.getAsString(configName) 是根据配置名称读取配置值（String 类型）
- 调用 InitConfig.getAsInt(configName) 是根据配置名称读取配置值（Int 类型）
- 调用 InitConfig.allConfig.getHash() 可返回配置 Map

Web 环境下使用 WebListener 登记 Servlet 初始化事件，保存了 allConfig 对象为 all_config，于是在 JSP 或 Servlet 中，可调用：

    Object _config = request.getServletContext().getAttribute("all_config");
    Map<String, Object> config = (Map<String, Object>)_config;
    Object configValue = config.get("XXX");
    
或者更简单的 El 表达式：
 
    ${all_config.XXX}
    
网站结构
------------------
这里的 网站结构 指的是 Web 虚拟目录，也就是通过 JSON 来反映网站外显的结构。同样，这个 JSON 位于 classpath/site_stru.js，且命名为 bf.AppStru.data 的变量：

    bf.AppStru.data = [ {
	  'name' : "·炜爵爷传",
	  'id' : 'biography',
	  'children' : [ {
		name : "下载标志",
		id : 'logo'
	  } ]
      }, {
	  'name' : "·美味菜单",
	  'id' : 'menu',
	  'children' : [ {
		name : "菜单一",
		id : 'menu-1'
	  } ]
      }, { 
	  'name' : "·最新资讯",
	  'id' : 'news'
      }, {
	  'name' : "·爵爷互动",
	  'id' : 'interact'
      }, {
	  'name' : "·联系我们",
      'id' : 'contact'
    }];

如果你不需要这项特性，删掉 classpath 的 site_stru.js 就可以了。


Web 环境配置
--------------
一般无须在 web.xml 配置。那是因为我们使用 Servlet 3 注解特性（皆是 @WebListener）。下面是启动配置时机（进阶）：

|类别|方法|    时机|
|----|-----|---|
|静态成员|InitConfig 类 static 静态块|JVM 加载类时| 
|侦听器|InitConfig(ServletContextListener).contextInitialized(ServletContextEvent e)|第一次有请求的时候执行|
|侦听器|NodeListener(ServletRequestListener).requestInitialized(ServletRequestEvent e) |每次请求都会调用|


![输入图片说明](http://img.blog.csdn.net/20150621145821155 "在这里输入图片标题")

修改配置
-------------
若需要修改配置的话，你需要一个后台。这里我们使用本身的 admin UI 框架（允许后续扩展），试着在 web.xml 加入：

	<!-- Admin UI 界面框架 -->
	<servlet>
		<servlet-name>AdminUI</servlet-name>
		<servlet-class>com.ajaxjs.adminUI.Controller</servlet-class>
	</servlet>
	<servlet-mapping>
		<servlet-name>AdminUI</servlet-name>
		<!-- 可指定路径，注意要归属于下面 filter 验证地址 -->
		<url-pattern>/config_admin</url-pattern>
	</servlet-mapping>
	
	<!-- 指定一种登录验证方式。当前为 HTTP Basic Auth -->
	<filter>
		<filter-name>HttpBasicAuthFilter</filter-name>
		<filter-class>com.ajaxjs.user.auth.HttpBasicAuthFilter</filter-class>
		<!-- adminPassword 指定登录密码；如不指定，则默认为 123123 -->
		<init-param>
			<param-name>adminPassword</param-name>
			<param-value>a123123</param-value>
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>HttpBasicAuthFilter</filter-name>
		<!-- 可指定路径，应使用 * 泛指该目录下所有路径 -->
		<url-pattern>/config_admin/*</url-pattern>
	</filter-mapping>

后台路径是 /user_admin。当然验证方式可以不一样，这里是最简单的 HTTP Basic Auth，账号密码是 admin/123123。

当前项目只是为常规性的网站提供诸如 title 标题、简介的配置编辑功能。当然你完全可以扩展更多，但配置文件只有一个 site_config.js（JSON 可以无限扩展），然后在 Admin UI 的 admin-menu.jsp 菜单加入 url 即可，具体是编辑 WebContent/WEB-INF/jsp/admim-menu.jsp 文件。  