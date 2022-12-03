CREATE TABLE IF NOT EXISTS `wf_process` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程名称',
  `display_name` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程显示名称',
  `type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程类型',
  `instance_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '实例 url',
  `state` tinyint(2) DEFAULT NULL COMMENT '流程是否可用',
  `content` longblob COMMENT '流程模型定义',
  `version` int(2) DEFAULT NULL COMMENT '版本',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
	`update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE
) COMMENT='流程定义';

CREATE TABLE IF NOT EXISTS `wf_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `order_no` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程实例编号',
  `process_id` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '流程定义 id',
  `priority` tinyint(2) DEFAULT NULL COMMENT '优先级',
  `parent_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父流程 id',
  `parent_node_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父流程依赖的节点名称',
  `expire_time` datetime DEFAULT NULL COMMENT '期望完成时间',
  `variable` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附属变量 json 存储',
  `version` int(3) DEFAULT NULL COMMENT '版本',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '发起人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(11) NULL DEFAULT NULL COMMENT '发起人 id',
	`create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发起日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '上次更新人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(11) NULL DEFAULT NULL COMMENT '上次更新人 id',
	`update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次更新日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `IDX_ORDER_PROCESSID` (`process_id`),
  KEY `IDX_ORDER_NO` (`order_no`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='流程实例';

CREATE TABLE IF NOT EXISTS `wf_order_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `order_no` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程实例编号',
  `order_state` tinyint(2) NOT NULL COMMENT '状态',
  `process_id` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '流程定义 id',
  `priority` tinyint(2) DEFAULT NULL COMMENT '优先级',
  `parent_id` int(11) DEFAULT NULL COMMENT '父流程 id',
  `parent_node_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父流程依赖的节点名称',
  `end_time` datetime DEFAULT NULL COMMENT '完成时间',
  `expire_time` datetime DEFAULT NULL COMMENT '期望完成时间',
  `variable` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附属变量 json 存储',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '发起人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(11) NULL DEFAULT NULL COMMENT '发起人 id',
	`create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发起日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '上次更新人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(11) NULL DEFAULT NULL COMMENT '上次更新人 id',
	`update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上次更新日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `IDX_ORDER_PROCESSID` (`process_id`),
  KEY `IDX_ORDER_NO` (`order_no`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='历史流程实例';

CREATE TABLE IF NOT EXISTS `wf_order_cc` (
  `order_id` int(11) DEFAULT NULL COMMENT '流程实例 id',
  `actor_id` int(11) DEFAULT NULL COMMENT '参与者 id',
  `create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
  `stat` tinyint(2) DEFAULT NULL COMMENT '状态',
  KEY `IDX_CCORDER_ORDER` (`order_id`)
) DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='抄送实例';


CREATE TABLE IF NOT EXISTS `wf_task` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `order_id` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '流程实例 id',
  `task_name` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `display_name` varchar(200) COLLATE utf8mb4_bin NOT NULL COMMENT '任务显示名称',
  `task_type` varchar(50) NOT NULL COMMENT '任务类型',
  `perform_type` varchar(50) DEFAULT NULL COMMENT '参与类型',
  `operator` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务处理人',
  `finish_time` datetime DEFAULT NULL COMMENT '任务完成时间',
  `expire_time` datetime DEFAULT NULL COMMENT '任务期望完成时间',
  `action_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务处理的 url',
  `parent_task_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父任务 id',
  `variable` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附属变量 json 存储',
  `version` tinyint(1) DEFAULT NULL COMMENT '版本',
	`creator` VARCHAR(50) NULL DEFAULT NULL COMMENT '创建人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`creator_id` INT(11) NULL DEFAULT NULL COMMENT '创建人 id',
	`create_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
	`updater` VARCHAR(50) NULL DEFAULT NULL COMMENT '修改人名称（可冗余的）' COLLATE 'utf8mb4_bin',
	`updater_id` INT(11) NULL DEFAULT NULL COMMENT '修改人 id',
	`update_date` DATETIME NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `IDX_TASK_ORDER` (`order_id`),
  KEY `IDX_TASK_TASKNAME` (`task_name`),
  KEY `IDX_TASK_PARENTTASK` (`parent_task_id`)
)  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='任务';

CREATE TABLE IF NOT EXISTS `wf_task_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `order_id` int(11) NOT NULL COMMENT '流程实例 id',
  `task_name` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '任务名称',
  `display_name` varchar(200) COLLATE utf8mb4_bin NOT NULL COMMENT '任务显示名称',
  `task_type` varchar(50) NOT NULL COMMENT '任务类型',
  `perform_type` varchar(50) DEFAULT NULL COMMENT '参与类型',
  `task_state` tinyint(2) NOT NULL COMMENT '任务状态',
  `operator` int(11) DEFAULT NULL COMMENT '任务处理人',
  `finish_time` datetime DEFAULT NULL COMMENT '任务完成时间',
  `expire_time` datetime DEFAULT NULL COMMENT '任务期望完成时间',
  `action_url` varchar(200) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '任务处理的 url',
  `parent_task_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父任务 id',
  `variable` varchar(2000) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '附属变量 json 存储',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id`) USING BTREE,
  KEY `IDX_TASK_ORDER` (`order_id`),
  KEY `IDX_TASK_TASKNAME` (`task_name`),
  KEY `IDX_TASK_PARENTTASK` (`parent_task_id`)
)  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='历史任务';

CREATE TABLE IF NOT EXISTS `wf_task_actor` (
  `task_id` int(11) NOT NULL COMMENT '任务ID',
  `actor_id` int(11) NOT NULL COMMENT '参与者ID',
  KEY `IDX_TASKACTOR_TASK` (`task_id`)
)  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='任务参与者';

CREATE TABLE IF NOT EXISTS `wf_surrogate` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `process_name` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '流程名称',
  `operator` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '授权人',
  `surrogate` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '代理人',
  `odate` datetime DEFAULT NULL COMMENT '操作时间',
  `sdate` datetime DEFAULT NULL COMMENT '开始时间',
  `edate` datetime DEFAULT NULL COMMENT '结束时间',
  `state` tinyint(1) DEFAULT NULL COMMENT '状态',
  PRIMARY KEY (`id`),
  KEY `IDX_SURROGATE_OPERATOR` (`operator`)
)  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='委托代理';