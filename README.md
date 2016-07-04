#AJAXJS Web
=================================== 
AJAXJS Web 是一款基于 Java 平台原创的 Web 框架，继承了 Java 平台的高效、安全、稳定、跨平台等诸多优势， 但却摒弃了传统 SSH 企业级架构所带来的庞大和臃肿，非常适合互联网中小型网站的应用。

设计目标：clean, simple, fast.

[点击这里你可以链接到主页](http://framework.ajaxjs.com/framework/)
[作者博客](http://blog.csdn.net/zhangxin09/) 欢迎加入 QQ 群讨论：3150067

- 一体化全栈式方案，提供完整的前后端解决方案，包括原创的前端库及基于主流选择的服务框架
- 服务端堆栈 Servlet3.0/Spring MVC、HTML5/JSP、 JDBC/Mybatis、MySQL/Sqlite + Shiro权限、Node.js/Nashorn/Rhino
- 前端采取免 jQuery 依赖的原生 JS+预编译样式 Less.js 方案，并逐步向强类型的、真正组件化的 TypeScript/React 推进

###设计原则 Design Principles
快速、高效，轻量级维护，提倡敏捷开发；遵循“够用就好、就地取材”原则，谨慎采用第三方库；提供完备的文档、充足的注释和大量单元测试。 AJAXJS 会一直免费、开源。

###基础功能 Foundation
实体快速 CRUD 并装配；前端基于 TypeScript+React 组件，提供 List/Carousel/Grid/Tree/HTMLEditor 等控件；响应式设计兼容 PC/H5App/微信公众号

### 基于分层的模块化思想

服务端采用分层的思想分为以下几个部分。

 *   ajaxjs-base 基础模块，包含相当多的工具类或静态方法 [jar 包下载（去除注释和单测仅77kb大小，约七千多行代码）](http://git.oschina.net/sp42/ajaxjs/raw/master/java-project/ajaxjs-ssm/WebContent/WEB-INF/lib/ajaxjs-base-1.0.0.jar) [源码直达](http://git.oschina.net/sp42/ajaxjs/tree/master/java-project/ajaxjs-base/src?dir=1&amp;filepath=java-project%2Fajaxjs-base%2Fsrc) [文档](http://framework.ajaxjs.com/framework/javadoc2/ajaxjs-base/)。该模块下又包含以下子模块：
    
    1.  util 通用工具类，有集合工具类、字符串工具类、反射工具类、文件和流工具类、日期工具类，缓存类，还有一个基于 java.util.logger 日志，简易图片处理器，一个小巧 ioc 依赖注射实现。
    2.  net 网络通讯类，当前是类似 HttpClient 的一个“轮子”，可发送 GET/POST 请求。
    3.  json 解析包。本着 Json/Js 亲缘的关系，大胆采用 JDK 自带的 JavaScript 实现做一个 JSON 解析器。
    4.  jdbc 围绕数据库而设的一些工具类

base 与特定运行脱离，可运行在 web、swing、android 环境中，也就是 jvm 上通用。

*   ajaxjs-web WEB 模块。[jar 包下载](http://git.oschina.net/sp42/ajaxjs/raw/master/java-project/ajaxjs-ssm/WebContent/WEB-INF/lib/ajaxjs-web-1.0.0.jar) [源码直达（约八千多行代码）](http://git.oschina.net/sp42/ajaxjs/tree/master/java-project/ajaxjs-web/src?dir=1&amp;filepath=java-project%2Fajaxjs-web%2Fsrc&amp;oid=a3430ec9e7d4eac0ba248236ecffd93c41fd7f61&amp;sha=74eae52bc99f431fa9700ffaad382a480266022b) [文档](http://framework.ajaxjs.com/framework/javadoc2/ajaxjs-web/)。该模块下又包含以下子模块：
   
    1.  web 主要针对 Web 常见的工序提供了自己的逻辑和实现，例如通过 HttpServletRequestWrapper 扩展了 Request 对象。
    2.  view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签。
    3.  net，继续提供更多的网络模块，包括文件上传器、JSON API 的 DTO 实现、FTP 简易客户端和邮件发送器 mail（无须依赖 JavaMail）。
    4.  secutiy 将来提供安全模块（TODO）

*   ajaxjs-ssm Spring+Spring MVC+Mybatis 框架的一个实现
*   ajaxjs-es 基于 Elasticsearch 实现（TODO）

上述提供的 JAR 包里面已经包含源码和文档了，无须额外附加。源码分别在不同的项目之中，项目是标准的Eclipse EE结构，导入即可。