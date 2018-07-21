欢迎来到 AJAXJS Web Framework！
=============

全栈 Web 框架，包含 Java 后端框架和 HTML/CSS/JS 前端库。设计目标：Clean, Simple, Fast, Full-stacks & Lightweight.

A fullstack Java web project. It's clean, simple, fast and extensible. Not only backend code writen in Java, but also frontend code in native JavaScript. 

AJAXJS 的设计原则：首先是尽量减少引入新的概念和新的第三方框架或库，而仅仅是在 Java 类库的核心概念之上进行梳理和进一步简化，个人认为一个第三方 jar 包就是一门新的 DSL 语言，DSL 语言不管大小，对心智来说都是一个额外的负担。如果没有足够的理由，没有必要去使用新的 DSL；其次，在写法上极其平常，顶多是引入链式写法（或所谓的流式接口），同时在 api 命名上，尽量准确精炼而且是众人所熟知的，不标新立异；最后就是代码量少，大部分每个类总共几十行，学习和理解的曲线低。详见官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/) 介绍。 

Maven 依赖
```
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>ajaxjs-web</artifactId>
    <version>1.0.9</version>
</dependency>
```

检出源码到本地后，请注意以下两点：
- 设置源码目录（set Source Folder）为 src/main，而非一般的 src/java/main，测试目录也是 src/test 敬请注意。
- 推荐使用约定目录，包括静态注意、模板和数据库配置等等，还有 tag files 标签下载，一键设置请执行 ANT 脚本：[build.xml](https://gitee.com/sp42_admin/ajaxjs/blob/master/build.xml)。

相关项目一览
--------------

|名称|说明|
|------|----|
|ajaxjs-base|基础类库，纯 Java 项目。[跳转项目主页](http://git.oschina.net/sp42/ajaxjs-base)。|
|ajaxjs-web|本项目，Web 的 MVC 框架+小型 UI 库，依赖 base JAR 包。见下面详细介绍。|
|ajaxjs-cms|CMS 网站内容发布系统，依赖上述 base + web 的 JAR 包。[跳转项目主页](http://git.oschina.net/sp42/ajaxjs-cms)。|

AJAXJS WEB
-----------
AJAXJS WEB 包含以下模块：

- 通过 Servlet 3.0 的 web-fragment 提供了静态资源打包，包括 UI 库的各种控件、自定义标签库、404/500 页等；
- web Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象，还有一个文件上传组件和验证码组件、Mock 模拟对象；
- mvc 模块，类似 SpringMVC 基于注解的控制器的写法，注解基于 JSR 标准方案。当前支持 JSP 视图；
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

联系方式
----------

- 官网 [https://framework.ajaxjs.com](https://framework.ajaxjs.com/)  
- 博客 [http://blog.csdn.net/zhangxin09](http://blog.csdn.net/zhangxin09/) 
- Q群 [3150067](//shang.qq.com/wpa/qunwpa?idkey=99415d164e2c776567c9370cc5b0bde26f4e2e7c5068978a24d1fe7c976ace93)
- 邮箱 support@ajaxjs.com

版权声明 LICENSE
==========
作者版权所有，开源许可：Apache License, Version 2.0
 