欢迎来到 AJAXJS: A full-stack Web Framework for Java！
=============
### 特点：全栈 、轻量级、函数式和响应式编程。

前端提供了常见的 UI 组件，后端提供 IOC/AOP/ORM/MVC/REST 等特性，支持 Map/POJO 的 CRUD 服务，基于 DAO/Service/Controller 分层。

- 前端：Vue.js（MVVM+组件化） + LESS.js
- 后端：Java 1.8 + Tomcat 8 + MySQL/SQLite

代码精炼简洁，很少第三方的库或包依赖。功能不算很多，但应该较为实用的那些，也可以在此基础上进行扩展。虽是“轮子”，但代码都自己理过一遍，务求清晰易懂易修改。注释、单测等齐备。详见官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/) 介绍。 

### 各项目一览

|项目名称|简介|依赖库|发布的打包方式|
|------|-----|----|------|
|ajaxjs-base|基础库，包含大量工具函数和实用方法，[浏览项目 readme](ajaxjs-base)|无|jar|
|ajaxjs-data|数据层，类似 DBUtils 的 JDBC 封装，更小巧和简单，[浏览项目 readme](ajaxjs-base)|ajaxjs-base|jar|
|ajaxjs-web|MVC 框架，类似 Spring MVC 的实现，[浏览项目 readme](ajaxjs-base)|ajaxjs-base|jar|
|ajaxjs-web-js|独立的前端框架，基于 vue.js 2.x，[浏览项目 readme](ajaxjs-base)|vue.js|jar|
|ajaxjs-cms|整个各个子项目的网站发布系统，[浏览项目 readme](ajaxjs-base)|ajaxjs-data/ajaxjs-web|jar|
|ajaxjs-demo|一个简单的网站，包含前后台，作为演示样例，方便学习或者快速手脚架，[浏览项目 readme](ajaxjs-base)|ajaxjs-cms|war|

除了前端 js 项目外，依赖关系由上至下排列。

为方便管理，当前在一个 repository 中存放多个项目，且同时支持 Git/SVN 存取。如果您想单独检出某个项目，建议使用 SVN 然后指定目录检出。

Maven 依赖
```
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-web</artifactId>
    <version>1.1.1</version>
</dependency>
```



检出源码到本地后，请注意以下两点：
- 设置源码目录（set Source Folder）为 src/main，而非一般的 src/java/main，测试目录也是 src/test 敬请注意。
- 推荐使用约定目录，包括静态注意、模板和数据库配置等等。

项目依赖
--------------
- ajaxjs-base 基础类库，纯 Java 项目。[跳转项目主页](http://git.oschina.net/sp42_admin/ajaxjs-base)


AJAXJS WEB
-----------
AJAXJS WEB 包含以下模块：

- mvc 模块，类似 SpringMVC 基于注解的控制器的写法，实现了 URL-METHOD 映射、参数自动化注入（支持 Query/Path 参数，Form2Bean/Map）、RESTFUL 风格支持（基于 JSR 注解）、返回结果解析（原生JSON/HTML/JSP返回）、异常统一结构化规范支持、拦截器支持等等。当前支持 JSP 视图。
- 通过 Servlet 3.0 的 web-fragment 提供了静态资源打包，包括 UI 库的各种控件、自定义标签库、404/500 页等；
- web Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象，还有一个文件上传组件和验证码组件、Mock 模拟对象；
- view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签；
- security 网站防御系统，抵御 XSS、CSRF 等攻击；
- config 基于 JSON 格式的配置系统。
- mail 无须 JavaMail 发送邮件。原理是通过最简单的 telnet 发送；

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

小巧、灵活、通用、基于 JSON 的配置系统，完全可以代替 properties 文件实现配置模块。

[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。


开发生涯中一点的个人感悟
----
框架设计中，有什么值得考虑或者取舍的呢？首先是尽量减少引入新的概念和新的第三方框架或库，而仅仅是在 Java 类库的核心概念之上进行梳理和进一步简化，个人认为一个第三方 jar 包就是一门新的 DSL 语言，DSL 语言不管大小，对心智来说都是一个额外的负担。如果没有足够的理由，没有必要去使用新的 DSL；其次，在写法上极其平常，顶多是引入链式写法（或所谓的流式接口），同时在 api 命名上，尽量准确精炼而且是众人所熟知的，不标新立异；最后就是代码量少，大部分每个类总共几十行，学习和理解的曲线低。

联系方式
----------

- 官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/)  
- 博客 [http://blog.csdn.net/zhangxin09](http://blog.csdn.net/zhangxin09/) 
- QQ 群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
 