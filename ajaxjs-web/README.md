[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-web/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-web)
![coverage](https://img.shields.io/badge/coverage-80%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

AJAXJS WEB
=============
AJAXJS WEB 包含以下模块：

- mvc 模块，类似 SpringMVC 基于注解的控制器的写法，实现了 Url-JavaMethod 映射、参数自动化注入（支持 Query/Path 参数，Form2Bean/Map）、Restful 风格支持（基于 JSR 注解）、返回结果解析（原生JSON/HTML/JSP返回）、异常统一结构化规范支持、拦截器支持等等。当前支持 JSP 视图。
- 通过 Servlet 3.0 的 web-fragment 提供了静态资源打包，包括 UI 库的各种控件、自定义标签库、404/500 页等；
- web Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象，还有一个文件上传组件和验证码组件、Mock 模拟对象；
- view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签；
- upload 简易的文件上传组件；
- security 网站防御系统，抵御 XSS、CSRF 等攻击；
- config 基于本地 JSON 文件的通用配置系统，完全可以代替 properties 文件实现配置模块。

Ajaxjs 前端框架[请点击](../ajaxjs-web-js)。


安装
---------

要求 Java 1.8+，Jar 直接下载：[～1300kb，包含 class 文件 160kb，其余为 js/css/jpg 等静态资源](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-web/1.1.2/ajaxjs-web-1.1.2.jar)

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
|ajaxjs-base| 基础库，详情[请点击](../ajaxjs-base)|


环境配置说明
-------------
Eclipse 用户请注意：有别于一般 Dymanic Web 项目，这是一个 Web-Fragment 项目，它基于 Servlet 3.0 特性，新建项目时适宜于下面如图的类型。

![Web-Fragment 项目](https://images.gitee.com/uploads/images/2018/1117/114116_c739299b_784269.png "TIM截图20181117113715.png")


教程
---------
参见 [wiki](https://gitee.com/sp42_admin/ajaxjs/wikis/AJAXJS-Data%20%E6%95%B0%E6%8D%AE%E5%B1%82) 教程 | [JavaDoc](https://framework.ajaxjs.com/framework/javadoc/ajaxjs-web/)

官网
---------
点击进入 [framework.ajaxjs.com](https://framework.ajaxjs.com]) 了解更多信息。


版权声明 LICENSE
---------
作者版权所有，开源许可：Apache License, Version 2.0

