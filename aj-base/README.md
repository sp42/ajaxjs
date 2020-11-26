
[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-base)
![coverage](https://img.shields.io/badge/coverage-80%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

# AJAXJS Framework Base 基础库

AJAXJS Base 是一个小型的、快速可用的工具库，除了 Javassist 基本没有依赖其他第三方组件，为纯 Java 项目。既然为工具库（Utils、Helper），那么它符合以下特征：1、多为静态方法；2、应比较精炼，不是什么方法都往里面加。它有以下子模块：

- ioc/aop，一个基于 Javassist 的 ioc 依赖注射实现。[原理](http://blog.csdn.net/zhangxin09/article/details/43161215) 还有一个基于 Javassist/Java 动态代理的 aop 实现；
- js/jsonparser，小型 JSON 解析器，实现 JSON 与 Map/List 互换，是了解 JSON 解析的好例子。[文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/JSON%20%E8%A7%A3%E6%9E%90%E5%99%A8?sort_id=1577735)；
- util/common，常规工具包，有字符串工具类、编码工具类、常用日期处理类、值处理工具类等。[文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/%20%E5%B8%B8%E8%A7%81%E5%B7%A5%E5%85%B7%E6%96%B9%E6%B3%95?sort_id=1565971)；
- util/logger，基于 java.util.logger 封装的日志。 [文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/LOGGER%20%E6%97%A5%E5%BF%97%E7%BB%84%E4%BB%B6?sort_id=1558526)；
- util/io，有文件工具类、资源扫描器和简易图片处理器。 [文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/IOHelper?sort_id=1567016)；
- util/reflect，反射工具包，进行反射操作封装。 [文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/%E5%8F%8D%E5%B0%84?sort_id=1558528)；
- util/cryptography，加密解密工具包。 [文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/%E5%8A%A0%E5%AF%86%E8%A7%A3%E5%AF%86?sort_id=1567011)；
- net/mail，无须 JavaMail 发送邮件。原理是通过最简单的 telnet。发送 [文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/%E9%82%AE%E4%BB%B6%E5%8F%91%E9%80%81%E7%BB%84%E4%BB%B6?sort_id=1558530)；
- net/http，发送 HTTP 的客户端。[文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/%E5%8F%91%E9%80%81%20HTTP%20%E8%AF%B7%E6%B1%82%E7%BB%84%E4%BB%B6?sort_id=1558524)
- keyvalue，键对值转换的助手类 。[文 档](https://gitee.com/sp42_admin/ajaxjs/wikis/MapTools?sort_id=1324340)



# AJAXJS Framework Data 数据层

AJAXJS Base 包括了 AJAXJS Data。AJAJX-Data 类似于 DBUtils 的数据库工具程序，但更为简单和干净，核心的类只有两个：JdbcHelper（不超过 600 行）和 DaoHandler（不超过 250 行）。

AJAXJS-Data 主要实现了以下功能：

- 基于 JDBC 对象 Connection、PreparedSatatement、ResultSet 的 CRUD 封装，应用了 Java 8 函数式的风格；
- 半 ORM 实现，需要开发者手写 SQL，然后返回结果，结果类型兼容 Java Bean 和 Map 的两种实体；
- 提供基于 接口+注解 DAO 的服务，类似于 MyBatis 接口方式的 SQL 陈述，基于 Java 动态代理的实现；
- 内置分页服务，提供基于 List 扩展的 PageList 容器；
- 其他 SQL 工具函数，例如格式化 SQL、打印真实 SQL 语句。
- 仅支持两种数据库： MySQL 和 SQLite
- 两张表之间的关联要手写 SQL


安装
---------
要求 Java 1.8+，Jar 直接下载：[200kb](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-base/1.2.3/ajaxjs-base-1.2.3.jar)

Maven 坐标：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-base</artifactId>
  <version>${ajaxjs-base.version}</version>
</dependency>
```

参见
-----
[Wiki 教程](https://gitee.com/sp42_admin/ajaxjs/wikis) | [JavaDoc](https://framework.ajaxjs.com/framework/javadoc/ajaxjs-base/)

最后是我之前的尝试，记录在博客上了。

- http://blog.csdn.net/zhangxin09/article/details/17403211
- http://blog.csdn.net/zhangxin09/article/details/7899525
- http://blog.csdn.net/zhangxin09/article/details/55805849
- http://blog.csdn.net/zhangxin09/article/details/70187712

单元测试覆盖率
---------
工具：EclEmma

![输入图片说明](https://static.oschina.net/uploads/img/201802/20113259_XALo.jpg "在这里输入图片标题")


鸣谢
---------
本人水平有限，特别引用了下面的源码。
- 日志组件引用了 Tomcat 的 FileHandler 源码。
- 使用了 Twitter 的分布式自增 ID 算法 Snowflake:雪花生成器。
- 数据层参考了开源项目：https://gitee.com/bitprince/memory


官网
---------
点击进入 [framework.ajaxjs.com](https://framework.ajaxjs.com]) 了解更多信息。


版权声明 LICENSE
---------
作者版权所有，开源许可：Apache License, Version 2.0
