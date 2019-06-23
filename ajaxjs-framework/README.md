AJAXJS-Framework 框架基础服务
===========

这是一个真正的框架级别的模块。[ajaxjs-web](../ajaxjs-web/README.md) 还是一个库，不是一个框架。框架的意思我觉得是完整的方案，库只是提供框架的一部分功能或内容。框架比库高级，是库的延伸。功能上讲 ajaxs-web 做的只是 MVC，CRUD 等其他基本项目所用到的还是于此 ajaxjs-framework 来完成。因此这是一个有完整意义的框架方案，提供所需的功能模块，不一定全都面，但应该都适合一个典型的建站后台。

落到实处看，该项目的主要目标是实现一个网站管理后台，可以发布各项内容或者发布 REST 接口。

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
	</jsp:include>
	
后面的方式可指定 LESS 样式和页面标题等信息。

静态页面修改
-------------

网站信息维护
---------------

网站层次结构与页面节点
--------------

通用标签库
---------------
分页
列表
正文

配置中心
===========

CRUD与业务逻辑
==========



辅助工具
========

表结构浏览器
----------

导出为 Excel 
------------

代码生成器
-----------

查看操作日志
------------

查看 tomcat 日志
---------



杂项
==========
静态资源




