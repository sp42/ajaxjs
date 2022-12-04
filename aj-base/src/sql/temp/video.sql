
-- Dumping structure for table ajaxjs.vod_actor
CREATE TABLE IF NOT EXISTS `vod_actor` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(90) CHARACTER SET utf8mb4 NOT NULL COMMENT '名称',
  `title` varchar(255) DEFAULT NULL COMMENT '副标题',
  `brief` varchar(255) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `source` varchar(100) DEFAULT NULL COMMENT '出处',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `sourceUrl` varchar(255) DEFAULT NULL COMMENT '源网址',
  `typeId` int(11) DEFAULT NULL COMMENT '分类id',
  `stat` tinyint(4) DEFAULT NULL COMMENT '数据字典：状态',
  `uid` bigint(20) NOT NULL COMMENT '唯一 id，通过“雪花算法”生成不重复 id',
  `createByUser` bigint(20) DEFAULT NULL COMMENT '创建者 id',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='演员';

-- Dumping data for table ajaxjs.vod_actor: ~0 rows (approximately)

-- Dumping structure for table ajaxjs.vod_clip
CREATE TABLE IF NOT EXISTS `vod_clip` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `videoId` int(11) NOT NULL COMMENT '影片 id',
  `name` varchar(90) CHARACTER SET utf8mb4 NOT NULL COMMENT '文件名',
  `content` longtext COMMENT '内容',
  `order` tinyint(2) DEFAULT NULL COMMENT '顺序',
  `length` tinyint(4) DEFAULT NULL COMMENT '时长',
  `source` varchar(100) DEFAULT NULL COMMENT '出处',
  `encodeType` tinyint(2) DEFAULT NULL COMMENT '视频压缩算法',
  `isOpz` tinyint(1) DEFAULT NULL COMMENT '是否已优化（H256）',
  `stat` tinyint(4) DEFAULT NULL COMMENT '数据字典：状态',
  `uid` bigint(20) NOT NULL COMMENT '唯一 id，通过“雪花算法”生成不重复 id',
  `createByUser` bigint(20) DEFAULT NULL COMMENT '创建者 id',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='影片视频文件';

-- Dumping data for table ajaxjs.vod_clip: ~0 rows (approximately)

-- Dumping structure for table ajaxjs.vod_video
CREATE TABLE IF NOT EXISTS `vod_video` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键 id，自增',
  `name` varchar(90) CHARACTER SET utf8mb4 NOT NULL COMMENT '名称',
  `title` varchar(255) DEFAULT NULL COMMENT '副标题',
  `brief` varchar(255) DEFAULT NULL COMMENT '简介',
  `content` longtext COMMENT '内容',
  `author` varchar(100) DEFAULT NULL COMMENT '作者',
  `source` varchar(100) DEFAULT NULL COMMENT '出处',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `sourceUrl` varchar(255) DEFAULT NULL COMMENT '源网址',
  `typeId` int(11) DEFAULT NULL COMMENT '分类id',
  `stat` tinyint(4) DEFAULT NULL COMMENT '数据字典：状态',
  `uid` bigint(20) NOT NULL COMMENT '唯一 id，通过“雪花算法”生成不重复 id',
  `createByUser` bigint(20) DEFAULT NULL COMMENT '创建者 id',
  `createDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `updateDate` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='影片';

-- Dumping data for table ajaxjs.vod_video: ~0 rows (approximately)