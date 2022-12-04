CREATE TABLE IF NOT EXISTS `auth_client_details` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `client_id` varchar(100) NOT NULL COMMENT '接入的客户端 id',
  `name` varchar(20) NOT NULL COMMENT '客户端名称',
  `content` varchar(256) DEFAULT NULL COMMENT '简介',
  `client_secret` varchar(255) NOT NULL COMMENT '接入的客户端的密钥',
  `redirec_uri` varchar(1000) DEFAULT NULL COMMENT '回调地址',
  `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
  `extend` text COMMENT '扩展 JSON 字段',
  `tenant_id` int(11) DEFAULT NULL,
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT = '接入的客户端信息';

CREATE TABLE IF NOT EXISTS `auth_scope` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) NOT NULL COMMENT '客户端名称',
  `content` varchar(256) DEFAULT NULL COMMENT '简介',
  `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
  `extend` text COMMENT '扩展 JSON 字段',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT = '可被访问的用户权限';

CREATE TABLE IF NOT EXISTS `auth_access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `access_token` varchar(255) NOT NULL COMMENT 'Access Token',
  `user_id` int(11) NOT NULL COMMENT '关联的用户 id',
  `user_name` varchar(100) DEFAULT NULL COMMENT '用户名',
  `client_id` int(11) NOT NULL COMMENT '接入的客户端ID',
  `expires_in` bigint(11) NOT NULL COMMENT '过期时间戳',
  `grant_type` varchar(50) DEFAULT NULL COMMENT '授权类型，比如：authorization_code',
  `scope` varchar(100) DEFAULT NULL COMMENT '可被访问的用户的权限范围，比如：basic、super',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT = 'AccessToken 令牌信息';

CREATE TABLE IF NOT EXISTS `auth_refresh_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `refresh_token` varchar(255) NOT NULL COMMENT 'Refresh Token',
  `token_id` int(11) NOT NULL COMMENT 'access_token 对应 的Access Token 记录',
  `expires_in` bigint(11) NOT NULL COMMENT '过期时间戳',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT = 'RefreshToken 令牌信息';

CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) DEFAULT NULL COMMENT '显示名称，昵称（nickname）',
  `username` varchar(20) NOT NULL COMMENT '用户名、登录名',
  `realname` varchar(15) DEFAULT NULL COMMENT '真实姓名',
  `gender` tinyint(2) DEFAULT '0' COMMENT '性别 0=未知/1=男/2=女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `content` varchar(200) DEFAULT NULL COMMENT '简介、描述',
  `phone` varchar(15) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像',
  `location` varchar(50) DEFAULT NULL COMMENT '地区',
  `org_id` int(11) DEFAULT '0' COMMENT '组织 id，对应“数据字典”里面的组织分类',
  `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
  `role_id` int(11) DEFAULT NULL COMMENT '角色 id',
  `uid` bigint(20) DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
  `extend` text COMMENT '扩展 JSON 字段',
  `tenant_id` smallint(6) DEFAULT NULL COMMENT '租户 id。0 = 不设租户',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `tenantlId` (`tenant_id`) USING BTREE
) COMMENT = '用户信息';

CREATE TABLE IF NOT EXISTS `user_auth` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `login_type` tinyint(2) DEFAULT NULL COMMENT '登录类型（手机号 邮箱 用户名）或第三方应用名称（微信 微博等）',
  `register_type` tinyint(2) NOT NULL DEFAULT '0' COMMENT '注册类型',
  `register_ip` varchar(45) DEFAULT NULL COMMENT '注册 ip',
  `identifier` varchar(50) DEFAULT NULL COMMENT '第三方应用的 User 唯一标识，如微信的 OpenId',
  `credential` varchar(255) DEFAULT NULL COMMENT '密码凭证（站内的保存密码，站外的不保存或保存 token）',
  `email_verified` tinyint(1) DEFAULT NULL COMMENT '邮箱是否验证',
  `phone_verified` tinyint(1) DEFAULT NULL COMMENT '手机是否验证',
  `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
  `uid` bigint(20) DEFAULT NULL COMMENT '唯一 id，通过 uuid 生成不重复 id',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `update_password_date` datetime DEFAULT NULL COMMENT '上次更新密码日期',
  PRIMARY KEY (`id`) USING BTREE
) COMMENT = '用户扩展信息';

CREATE TABLE IF NOT EXISTS `user_login_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户 id',
  `login_type` varchar(50) DEFAULT NULL COMMENT '登录类型',
  `ip` varchar(45) DEFAULT NULL COMMENT '登录 ip',
  `ip_location` varchar(50) NOT NULL COMMENT '登录地区',
  `extend` text COMMENT '扩展 JSON 字段',
  `tenant_id` smallint(6) DEFAULT NULL COMMENT '租户 id。0 = 不设租户',
  `user_agent` varchar(255) DEFAULT NULL COMMENT '客户端标识',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `tenantlId` (`tenant_id`) USING BTREE
) COMMENT = '用户信息';

CREATE TABLE IF NOT EXISTS `user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `content` varchar(200) DEFAULT NULL COMMENT '简介、描述',
  `parent_id` int(11) DEFAULT '0' COMMENT '父 id',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户 id。0 = 不设租户',
  `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
  `create_sum` bigint(20) DEFAULT '0' COMMENT '创建操作权限总值',
  `read_sum` bigint(20) DEFAULT '0' COMMENT '读取操作权限总值',
  `update_sum` bigint(20) DEFAULT '0' COMMENT '更新操作权限总值',
  `delete_sum` bigint(20) DEFAULT '0' COMMENT '删除操作权限总值',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `tenantlId` (`tenant_id`) USING BTREE
) COMMENT = '用户角色';

CREATE TABLE IF NOT EXISTS `user_privilege` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(20) NOT NULL COMMENT '名称',
  `content` varchar(200) DEFAULT NULL COMMENT '简介、描述',
  `parent_id` int(11) DEFAULT '0' COMMENT '父 id',
  `stat` tinyint(2) DEFAULT NULL COMMENT '数据字典：状态',
  `tenant_id` int(11) NOT NULL DEFAULT '0' COMMENT '租户 id。0 = 不设租户',
  `creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）',
  `creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）',
  `updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
  `update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `tenantlId` (`tenant_id`) USING BTREE
) COMMENT = '用户权限';