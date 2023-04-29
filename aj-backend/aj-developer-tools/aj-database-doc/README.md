# 实用工具 Database-doc：数据库文档展示工具

数据库文档展示工具（Database-doc），又叫数据库注释浏览工具，是一个简单的数据库展示各个字段注释的开源工具。它包含下面的功能。

- 读取数据库的元数据（Meta data）转化为 HTML 呈现。
- 支持多数据源切换。当前仅支持 MySQL 数据库
- 提供一个简单的代码生成器，可快速地将某张表转化为 JavaBean
- Java Spring 项目，依赖少，方便集成

该工具如下截图所示，
<div align="center"><img src="https://foruda.gitee.com/images/1682750683847627505/bf927537_784269.png" alt="example2" width="600" /></div>

- 在线演示地址请点击 <a href="https://framework.ajaxjs.com/demo/database-doc/" target="_blank">https://framework.ajaxjs.com/demo/database-doc/</a>。
- 源码：[https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-backend/aj-developer-tools/aj-database-doc](https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-backend/aj-developer-tools/aj-database-doc)。
- Maven POM 依赖：

```xml
<!-- Database-doc 数据库文档展示工具 -->
<dependency>
    <groupId>com.ajaxjs</groupId>
    <artifactId>aj-database-doc</artifactId>
    <version>1.0.0</version>
</dependency>
```