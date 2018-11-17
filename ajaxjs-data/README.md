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
- 不支持 batch 批处理
- 不支持事务


安装
---------

要求 Java 1.8+，Jar 直接下载：[～56kb](https://search.maven.org/remotecontent?filepath=com/ajaxjs/ajaxjs-data/1.0.8/ajaxjs-data-1.0.8.jar)

Maven 坐标：

```
<dependency>
  <groupId>com.ajaxjs</groupId>
  <artifactId>ajaxjs-data</artifactId>
  <version>1.0.8</version>
</dependency>
```

教程
---------
参见 [wiki](https://gitee.com/sp42_admin/ajaxjs/wikis/AJAXJS-Data%20%E6%95%B0%E6%8D%AE%E5%B1%82) 教程。


鸣谢
---------

由于本人水平有限，动态拼接 SQL 功能引用了 MyBatis 的 SQL Builder 的源码，详见 com.ajaxjs.orm.thirdparty 包。还有“雪花 id”生成器也是网友贡献的。其余 AJAXJS-Data 就没有什么依赖了（数据库 Driver 除外）。

另外下面参考的开源项目：

https://gitee.com/bitprince/memory

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









