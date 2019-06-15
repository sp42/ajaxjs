AJAXJS-Framework 框架基础服务
===========

这是一个真正的框架级别的模块。ajaxjs-web 还是一个库，不是一个框架。框架的意思我觉得是完整的方案，库只是提供框架的一部分功能或内容。框架比库高级，是库的延伸。功能上讲 ajaxs-web 做的只是 MVC，CRUD 等其他基本项目所用到的还是于此 ajaxjs-framework 来完成。因此这是一个完成的框架方案，提供所需的功能模块，不一定全都面，但应该都适合一个典型的网站后台。



- config 基于本地 JSON 文件的通用配置系统，完全可以代替 properties 文件实现配置模块。

- PageTag 分页标签


安装
---------

要求 Java 1.8+，JSP v2.2+。Jar 体积约～1.19MB（包含了较多的 CSS/HTML/jpg 网站素材），[直接下载 jar 包](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-web/1.1.5/ajaxjs-web-1.1.3.jar)。

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
|javax.ws.rs-api| 通过 JSR-311 注解定义控制器|
|javax.validation| 通过 JSR-303 数据校验|
|ajaxjs-base| 基础工具库，详情[请点击](../ajaxjs-base)|

功能说明
===========