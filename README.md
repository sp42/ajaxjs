#AJAXJS Web
=================================== 

设计目标：clean, simple, fast.

[官网](http://framework.ajaxjs.com/framework/)
[作者博客](http://blog.csdn.net/zhangxin09/) 欢迎加入 QQ 群讨论：3150067

AJAXJS Framework 基于 [Apache License Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html) 开源协议发布。

 *   ajaxjs-base 基础模块，包含相当多的工具类或静态方法  [源码](http://git.oschina.net/sp42/ajaxjs/tree/master/java-project/ajaxjs-base/src?dir=1&amp;filepath=java-project%2Fajaxjs-base%2Fsrc) [文档](http://framework.ajaxjs.com/framework/javadoc2/ajaxjs-base/)。该模块包含以下子模块：
    
    1.  util 通用工具类，有集合工具类、字符串工具类、反射工具类、文件和流工具类、日期工具类，缓存类，还有一个基于 java.util.logger 日志，简易图片处理器，一个简易的 ioc 依赖注射实现。
    2.  net 网络通讯类，当前是类似 HttpClient 的一个“轮子”，可发送 GET/POST 请求。
    3.  json 解析包。本着 Json/Js 亲缘的关系，大胆采用 JDK 自带的 JavaScript 实现做一个 JSON 解析器。
    4.  jdbc 围绕数据库而设的一些工具类

base 与特定 jvm 环境脱离，可运行在 web、swing、android 环境中。

*   ajaxjs-web WEB 模块。[源码](http://git.oschina.net/sp42/ajaxjs/tree/master/java-project/ajaxjs-web/src?dir=1&amp;filepath=java-project%2Fajaxjs-web%2Fsrc&amp;oid=a3430ec9e7d4eac0ba248236ecffd93c41fd7f61&amp;sha=74eae52bc99f431fa9700ffaad382a480266022b) 。该模块包含以下子模块：
   
    1.  web 主要针对 Web 常见的工序提供了自己的逻辑和实现，例如通过 HttpServletRequestWrapper 扩展了 Request 对象。
    2.  view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签。
    3.  net 网络模块，包括文件上传器、JSON API 的 DTO 实现、FTP 简易客户端和邮件发送器 mail（无须依赖 JavaMail）。
    4.  secutiy 将来提供安全模块（TODO）
