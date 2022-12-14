# 数据库文档展示工具
数据库文档展示工具（database doc），又叫数据库注释浏览工具，是一个简单的数据库展示各个字段注释的开源工具。如下截图所示，它包含下面的功能。

- 读取数据库的元数据（Meta data）转化为 HTML 呈现。
- 支持多数据源切换。当前仅支持 MySQL 数据库
- 提供一个简单的代码生成器，可快速地将某张表转化为 JavaBean
- Java Spring 项目，依赖少，方便集成

## 多数据源管理
![多数据源管理](https://img-blog.csdnimg.cn/39e110471b104af897a26eec2b78143c.png)

## 生成 Java Bean
[生成 Java Bean 代码](https://img-blog.csdnimg.cn/acb5f3b506ff4a0380b7f409fd01cc29.png)

# 集成方法
提示：该工具使用“前后端分类”架构，如果单纯只为展示数据库文档，那么只提供前端 UI 读取的 JSON 数据即可，不需要后台服务。

如果你希望管理多个数据库的库或数据源，就要涉及 SQL （存储起来）和 Java 后台服务。请接着继续看如何集成。

## 添加依赖
该工具不是一个独立运行的工程，而是提供 jar 包集成到你的项目中。故你需依赖下面的 Maven：

```xml
<dependency>
	<groupId>com.ajaxjs</groupId>
	<artifactId>ajaxjs-framework</artifactId>
	<version>1.0.5</version>
</dependency>
```
这个 jar 包通过 Servlet 3.0 打包技术，将前端都集成进去 jar 包里了。


## 创建数据库
创建数据库，仅需一张表。新建数据源表 `adp_datasource` 如下：

```sql
CREATE TABLE `adp_datasource` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
	`name` VARCHAR(45) NOT NULL COMMENT '名称' COLLATE 'utf8mb4_unicode_ci',
	`url_dir` VARCHAR(50) NOT NULL COMMENT '数据源编码，唯一' COLLATE 'utf8mb4_unicode_ci',
	`type` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '数据源类型' COLLATE 'utf8mb4_unicode_ci',
	`url` VARCHAR(255) NOT NULL COMMENT '连接地址' COLLATE 'utf8mb4_unicode_ci',
	`username` VARCHAR(255) NULL DEFAULT NULL COMMENT '登录用户' COLLATE 'utf8mb4_unicode_ci',
	`password` VARCHAR(255) NULL DEFAULT NULL COMMENT '登录密码' COLLATE 'utf8mb4_unicode_ci',
	`connect_ok` TINYINT(1) NULL DEFAULT NULL COMMENT '是否连接验证成功',
	`stat` TINYINT(3) NULL DEFAULT NULL COMMENT '数据字典：状态',
	`cross_db` TINYINT(1) NULL DEFAULT NULL COMMENT '是否跨库',
	`uid` BIGINT(19) NULL DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_unicode_ci',
	`creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_unicode_ci',
	`updater_id` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
	`update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
)
COMMENT='数据源'
```
## 配置 API
Java API 部分，主要分为数据源本身的 CRUD 服务，和切换数据源产生文档 JSON 这么两个部分。数据源的 CRUD 由下面 `DataSourceController` 控制器处理（这是一个标准的 Spring MVC 控制器）：

```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.controller.BaseDataSourceController;

@RestController
@RequestMapping("/data_service/datasource") // 这个接口地址不能自定义了，否则前端要跟着改
public class DataSourceController extends BaseDataSourceController {
    // 配置表名。注意你的数据连接（一般是连接字符串）如果不指定库名（为了跨库），则要添加上完整的库名
	final static String TABLE_NAME = "aj_base.adp_datasource"; 

	@Override
	protected String getTableName() { 
		return TABLE_NAME;
	}

    /*
     返回数据库连接。当前的例子从 Spring IOC 返回 DataSource 再得到 Connection，现实中可以按照你的注入方式得到 Connection
    */
	@Override
	protected Connection getConnection() { 
		DataSource ds = DiContextUtil.getBean(DataSource.class);

		try {
			return ds.getConnection();
		} catch (SQLException e) {
			return null;
		}
	}
}
```

我们的 API 设计风格即是，类库提供抽象基类，让用户继承它，并提供相关的参数配置。当前配置有两个,分别对应两个 Java 抽象的方法：一个是配置表名，另外一个是配置数据库的连接。

## 配置 MakeDbDocController


生成数据库信息的 JSON，用于显示数据库文档。

```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.database_meta.BaseMakeDbDocController;

@RestController
@RequestMapping("/make_database_doc")
public class MakeDbDocController extends BaseMakeDbDocController {

}
```

# 使用答疑

- 这个中原理是什么？

很简单，JDBC API 就有强大的数据库元数据获取能力。JDBC MetaData 一种方式是遍历各字段信息。但这种方式通用性不强，依赖于 MySQL，换别的数据库不一定。
另外一种方式就是获取 CREATE DDL（本工具使用的方式），解析这个 DDL 通过开源 SQL 解析工具 `com.github.jsqlparser` 完成。最后一股脑把所有信息生成 JSON 给 UI。

前端基于 Vue2/iView 搞定，没啥难度，就是把那个大 JSON 渲染 UI 出来。

- 只支持 Spring Boot 吗？

不是，老系统的 Spring MVC 也行。原生  Servlet 行不行？抱歉，不行哦~

- 源码在哪里？

前端在 https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-ui-widget/database-doc ，后端其实是 AJ Framework，具体部分在  [1](https://gitee.com/sp42_admin/ajaxjs/tree/master/aj-framework/src/main/java/com/ajaxjs/database_meta)、[2](https://gitee.com/sp42_admin/ajaxjs/blob/master/aj-framework/src/main/java/com/ajaxjs/data_service/controller/BaseDataSourceController.java)。

- 如何打印？

左侧的菜单中，右键鼠标打开新的浏览器创建，这是完成页面，直接打印。

- 如何导出 PDF?

参考“打印”，选择 PDF 格式打印到文件。

- 我不想任何人可以访问页面，怎么加用户登录、验证？

本工具不考虑这方面问题。如同上述集成方式，不直接提供 Controller，而是让你去继承它，至于如何配置用户身份识别，悉随尊便，加过滤器、拦截器都行，是你的事~

提示：简单点搞个 HTTP Basic 认证即可，安全性高点就 HTTP Digest。

- 有否计划其他数据库，如 SQL Server?

很可能不会，精力有限，除非……