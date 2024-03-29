# 实用工具：数据库文档展示工具

## 简介
数据库文档展示工具（database doc），又叫数据库注释浏览工具，是一个简单的数据库展示各个字段注释的开源工具。在日常开发工作中，您有否这样的体验？

- 想给前端开发或者其他人员浏览数据库结构、表详情等的信息，出于管理的原因，却又不想告诉他们数据库账号、密码；
- 要生成数据库结构文档或报表，不知有什么好工具。即使生成了，结构变动了，又要再生成一次，——有一个同步的问题
- 根据表结构快速生成 Java 实体

如果有上述场景，那么数据库文档展示工具就是为您而设的。该工具如下截图所示，

<div align="center"><img src="https://img-blog.csdnimg.cn/c579f9f3177f401186bdd02e5a2205b4.png" alt="example2" width="600" /></div>



它包含下面的功能。

- 读取数据库的元数据（Meta data）转化为 HTML 呈现。
- 支持多数据源切换。当前仅支持 MySQL 数据库
- 提供一个简单的代码生成器，可快速地将某张表转化为 JavaBean
- Java Spring 项目，依赖少，方便集成

在线演示地址请点击 <a href="https://framework.ajaxjs.com/demo/database-doc/" target="_blank">https://framework.ajaxjs.com/demo/database-doc/</a>。


## 使用方法

### 多数据源管理

<div align="center"><img src="https://img-blog.csdnimg.cn/39e110471b104af897a26eec2b78143c.png"  width="600" /></div>

### 生成 Java Bean

<div align="center"><img src="https://img-blog.csdnimg.cn/acb5f3b506ff4a0380b7f409fd01cc29.png"  width="600" /></div>

批量下载 Java Bean，打包整个库的 Bean 文件转换 zip 包下载。

<div align="center"><img src="https://img-blog.csdnimg.cn/5f1b79abb0b247ecbef3d331a1b94cf8.png"  width="200" /></div>


# 集成方法
提示：该工具使用“前后端分离”架构，如果单纯只为展示数据库文档，那么只提供前端 UI 读取的 JSON 数据即可，不需要后台服务。看阁下需要，当然那 JSON 要符合特定的格式。

当然，更常见的情况是配合我们所提供的服务端程序一起使用——我们接着来看。

## 添加依赖
该工具不是一个独立运行的工程，而是提供 jar 包集成到你的项目中。故你需依赖下面的 Maven：

```xml
<dependency>
	<groupId>com.ajaxjs</groupId>
	<artifactId>ajaxjs-framework</artifactId>
	<version>1.0.6</version>
</dependency>
```
这个 jar 包通过 Servlet 3.0 打包技术，将前端都集成进去 jar 包里了。


## 配置 MakeDbDocController
我们需要生成数据库信息的 JSON，用于显示数据库文档。怎么生成？必须通过后端也就是一个控制器来生成——那就是 `MakeDbDocController`，它是一个标准的 Spring MVC 控制器。使用它，您要将其继承于基类 `BaseMakeDbDocController`（这个类我们已经提供了）。我们的 API 设计风格即是，类库提供抽象基类，让用户继承它，并提供相关的参数配置。

```java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.database_meta.BaseMakeDbDocController;

@RestController
@RequestMapping("/make_database_doc") // 这个接口地址不能自定义了，否则前端要跟着改
public class MakeDbDocController extends BaseMakeDbDocController {

}
```
当前这个 `MakeDbDocController` 非常简单，并没有什么要配置的地方，所以您需要做的就是复制 MakeDbDocController 到你的工程中去。

至于 `BaseMakeDbDocController` 里面，最主要就是封装了一个 GET 的方法，用于返回配置的 JSON。

这样，启动您的项目，在浏览器访问 `{前缀}/html/database-doc/`，例如 `http://localhost:8080/oba/html/database-doc/` 即可访问页面。

## 配置多数据源的支持
如果你希望管理多个数据库的库或数据源，就要涉及 SQL （存储起来）和 Java 后台服务。请接着继续看如何集成（相对会麻烦一些）。

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
Java API 部分主要是数据源本身的 CRUD 服务。具体来说就是你要复制下面这个 `DataSourceController` 类到你的项目中，并适当修改配置。数据源的 CRUD 由这个控制器来处理：

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

当前配置有两个，分别对应两个 Java 抽象的方法：一个是配置表名，另外一个是配置数据库的连接。这个数据源是能访问到表 `TABLE_NAME = "aj_base.adp_datasource"` 的地方。怎么返回数据库的连接？一般 Spring 项目可以通过注入返回标准 DataSource，从而返回 Connection，例如下面的一个例子。

```java
@Bean(value = "dataSource", destroyMethod = "close")
DataSource getDs() {
	return DataSerivceUtils.setupJdbcPool("com.mysql.cj.jdbc.Driver", url, user, psw);
}
```

具体配置视乎你实际配置而定。只要返回 Connection 就行。



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

- 如何导出 Word/PDF?

Word 的话，全文 Copy & Paste 即可。PDF 的话，在浏览器里面“打印”，选择 PDF 格式打印到文件。

- 我不想任何人可以访问页面，怎么加用户登录、验证？

本工具不考虑这方面问题。如同上述集成方式，不直接提供 Controller，而是让你去继承它，至于如何配置用户身份识别，悉随尊便，加过滤器、拦截器都行，是你的事~

提示：简单点搞个 HTTP Basic 认证即可，安全性高点就 HTTP Digest。

- 有否计划其他数据库，如 SQL Server?

很可能不会，精力有限，除非……