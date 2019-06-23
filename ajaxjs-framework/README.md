AJAXJS-Framework 框架基础服务
===========

这是一个真正的框架级别的模块。[ajaxjs-web](../ajaxjs-web/README.md) 还是一个库，不是一个框架。框架的意思我觉得是完整的方案，库只是提供框架的一部分功能或内容。框架比库高级，是库的延伸。功能上讲 ajaxs-web 做的只是 MVC，CRUD 等其他基本项目所用到的还是于此 ajaxjs-framework 来完成。因此这是一个有完整意义的框架方案，提供所需的功能模块，不一定全都面，但应该都适合一个典型的建站后台。


- config 基于本地 JSON 文件的通用配置系统，完全可以代替 properties 文件实现配置模块。

- PageTag 分页标签


安装
---------

要求 Java 1.8+，JSP v2.2+。Jar 体积约～1.19MB（包含了较多的 CSS/HTML/网站图片素材），[直接下载 jar 包](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-web/1.1.5/ajaxjs-web-1.1.3.jar)。

Maven 坐标：

```
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-web</artifactId>
    <version>${ajaxjs_web.version}</version>
</dependency>
```

依赖库说明
-------------
|项目|作用|
|---|---|
|ajaxjs-data| CRUD 库，详情[请点击](../ajaxjs-data/README.md)|
|ajaxjs-web| Web MVC 库，详情[请点击](../ajaxjs-web/README.md)|

功能说明
===========

全局统一的 HTML 文件头
------------------
统一的 HTML Head 文件头，位于项目的 /META-INF/resources/WEB-INF/head.jsp，提供默认的 CSS Reset 和 JS 框架导入。使用方式有两种：

	<%@include file="/WEB-INF/jsp/head.jsp"%>
	
或者：

	<jsp:include page="/WEB-INF/jsp/head.jsp">
		<jsp:param name="lessFile" value="/asset/less/main.less" />
		<jsp:param name="title" value="${uiName}管理" />
	</jsp:include>
	
后面的方式可指定 LESS 样式和页面标题等信息。


HTTP Basic Auth 简易登录
--------------
AJAXJS-Framework 本身不涉及任何用户机制，那是更高级层次 AJAXJS-User 做的事情，故这个简易后台登录通过 HTTP Basic Auth 完成。


![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/215200_d8fc93d6_784269.png "屏幕截图.png")

通过 Servlet 过滤器拦截。

	<!-- 指定一种登录验证方式。当前为 HTTP Basic Auth -->
	<filter>
		<filter-name>HttpBasicAuthFilter</filter-name>
		<filter-class>com.ajaxjs.web.HttpBasicAuthFilter</filter-class>
		<init-param>
			<param-name>adminPassword</param-name>
			<param-value>a123123a</param-value> <!-- adminPassword 指定登录密码；如不指定，则默认为 123123 -->
		</init-param>
	</filter>
	<filter-mapping>
		<filter-name>HttpBasicAuthFilter</filter-name>
		<url-pattern>/admin/*</url-pattern><!-- 可指定路径，应使用 * 泛指该目录下所有路径 -->
	</filter-mapping>




静态页面修改
-------------

不是修改模版样式，而是指页面中正文这类内容可被修改，例如“公司简介”中的“关于我们”，这段正文不保存在数据库中，而是直接保存在 .html/.jsp 文件中，但是被约定的标签所包围，如：

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/190914_628b6a68_784269.png "屏幕截图.png")

于是通过字符串定位即可修改内容，标签外的 HTML 不予以修改。

编辑方法：先通过预览界面定位欲修改的页面（实际嵌入了一个 iframe），点击【编辑此页面】按钮即可，

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/191524_30f069c5_784269.png "屏幕截图.png")

编辑界面：

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/191630_291b7f8a_784269.png "屏幕截图.png")

网站信息维护
---------------

对应 HTML HEAD 元素的 title/keywords/description 信息。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/205527_9574248a_784269.png "屏幕截图.png")


网站层次结构与页面节点
--------------

网站各个层次的管理，可以创建子目录或者隐藏子目录。


![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/205901_074c563d_784269.png "屏幕截图.png")

对应外界访问网站的导航和二级、三级甚至 n 级的层次。

![输入图片说明](https://images.gitee.com/uploads/images/2019/0623/210046_12179bd2_784269.png "屏幕截图.png")



通用标签库
---------------
分页
列表
正文

配置中心
===========

CRUD与业务逻辑
==========





杂项
==========
静态资源




