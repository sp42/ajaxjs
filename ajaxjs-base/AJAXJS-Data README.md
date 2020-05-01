[![Maven Central](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-data/badge.svg)](https://maven-badges-generator.herokuapp.com/maven-central/com.ajaxjs/ajaxjs-data)
![coverage](https://img.shields.io/badge/coverage-70%25-yellowgreen.svg?maxAge=2592000)
[![License](https://img.shields.io/badge/license-Apache--2.0-green.svg?longCache=true&style=flat)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![Email](https://img.shields.io/badge/Contact--me-Email-orange.svg)](mailto:support@ajaxjs.com)
[![QQ群](https://framework.ajaxjs.com/framework/asset/qq.svg)](https://shang.qq.com/wpa/qunwpa?idkey=3877893a4ed3a5f0be01e809e7ac120e346102bd550deb6692239bb42de38e22)

AJAXJS-Data 简介
===========
AJAJX-Data 类似于 DBUtils 的数据库工具程序，但更为简单和干净，核心的类只有两个：JdbcHelper（不超过 600 行）和 DaoHandler（不超过 250 行）。

AJAXJS-Data 主要实现了以下功能：

- 基于 JDBC 对象 Connection、PreparedSatatement、ResultSet 的 CRUD 封装，应用了 Java 8 函数式的风格；
- 半 ORM 实现，需要开发者手写 SQL，然后返回结果，结果类型兼容 Java Bean 和 Map 的两种实体；
- 提供基于 接口+注解 DAO 的服务，类似于 MyBatis 接口方式的 SQL 陈述，基于 Java 动态代理的实现；
- 内置分页服务，提供基于 List 扩展的 PageList 容器；
- 其他 SQL 工具函数，例如格式化 SQL、打印真实 SQL 语句。

尚不支持的特性
----------
- 仅支持两种数据库： MySQL 和 SQLite
- 两张表之间的关联要手写 SQL


安装
---------

要求 Java 1.8+，Jar 直接下载：[～56kb](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-data/1.0.8/ajaxjs-data-1.0.8.jar)

Maven 坐标：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-data</artifactId>
  <version>${ajaxjs_data.version}</version>
</dependency>
```

教程
---------
参见 [wiki](https://gitee.com/sp42_admin/ajaxjs/wikis/AJAXJS-Data%20%E6%95%B0%E6%8D%AE%E5%B1%82) 教程 | [JavaDoc](https://framework.ajaxjs.com/framework/javadoc/ajaxjs-data/)



鸣谢
---------
参考的开源项目：https://gitee.com/bitprince/memory

使用了 Twitter 的分布式自增ID算法 Snowflake:雪花生成器。

最后是我之前的尝试，记录在博客上了。

- http://blog.csdn.net/zhangxin09/article/details/17403211
- http://blog.csdn.net/zhangxin09/article/details/7899525
- http://blog.csdn.net/zhangxin09/article/details/55805849
- http://blog.csdn.net/zhangxin09/article/details/70187712


官网
---------
点击进入 [framework.ajaxjs.com](https://framework.ajaxjs.com]) 了解更多信息。


版权声明 LICENSE
---------
作者版权所有，开源许可：Apache License, Version 2.0









