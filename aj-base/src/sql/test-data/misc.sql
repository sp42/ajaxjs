INSERT INTO `sys_datadict` (`id`, `name`, `value`, `content`, `parentId`, `type`, `sortNo`, `stat`, `uid`, `extraData`, `tenantId`, `creator`, `createDate`, `updateDate`) VALUES
	(1, '图文', '', '图文类型分类', NULL, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2021-11-06 23:56:16', '2022-03-28 15:20:20'),
	(2, '新闻', '', NULL, 1, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2021-11-06 23:57:25', '2021-11-06 23:57:25'),
	(3, '国内新闻', '', NULL, 2, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2021-11-06 23:58:11', '2021-11-06 23:58:11'),
	(4, '国际新闻', 'InterNews', NULL, 2, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2021-11-06 23:58:25', '2021-11-06 23:58:25'),
	(5, '港澳台新闻', '', NULL, 2, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2021-11-06 23:58:52', '2021-11-06 23:58:52'),
	(6, '香港新闻', '', NULL, 5, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-02-26 21:38:28', '2022-02-26 21:38:28'),
	(7, '澳门新闻', '', NULL, 5, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:20:43', '2022-03-28 15:20:43'),
	(8, '台湾新闻', '', NULL, 5, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:20:55', '2022-03-28 15:20:55'),
	(9, '广东新闻', '', NULL, 3, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:21:05', '2022-03-28 15:21:08'),
	(10, '湖南新闻', '', NULL, 3, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:21:23', '2022-03-28 15:21:23'),
	(11, '公告', '', NULL, 1, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:21:52', '2022-03-28 15:21:52'),
	(12, '美国新闻', '', NULL, 4, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:22:06', '2022-03-28 15:22:06'),
	(13, '俄罗斯新闻', '', NULL, 4, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 15:22:14', '2022-03-28 15:22:14'),
	(14, '广州新闻', '', NULL, 9, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-03-28 18:12:26', '2022-03-28 18:12:55'),
	(15, '通用数据', NULL, NULL, 0, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-04-19 12:00:07', '2022-04-19 12:00:07'),
	(17, 'Male', '男', NULL, 15, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-04-19 16:26:10', '2022-04-19 16:26:10'),
	(18, 'Female', '女', NULL, 15, NULL, NULL, NULL, NULL, NULL, 0, NULL, '2022-04-19 16:26:21', '2022-04-19 16:26:21');

INSERT INTO `sys_tag` (`id`, `tagIndex`, `name`, `desc`, `stat`, `uid`, `tenantId`, `createByUser`, `createDate`, `updateDate`) VALUES
	(1, 0, '大数据', '我的标签', NULL, NULL, 0, NULL, '2021-11-12 15:27:03', '2021-11-12 17:14:40'),
	(2, 1, '图文', NULL, NULL, NULL, 0, NULL, '2021-11-12 15:27:52', '2021-11-12 17:14:41'),
	(3, 2, 'CRM', NULL, NULL, NULL, 0, NULL, '2021-11-12 15:28:07', '2021-11-12 17:14:42');