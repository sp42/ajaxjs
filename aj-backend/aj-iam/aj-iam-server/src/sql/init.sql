CREATE TABLE `access_token` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
	`access_token` VARCHAR(255) NOT NULL COMMENT 'Access Token' COLLATE 'utf8mb4_unicode_ci',
	`refresh_token` VARCHAR(255) NULL DEFAULT NULL COMMENT 'Refresh Token' COLLATE 'utf8mb4_unicode_ci',
	`user_id` INT(10) NULL DEFAULT NULL COMMENT '关联的用户 id',
	`user_name` VARCHAR(100) NULL DEFAULT NULL COMMENT '用户名（冗余的）' COLLATE 'utf8mb4_unicode_ci',
	`client_id` VARCHAR(50) NULL DEFAULT NULL COMMENT '接入的客户端ID' COLLATE 'utf8mb4_unicode_ci',
	`expires_date` DATETIME NULL DEFAULT NULL COMMENT '过期的具体时间',
	`grant_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '授权类型，比如：authorization_code' COLLATE 'utf8mb4_unicode_ci',
	`scope` VARCHAR(100) NULL DEFAULT NULL COMMENT '可被访问的用户的权限范围，比如：basic、super' COLLATE 'utf8mb4_unicode_ci',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建日期',
	`update_date` DATETIME NULL DEFAULT 'CURRENT_TIMESTAMP' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
)
COMMENT='AccessToken 令牌信息'


CREATE TABLE `app` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
	`pid` INT(10) NOT NULL DEFAULT '0' COMMENT '父级 id',
	`name` VARCHAR(20) NOT NULL COMMENT '名称' COLLATE 'utf8mb4_unicode_ci',
	`content` VARCHAR(256) NULL DEFAULT NULL COMMENT '简介' COLLATE 'utf8mb4_unicode_ci',
	`client_id` VARCHAR(256) NOT NULL COMMENT '客户端 id' COLLATE 'utf8mb4_unicode_ci',
	`client_secret` VARCHAR(256) NOT NULL COMMENT '客户端秘钥' COLLATE 'utf8mb4_unicode_ci',
	`redirect_uri` VARCHAR(256) NOT NULL COMMENT '用户授权完成之后重定向回你的应用' COLLATE 'utf8mb4_unicode_ci',
	`type` VARCHAR(20) NULL DEFAULT 'MISC' COMMENT '应用类型：HTML, APP，API_SERVICE, RPC_SERVICE, MISC' COLLATE 'utf8mb4_unicode_ci',
	`logo` VARCHAR(200) NULL DEFAULT NULL COMMENT '图标' COLLATE 'utf8mb4_unicode_ci',
	`stat` TINYINT(3) NULL DEFAULT '1' COMMENT '数据字典：状态',
	`expires` INT(10) NULL DEFAULT NULL COMMENT 'Token 有效期，单位：分钟',
	`extend` TEXT NULL DEFAULT NULL COMMENT '扩展 JSON 字段' COLLATE 'utf8mb4_unicode_ci',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
	`update_date` DATETIME NULL DEFAULT 'CURRENT_TIMESTAMP' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
)
COMMENT='应用/客户端'

CREATE TABLE `user` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
	`uid` BIGINT(19) NULL DEFAULT NULL COMMENT '唯一不重复 id，可以是雪花 id',
	`org_id` INT(10) NULL DEFAULT NULL COMMENT '部门 ID',
	`tenant_id` INT(10) NULL DEFAULT NULL COMMENT '租户 id',
	`login_id` VARCHAR(20) NOT NULL COMMENT '用户登录 id，不可重复' COLLATE 'utf8mb4_unicode_ci',
	`name` VARCHAR(20) NULL DEFAULT NULL COMMENT '用户名称，可以重复' COLLATE 'utf8mb4_unicode_ci',
	`real_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '用户真实姓名' COLLATE 'utf8mb4_unicode_ci',
	`content` VARCHAR(256) NULL DEFAULT NULL COMMENT '备注' COLLATE 'utf8mb4_unicode_ci',
	`gender` TINYINT(3) NULL DEFAULT NULL COMMENT '性别',
	`birthday` DATE NULL DEFAULT NULL COMMENT '出生日期',
	`avatar` VARCHAR(255) NULL DEFAULT NULL COMMENT '头像' COLLATE 'utf8mb4_unicode_ci',
	`stat` TINYINT(3) NULL DEFAULT '0' COMMENT '数据字典：状态',
	`extend` TEXT NULL DEFAULT NULL COMMENT '扩展 JSON 字段' COLLATE 'utf8mb4_unicode_ci',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
	`update_date` DATETIME NULL DEFAULT 'CURRENT_TIMESTAMP' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
	UNIQUE INDEX `uid` (`uid`) USING BTREE
)
COMMENT='关于用户的一些基本情况'


CREATE TABLE `user_account` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
	`user_id` INT(10) NOT NULL COMMENT '用户id',
	`login_type` TINYINT(3) NULL DEFAULT NULL COMMENT '登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）',
	`register_type` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '注册类型',
	`register_ip` VARCHAR(45) NULL DEFAULT NULL COMMENT '注册 ip' COLLATE 'utf8mb4_bin',
	`identifier` VARCHAR(50) NULL DEFAULT NULL COMMENT '第三方应用的 User 唯一标识，如微信的 OpenId' COLLATE 'utf8mb4_bin',
	`password` VARCHAR(255) NULL DEFAULT NULL COMMENT '密码凭证（站内的保存密码，站外的不保存或保存 token）' COLLATE 'utf8mb4_bin',
	`email_verified` TINYINT(1) NULL DEFAULT NULL COMMENT '邮箱是否验证',
	`phone_verified` TINYINT(1) NULL DEFAULT NULL COMMENT '手机是否验证',
	`stat` TINYINT(3) NULL DEFAULT NULL COMMENT '数据字典：状态',
	`uid` BIGINT(19) NULL DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(10) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(10) NULL DEFAULT NULL COMMENT '修改人 id',
	`update_date` DATETIME NULL DEFAULT 'CURRENT_TIMESTAMP' ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
	`update_password_date` DATETIME NULL DEFAULT NULL COMMENT '上次更新密码日期',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='用户账号'

CREATE TABLE `user_login_log` (
	`id` INT(10) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
	`user_id` INT(10) NOT NULL DEFAULT '0' COMMENT '用户 id',
	`user_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '用户登录名（冗余）' COLLATE 'utf8mb4_unicode_ci',
	`login_type` VARCHAR(50) NULL DEFAULT NULL COMMENT '登录类型' COLLATE 'utf8mb4_unicode_ci',
	`ip` VARCHAR(45) NULL DEFAULT NULL COMMENT '登录 ip' COLLATE 'utf8mb4_bin',
	`ip_location` VARCHAR(50) NOT NULL COMMENT '登录地区' COLLATE 'utf8mb4_bin',
	`extend` TEXT NULL DEFAULT NULL COMMENT '扩展 JSON 字段' COLLATE 'utf8mb4_unicode_ci',
	`tenant_id` SMALLINT(5) NULL DEFAULT NULL COMMENT '租户 id。0 = 不设租户',
	`user_agent` VARCHAR(255) NULL DEFAULT NULL COMMENT '客户端标识' COLLATE 'utf8mb4_bin',
	`create_date` DATETIME NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建日期',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
	INDEX `tenantlId` (`tenant_id`) USING BTREE
)
COMMENT='用户登录信息'