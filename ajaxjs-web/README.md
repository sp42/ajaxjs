#AJAXJS Web
=================================== 

WEB 模块包含以下子模块：

1.  web 主要针对 Http 与 Servlet 工具类，例如通过 HttpServletRequestWrapper 扩展了 Request 对象。
2.  view 视图模块，提供一个简单的、基于 JSP/JSTL 的扩展标签。
3.  net 网络模块，包括文件上传器、JSON API 的 DTO 实现、FTP 简易客户端和邮件发送器 mail（无须依赖 JavaMail）。
4.  secutiy 将来提供安全模块（TODO）

数据库连接
因为使用了 Tomcat 数据库连接池，所以依赖于 Tomcat 的 web 环境，于是数据库的配置文件也就不在 properties 文件中，取而代之的是 WebContent/META-INF/context.xml 配置文件。
 