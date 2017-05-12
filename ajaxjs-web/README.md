#AJAXJS Web
=================================== 

WEB 模块包含以下子模块：

1.  web 主要针对 Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象。
2.  view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签。
3.  net 网络模块，包括文件上传器、JSON API 的 DTO 实现、FTP 简易客户端和邮件发送器 mail（无须依赖 JavaMail）。

数据库连接
因为使用了 Tomcat 数据库连接池，所以依赖于 Tomcat 的 web 环境，于是数据库的配置文件也就不在 properties 文件中，取而代之的是 WebContent/META-INF/context.xml 配置文件。
 
 
包含下面模块

|名称|说明|
|------|----|
|ajaxjs-web|Web 的基础类库|
|ajaxjs-webconfig|基于 JSON 格式的配置系统|
|ajaxjs-security|安全框架|
|ajaxjs-page-editor| HTML 页面编辑器|
