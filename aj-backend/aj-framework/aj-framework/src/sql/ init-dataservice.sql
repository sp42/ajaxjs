CREATE TABLE `ds_common_api` (
    `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `pid` INT(10) NULL DEFAULT '-1' COMMENT '父节点 id，如果是顶级节点为 -1 或者空',
    `name` VARCHAR(90) NULL DEFAULT NULL COMMENT '说明',
    `namespace` VARCHAR(50) NOT NULL COMMENT '命名空间，标识',
    `table_name` VARCHAR(45) NULL DEFAULT NULL COMMENT '表名',
    `type` VARCHAR(10) NULL DEFAULT NULL COMMENT '类型 SINGLE | CRUD',
    `clz_name` VARCHAR(10) NULL DEFAULT NULL COMMENT '实体类引用名称',
    `id_field` VARCHAR(10) NULL DEFAULT 'id' COMMENT '主键字段名称',
    `sql` TEXT NULL DEFAULT NULL COMMENT '单条 SQL 命令',
    `info_sql` TEXT NULL DEFAULT NULL COMMENT '查询详情的 SQL（可选的）',
    `list_sql` TEXT NULL DEFAULT NULL COMMENT '查询列表的 SQL（可选的）',
    `create_sql` TEXT NULL DEFAULT NULL COMMENT '创建的 SQL（可选的）',
    `update_sql` TEXT NULL DEFAULT NULL COMMENT '修改的 SQL（可选的）',
    `delete_sql` TEXT NULL DEFAULT NULL COMMENT '删除的 SQL（可选的）',
    `del_field` VARCHAR(100) NULL DEFAULT 'stat' COMMENT '删除字段名称',
    `has_is_deleted` TINYINT(1) NULL DEFAULT '1' COMMENT '是否有逻辑删除标记',
    `tenant_isolation` TINYINT(3) UNSIGNED NULL DEFAULT NULL COMMENT '是否加入租户数据隔离',
    `id_type` TINYINT(3) UNSIGNED NULL DEFAULT NULL COMMENT '1=自增；2=雪花；3=UUID',
    `stat` TINYINT(3) UNSIGNED NULL DEFAULT '0' COMMENT '数据字典：状态',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' ,
    `creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT (now()) COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NOT NULL DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
)
COMMENT='通用万能型 API 接口的配置'
COLLATE='utf8mb4_unicode_ci'

CREATE TABLE `ds_project` (
    `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
    `datasource_id` INT(10) NULL DEFAULT NULL COMMENT '数据源 id',
    `name` VARCHAR(90) NULL DEFAULT NULL COMMENT '说明',
    `api_prefix_dev` VARCHAR(100) NULL DEFAULT NULL COMMENT 'API 前缀（开发阶段）',
    `api_prefix_prod` VARCHAR(100) NULL DEFAULT NULL COMMENT 'API 前缀（生产环境）',
    `default_config` JSON NULL DEFAULT NULL COMMENT '默认配置',
    `stat` TINYINT(3) UNSIGNED NULL DEFAULT '0' COMMENT '数据字典：状态',
    `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
    `creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
    `create_date` DATETIME NOT NULL DEFAULT (now()) COMMENT '创建日期',
    `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
    `updater_id` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
    `update_date` DATETIME NOT NULL DEFAULT (now()) ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
)
COMMENT='项目'

INSERT INTO `ds_common_api` (`id`, `pid`, `name`, `namespace`, `table_name`, `type`, `clz_name`, `id_field`, `sql`,
`info_sql`, `list_sql`, `create_sql`, `update_sql`, `delete_sql`, `del_field`, `has_is_deleted`, `tenant_isolation`, `id_type`, `stat`)
VALUES (1, -1, '工程', 'project', 'ds_project', NULL, NULL, 'id', NULL, NULL, NULL, NULL, NULL, NULL, 'stat', 1, NULL, NULL, 0);

INSERT INTO `ds_common_api` (`id`, `pid`, `name`, `namespace`, `type`, `table_name`, `clz_name`, `id_field`, `sql`,
`info_sql`, `list_sql`, `create_sql`, `update_sql`, `delete_sql`, `del_field`, `has_is_deleted`, `tenant_isolation`, `id_type`, `stat`)
VALUES (5, -1, 'common_api', 'common_api', '', 'common_api', NULL, 'id', NULL, NULL, 'SELECT \r\n	(SELECT JSON_ARRAYAGG(JSON_OBJECT(\r\n
\'id\', id, \'pid\', pid, \'name\', name, \'namespace\', namespace, \'tableName\', table_name, \'idField\', id_field,\r\n
\'className\', clz_name, \'delField\', del_field, \'sql\', `sql`, \'infoSql\', info_sql, \'listSql\', list_sql, \'type\', type,\r\n
\'createSql\', create_sql, \'updateSql\', update_sql, \'deleteSql\', delete_sql, \'hasIsDeleted\',\r\n
CAST(has_is_deleted = 1 AS JSON), \'tenantIsolation\', tenant_isolation, \'idType\', id_type, \'stat\', stat, \'creator\', creator,\r\n
\'creatorId\', creator_id, \'createDate\', create_date, \'updater\', updater,\r\n
 \'updaterId\', updater_id, \'updateDate\', update_date\r\n	)) FROM ds_common_api WHERE stat != 1 AND pid = a.id) AS children,\r\n
 a.*\r\n FROM ds_common_api a WHERE a.stat != 1 AND (pid IS NULL OR pid = -1)', NULL, NULL, NULL, 'stat', 1, NULL, NULL, 0);