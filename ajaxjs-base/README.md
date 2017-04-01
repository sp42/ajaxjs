#AJAXJS Base
------------
ajaxjs-base 为框架基础模块包，包含相当多的工具类或静态方法。该模块包含以下子模块：util、net、jdbc 和 framework。base 与特定 jvm 环境脱离，纯 Java 项目可运行在 web、swing、android 环境中，包括以下内容：

- util 工具包（链式风格文件处理包、Map 结构工具类、反射工具包、日志工具类、简易 ioc 依赖注射器、基于 Java Rhino 的 JSON 解析器）；
- jdbc JDBC 数据工具包；
- net 网络通讯包（http 链式风格请求包、mail 基于 telnet 的简易邮件发送器）；
- framework 数据层服务，泛型封装 Service/DAO/Model 提供快速增删改查服务，包含分页，查询条件，整合 Validator 后端验证。


首先，ajaxjs 的设计原则是尽量减少引入新的概念和新的第三方框架或库，而仅仅是在 Java 类库的核心概念之上进行梳理和进一步简化，个人认为一个第三方 jar 包就是一门新的 DSL 语言，DSL语言不管大小，对心智来说都是一个额外的负担。如果没有足够的理由，没有必要去使用新的 DSL；其次，在写法上极其平常，顶多是引入链式写法（或所谓的流式接口），同时在 api 命名上，尽量准确精炼而且是众人所熟知的，不标新立异；最后就是代码行数少，看一下大部分每个类总共几十行，学习和理解的曲线低。

util
-----------
通用工具包，有字符串工具类、反射操作封装、常用日期处理，还有基于 java.util.logger 日志。该模块包含以下子模块：

- io，处理流 Stream 的包，使用了链式调用的风格。有文件、流工具类和简易图片处理器；
- ioc，一个简易的 ioc 依赖注射实现；
- map，Map 集合工具类和一个本地缓存类；

[详细用法参见文档](http://http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-base/src/com/ajaxjs/util?dir=1&filepath=ajaxjs-base%2Fsrc%2Fcom%2Fajaxjs%2Futil)。

jdbc
-----------
jdbc 围绕数据库而设的一些工具类。

[详细用法参见文档](http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-base/src/com/ajaxjs/jdbc?dir=1&filepath=ajaxjs-base%2Fsrc%2Fcom%2Fajaxjs%2Fjdbc)。

net
-----------
网络通讯类。该模块包含以下子模块：

- http 可发送 GET/POST/PUT/DELETE 请求。远没有 Apache HttpClient 完善，但足以满足一般的请求；
- mail 无须 JavaMail 发送邮件。通过最简单的 telnet 原理发送；
- ip IP 工具类

[详细用法参见文档](http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-base/src/com/ajaxjs/net?dir=1&filepath=ajaxjs-base%2Fsrc%2Fcom%2Fajaxjs%2Fnet)。

js
-------------
JSON 解析包。本着 Json/Js 亲缘的关系，大胆采用 JDK 自带的 JavaScript 引擎实现做一个 JSON 解析器。

[详细用法参见文档](http://git.oschina.net/sp42/ajaxjs/tree/master/ajaxjs-base/src/com/ajaxjs/js?dir=1&filepath=ajaxjs-base%2Fsrc%2Fcom%2Fajaxjs%2Fjs)。

framework
-----------
数据业务层框架，该模块包含以下子模块：
- dao，Data Access Object 数据访问对象
- exception，封装特定的异常信息以便确定问题
- model，数据建模
- service，服务层

