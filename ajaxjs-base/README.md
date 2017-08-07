#AJAXJS Base
------------
框架基础模块包 ajaxjs-base 为纯 Java 项目与特定 jvm 环境脱离，可运行在 web、swing、android 环境中。Ajaxjs-base 包含了相当多的工具类或静态方法，有以下子模块：

util
-----------
常规工具包，有字符串工具类、常用日期处理类、值处理工具类等，还有该模块包含以下子模块：

- logger，基于 java.util.logger 封装的日志
- collection，集合工具类类，map 的工具方法，还有 map/list 的遍历器
- io，处理流 Stream 的包，使用了链式调用的风格。有文件、流工具类和简易图片处理器；
- reflect，反射工具包，进行反射操作封装；
- ioc，一个简易的 ioc 依赖注射实现；
- aop，一个简易的 aop 实现；

[>>详细用法参见手册](http://ajaxjs.mydoc.io/?t=207309)。

jdbc
-----------
jdbc 围绕数据库而设的一些工具类。

[>>详细用法参见手册](http://ajaxjs.mydoc.io/?t=145194)。

net
-----------
网络通讯类。该模块包含以下子模块：

- http 可发送 GET/POST/PUT/DELETE 请求。远没有 Apache HttpClient 完善，但足以满足一般的请求；
- mail 无须 JavaMail 发送邮件。原理是通过最简单的 telnet 发送；
- ip IP 工具类

[>>详细用法参见手册](http://ajaxjs.mydoc.io/?t=203095)。

js
-------------
小型 JSON 解析器。

[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。

framework
-----------
数据业务层框架，提供泛型封装 Service/DAO/Model 提供快速增删改查服务，包含分页，查询条件，整合 Validator 后端验证。该模块包含以下子模块：
- dao，Data Access Object 数据访问对象
- model，数据模型
- service，业务服务层

[>>详细用法参见手册](http://ajaxjs.mydoc.io/?t=203095)。


config
---------

基于 JSON 的配置系统。

[>>详细用法参见文档](http://ajaxjs.mydoc.io/?t=208700)。
