CREATE TABLE `wf_process`
  (
     `id`          INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `name`        VARCHAR(45) NOT NULL comment '流程名称',
     `displayName` VARCHAR(255) DEFAULT NULL comment '流程显示名称',
     `content`     TEXT comment 'XML 定义',
     `instanceUrl` VARCHAR(255) DEFAULT NULL comment '实例 url',
     `version`     TINYINT(4) DEFAULT NULL comment '版本号',
     `stat`        TINYINT(4) DEFAULT NULL comment '数据字典：状态',
     `uid`         BIGINT(20) NOT NULL comment '唯一 id，生成不重复 id',
     `creator`     INT(11) DEFAULT NULL comment '创建者 id',
     `createDate`  DATETIME NOT NULL comment '创建日期',
     `updateDate`  DATETIME NOT NULL comment '修改日期'
  )
charset=utf8mb4 comment='流程定义表';  

CREATE TABLE `wf_order`
  (
     `id`          INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `parentId`    INT(11) DEFAULT NULL comment '父流程ID',
     `processId`   INT(11) NOT NULL comment '流程定义ID',
     `priority`    TINYINT(1) DEFAULT NULL comment '优先级',
     `version`     TINYINT(4) DEFAULT NULL comment '版本号',
     `parentNodeName` VARCHAR(100) DEFAULT NULL comment '父流程依赖的节点名称',
     `orderNo`     VARCHAR(50) DEFAULT NULL comment '流程实例编号',
     `variable`    TEXT DEFAULT NULL comment '附属变量json存储',
     `stat`        TINYINT(4) DEFAULT NULL comment '数据字典：状态',
     `uid`         BIGINT(20) NOT NULL comment '唯一 id，通过 uuid 生成不重复 id',
     `expireDate`  DATETIME DEFAULT NULL comment '期望完成时间',
     `creator`     INT(11) DEFAULT NULL comment '创建者 id',
     `updator`     INT(11) DEFAULT NULL comment '更新者 id',
     `createDate`  DATETIME NOT NULL comment '创建日期',
     `updateDate`  DATETIME NOT NULL comment '修改日期'
  )
charset=utf8mb4 comment='流程实例表';  

CREATE TABLE `wf_order_history`
  (
     `id`          INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `parentId`    INT(11) comment '父流程ID',
     `processId`   INT(11) NOT NULL comment '流程定义ID',
     `priority`    TINYINT(1) DEFAULT NULLcomment '优先级',
     `orderNo`     VARCHAR(50) DEFAULT NULL comment '流程实例编号',
     `variable`    TEXT comment '附属变量json存储',
     `stat`        TINYINT(4) DEFAULT NULL comment '数据字典：状态',
     `uid`         BIGINT(20) NOT NULL comment '唯一 id，通过 uuid 生成不重复 id',
     `expireDate`  DATETIME DEFAULT NULL comment '期望完成时间',
     `endDate`     DATETIME DEFAULT NULL comment '完成时间',
     `creator`     INT(11) DEFAULT NULL comment '创建者 id',
     `updator`     INT(11) DEFAULT NULL comment '更新者 id',
     `createDate`  DATETIME NOT NULL comment '创建日期',
     `updateDate`  DATETIME NOT NULL comment '修改日期'
  )
charset=utf8mb4 comment='历史流程实例表';  

/* history 减少了 version 和 parentNodeName 字段，增加了 endDate 字段 */

CREATE TABLE `wf_task`
  (
     `id`          INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `parentId`    INT(11) DEFAULT NULL comment '父任务 ID',
     `orderId`     INT(11) NOT NULL comment '流程实例 ID',
     `name`        VARCHAR(45) DEFAULT NULL comment '任务名称',
     `displayName` VARCHAR(255) DEFAULT NULL comment '任务显示名称',
     `taskType`    TINYINT(1) NOT NULL comment '任务类型',
     `performType` TINYINT(1) DEFAULT NULL comment '参与类型',
     `operator`    INT(11) DEFAULT NULL comment '任务处理人',
     `actionUrl`   VARCHAR(200) DEFAULT NULL comment '任务处理的url',
     `version`     TINYINT(4) DEFAULT NULL comment '版本号',
     `variable`    TEXT DEFAULT NULL comment '附属变量json存储',
     `stat`        TINYINT(4) DEFAULT NULL comment '数据字典：状态',
     `uid`         BIGINT(20) NOT NULL comment '唯一 id，通过 uuid 生成不重复 id',
     `creator`     INT(11) DEFAULT NULL comment '创建者 id',
     `finishDate`  DATETIME DEFAULT NULL comment '任务完成时间',
     `expireDate`  DATETIME DEFAULT NULL comment '任务期望完成时间',
     `createDate`  DATETIME NOT NULL comment '创建日期',
     `updateDate`  DATETIME NOT NULL comment '修改日期'
  )
charset=utf8mb4 comment='任务表';  

CREATE TABLE `wf_task_history`
  (
     `id`          INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `parentId`    INT(11) DEFAULT NULL comment '父任务 ID',
     `orderId`     INT(11) NOT NULL comment '流程实例 ID',
     `name`        VARCHAR(45) DEFAULT NULL comment '任务名称',
     `displayName` VARCHAR(255) DEFAULT NULL comment '任务显示名称',
     `taskType`    TINYINT(1) NOT NULL comment '任务类型',
     `performType` TINYINT(1) DEFAULT NULL comment '参与类型',
     `operator`    INT(11) DEFAULT NULL comment '任务处理人',
     `actionUrl`   VARCHAR(200) DEFAULT NULL comment '任务处理的url',
     `variable`    TEXT DEFAULT NULL comment '附属变量json存储',
     `stat`        TINYINT(4) DEFAULT NULL comment '数据字典：状态',
     `uid`         BIGINT(20) NOT NULL comment '唯一 id，通过 uuid 生成不重复 id',
     `creator`     INT(11) DEFAULT NULL comment '创建者 id',
     `finishDate`  DATETIME DEFAULT NULL comment '任务完成时间',
     `expireDate`  DATETIME DEFAULT NULL comment '任务期望完成时间',
     `createDate`  DATETIME NOT NULL comment '创建日期',
     `updateDate`  DATETIME NOT NULL comment '修改日期'
  )
charset=utf8mb4 comment='历史任务表';  
 

 /* history 减少了 version 字段 */

CREATE TABLE `wf_task_actor` (
    `taskId`       INT(11) NOT NULL comment '任务ID',
    `actorId`      INT(11) NOT NULL comment '参与者ID'
)comment='任务参与者表';

create table `wf_task_actor_history` (
    `taskId`       INT(11) NOT NULL comment '任务ID',
    `actorId`      INT(11) NOT NULL comment '参与者ID'
)comment='历史任务参与者表';

CREATE TABLE `wf_surrogate`
  (
     `id`           INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `processName`  VARCHAR(100) comment '流程名称',
     `operator`     INT(11) comment '授权人',
     `surrogate`    INT(11) comment '代理人',
     `operatorDate` DATETIME comment '操作时间',
     `startDate`    DATETIME comment '开始时间',
     `endDate`      DATETIME comment '结束时间',
     `stat`         TINYINT(4) DEFAULT NULL comment '数据字典：状态'
  )
comment='委托代理表';  

CREATE TABLE `wf_cc_order`
  (
     `id`         INT(11) PRIMARY KEY NOT NULL auto_increment comment '主键 id，自增',
     `orderId`    INT(11) NOT NULL comment '流程实例 ID',
     `actorId`    INT(11) NOT NULL comment '参与者ID',
     `creator`    INT(11) DEFAULT NULL comment '发起人',
     `createDate` DATETIME NOT NULL comment '抄送时间',
     `finishDate` DATETIME comment '完成时间',
     `stat`       TINYINT(4) DEFAULT NULL comment '数据字典：状态'
  )
comment='抄送实例表';  