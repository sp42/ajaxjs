# 数据库文档展示工具
数据库文档展示工具（database doc），又叫数据库注释浏览工具，是一个简单的数据库展示各个字段注释的开源工具。如下截图所示，它包含下面的功能。

- 读取数据库的元数据（Meta data）转化为 HTML 呈现。
- 支持多数据源切换。当前仅支持 MySQL 数据库
- 提供一个简单的代码生成器，可快速地将某张表转化为 JavaBeanBean
- Java Spring 项目，依赖少，方便集成

# 集成方法
提示：该工具使用“前后端分类”架构，如果单纯只为展示数据库文档，那么只提供前端 UI 读取的 JSON 数据即可，不需要后台服务。
如果你希望管理多个数据库的库或数据源，就要涉及 SQL （存储起来）和 Java 后台服务。
请接着继续看如何集成。

- 创建数据库，仅需一张表。新建数据源表 adp_datasource 如下：

```
CREATE TABLE \`adp_datasource\` (
    \`id\` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    \`name\` VARCHAR(45) NOT NULL COMMENT '名称' COLLATE 'utf8mb4_unicode_ci',
    \`url_dir\` VARCHAR(50) NOT NULL COMMENT '数据源编码，唯一' COLLATE 'utf8mb4_unicode_ci',
    \`type\` VARCHAR(50) NOT NULL DEFAULT '' COMMENT '数据源类型' COLLATE 'utf8mb4_unicode_ci',
    \`url\` VARCHAR(255) NOT NULL COMMENT '连接地址' COLLATE 'utf8mb4_unicode_ci',
    \`username\` VARCHAR(255) NULL DEFAULT NULL COMMENT '登录用户' COLLATE 'utf8mb4_unicode_ci',
    \`password\` VARCHAR(255) NULL DEFAULT NULL COMMENT '登录密码' COLLATE 'utf8mb4_unicode_ci',
    \`connect_ok\` TINYINT(1) NULL DEFAULT NULL COMMENT '是否连接验证成功',
    \`stat\` TINYINT(3) NULL DEFAULT NULL COMMENT '数据字典：状态',
    \`cross_db\` TINYINT(1) NULL DEFAULT NULL COMMENT '是否跨库',
    \`uid\` BIGINT(19) NULL DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
    \`creator\` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_unicode_ci',
    \`creator_id\` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
    \`create_date\` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
    \`updater\` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_unicode_ci',
    \`updater_id\` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
    \`update_date\` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (\`id\`) USING BTREE,
    UNIQUE INDEX \`id_UNIQUE\` (\`id\`) USING BTREE
)
COMMENT='数据源'
```

- Java API 部分，主要分为数据源本身的 CRUD 服务，和切换数据源产生文档 JSON 这么两个部分。数据源的 CRUD 由下面 `DataSourceController` 控制器处理：

`java
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ajaxjs.data_service.controller.BaseDataSourceController;

@RestController
@RequestMapping("/data_service/datasource")
public class DataSourceController extends BaseDataSourceController {
	final static String TABLE_NAME = "aj_base.adp_datasource"; // 配置表名。注意你的数据连接如果不指定库名（为了跨库），则要添加上完整的库名

	@Override
	protected String getTableName() { 
		return TABLE_NAME;
	}
}
`

我们的 API 设计风格即是，类库提供抽象基类，让用户继承它，并提供相关的参数配置。



# 使用技巧

TODO