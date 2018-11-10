AJAXJS WEB
=============
AJAXJS WEB 包含以下模块：

- mvc 模块，类似 SpringMVC 基于注解的控制器的写法，实现了 URL-METHOD 映射、参数自动化注入（支持 Query/Path 参数，Form2Bean/Map）、RESTFUL 风格支持（基于 JSR 注解）、返回结果解析（原生JSON/HTML/JSP返回）、异常统一结构化规范支持、拦截器支持等等。当前支持 JSP 视图。
- 通过 Servlet 3.0 的 web-fragment 提供了静态资源打包，包括 UI 库的各种控件、自定义标签库、404/500 页等；
- web Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象，还有一个文件上传组件和验证码组件、Mock 模拟对象；
- view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签；
- security 网站防御系统，抵御 XSS、CSRF 等攻击；
- config 小巧、灵活、通用、基于 JSON 的配置系统，完全可以代替 properties 文件实现配置模块。

Maven 坐标
-----------

```
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-web</artifactId>
    <version>1.1.1</version>
</dependency>
```

依赖库说明
-------------
|项目|作用|
|---|---|
|Tomcat JDBCPool|Tomcat 自带的数据库连接池|
|Swagger|生成接口文档|

环境配置说明
-------------
- 数据库连接：因为使用了 Tomcat 数据库连接池，所以依赖于 Tomcat 的 web 环境，于是数据库的配置文件也就不在 properties 文件中，取而代之的是 WebContent/META-INF/context.xml 配置文件。
- 各个项目导出 jar 可交叉复用到不同项目。项目不限定何种 IDE（不包含任何 IDE 配置文件）。但默认地，src 为 Java 源码目录；WebContent 为 Web 项目根目录；lib 为依赖的 jar 包或本项目编译好的 jar 包。
- 详见手册帮助 [http://ajaxjs.mydoc.io/?t=148968](http://ajaxjs.mydoc.io/?t=148968)

前端框架
-----------
前端就要看 DEMO：[https://framework.ajaxjs.com/ui_demo/](https://framework.ajaxjs.com/ui_demo/)，然后右击鼠标查看源码或 F12 即可：）


config
---------

。

[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。