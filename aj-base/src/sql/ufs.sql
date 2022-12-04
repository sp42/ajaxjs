-- Dumping structure for table ufs.ufs_app_memember
CREATE TABLE IF NOT EXISTS `ufs_app_memember` (
  `id` bigint(20) NOT NULL,
  `app_id` varchar(40) DEFAULT NULL,
  `user_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table ufs.ufs_app_memember: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_convert
CREATE TABLE IF NOT EXISTS `ufs_convert` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `original_file_id` bigint(20) DEFAULT NULL COMMENT '原始文件的ID',
  `original_file_md5` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '原始文件的MD5',
  `kind` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '转换的种类',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '转换的名称',
  `params` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '转换的参数',
  `state` smallint(6) NOT NULL COMMENT '转换的状态(0=等待中，1=已成功、-1=已失败）',
  `retries` smallint(6) DEFAULT NULL COMMENT '该作业失败时，已经重试了多少次数',
  `take_time` int(11) DEFAULT NULL COMMENT '该作业的转换耗时',
  `started_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '作业开始转换的时间',
  `created_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '作业的生成时间',
  `converted_file_id` bigint(20) DEFAULT NULL COMMENT '已经成功转换的文件  ID',
  `original_file_size` bigint(20) DEFAULT NULL COMMENT '原始文件的大小',
  `convert_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `kind` (`kind`),
  KEY `name` (`name`(191)),
  KEY `original_file_id` (`original_file_id`),
  KEY `original_file_md5` (`original_file_md5`(191)),
  KEY `original_file_size` (`original_file_size`),
  KEY `retries` (`retries`),
  KEY `state` (`state`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件转换表';

-- Dumping data for table ufs.ufs_convert: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_file_server
CREATE TABLE IF NOT EXISTS `ufs_file_server` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `settings` text,
  `kind` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table ufs.ufs_file_server: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_index
CREATE TABLE IF NOT EXISTS `ufs_index` (
  `file_id` bigint(20) NOT NULL,
  `state` int(11) DEFAULT NULL,
  `retries` int(255) DEFAULT NULL,
  `take_time` int(11) DEFAULT NULL,
  `started_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '作业开始转换的时间',
  `created_at` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '作业的生成时间',
  `index_server_id` bigint(40) DEFAULT NULL,
  `id` bigint(40) DEFAULT NULL,
  `ocr_state` int(11) DEFAULT NULL,
  `tika_state` int(11) DEFAULT NULL,
  `update_state` int(11) DEFAULT NULL,
  `last_updated_at` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='索引';

-- Dumping data for table ufs.ufs_index: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_index_server
CREATE TABLE IF NOT EXISTS `ufs_index_server` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `settings` text,
  `cluster` varchar(200) DEFAULT NULL,
  `cluster_settings` varchar(2000) DEFAULT NULL,
  `seeds` varchar(2000) DEFAULT NULL,
  `kind` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table ufs.ufs_index_server: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_storage_app
CREATE TABLE IF NOT EXISTS `ufs_storage_app` (
  `id` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'ID',
  `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '名称',
  `description` varchar(1000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '描述',
  `settings` text COLLATE utf8mb4_unicode_ci COMMENT '设置',
  `strategy_ids` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `build_index` bit(1) DEFAULT NULL,
  `check_file_field` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='应用表';

-- Dumping data for table ufs.ufs_storage_app: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_storage_area
CREATE TABLE IF NOT EXISTS `ufs_storage_area` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `file_server_id` bigint(20) DEFAULT NULL,
  `index_server_id` bigint(20) DEFAULT NULL,
  `org_ids` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Dumping data for table ufs.ufs_storage_area: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_storage_block
CREATE TABLE IF NOT EXISTS `ufs_storage_block` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `app_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '归属的应用',
  `storage` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '对应的存储名',
  `md5` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '内容块的MD5啥希值',
  `size` bigint(20) NOT NULL COMMENT '内容块的大小',
  `ref_count` int(11) NOT NULL COMMENT '引用计数器',
  PRIMARY KEY (`id`),
  KEY `id` (`app_id`,`storage`,`id`),
  KEY `md5` (`app_id`,`storage`,`md5`(191))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件内容块表';

-- Dumping data for table ufs.ufs_storage_block: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_storage_file
CREATE TABLE IF NOT EXISTS `ufs_storage_file` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `app_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所属的应用',
  `owner_id` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '所有人',
  `block_id` bigint(20) NOT NULL COMMENT '对应的内容块',
  `access_control` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '访问控制(PRIVATE=私有、PUBLIC_READ=公共读、PUBLIC_READWRITE=公共读写)',
  `content_length` bigint(20) NOT NULL COMMENT '文件大小',
  `content_md5` varchar(200) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件的MD5啥希值',
  `created_at` datetime NOT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `response_headers` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'HTTP响应重写头',
  `content_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '文件类型',
  `secret_key` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `index_server_id` bigint(40) DEFAULT NULL,
  `file_name` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件表';

-- Dumping data for table ufs.ufs_storage_file: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_storage_file_metadata
CREATE TABLE IF NOT EXISTS `ufs_storage_file_metadata` (
  `file_id` bigint(20) NOT NULL COMMENT '文件 ID',
  `meta_key` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '元数据 KEY',
  `meta_value` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '元数据 VALUE',
  PRIMARY KEY (`file_id`,`meta_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文件元数据表';

-- Dumping data for table ufs.ufs_storage_file_metadata: ~0 rows (approximately)

-- Dumping structure for table ufs.ufs_storage_strategy
CREATE TABLE IF NOT EXISTS `ufs_storage_strategy` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `is_default` bit(1) DEFAULT NULL,
  `settings` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;