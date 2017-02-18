方案选型原则：简明快速、小巧精致、开源主流。

数据库
------------------

选用 MySQL/SQLite。如果项目并发量不大可使用 SQLite，更简单。相关 JDBC 驱动下载如下。
-     MySQL [驱动](http://www.mysql.com/products/connector/)
-     SQLite [驱动](https://bitbucket.org/xerial/sqlite-jdbc)

数据库工具除了大家经常使用的 NativeCat，我还推荐 MySQL 官方的 [MySQL Workbench](http://www.mysql.com/products/workbench/)，免费、开源。

ORM：MyBatis
------------------
MyBatis 支持 XML、注解和 [SQLBuilder](http://www.mybatis.org/mybatis-3/statement-builders.html) 这三种方式构建 SQL 语句。我比较多采用 SQLBuilder，可在 Java 中动态组织 SQL，便于封装。另外，[DbUtils](http://commons.apache.org/proper/commons-dbutils/) 也蛮不错。

数据库连接池
------------------
使用 Tomcat 自带的 JDBC Pool，支持高并发应用环境。

后端字段验证器
------------------

数据验证一般基于 JSR 303 规范。常见的 Hibernate Validator 体积 13MB，另外一个 Apache BVal 才 400kb——果断后者。参见我写的[《数据验证框架 Apache BVal 简介》](http://blog.csdn.net/zhangxin09/article/details/50600575)。