INSERT INTO `auth_access_token` (`id`, `access_token`, `user_id`, `user_name`, `client_id`, `expires_in`, `grant_type`, `scope`, `create_date`) VALUES
	(1, '1.e74f975492bf580ff3f696d86d773ea9083afcfc.2592000.1653644731', 1, 'admin', 1, 1653644731, 'authorization_code', 'basic', '2021-12-21 21:10:53'),
	(3, '1.49c95a9f19aa00a6278af8f31d450a013d3fd561.2592000.1653755008', 1, 'admin', 1, 1653755008, 'authorization_code', 'DEFAULT_SCOPE', '2022-04-27 22:15:40');


INSERT INTO `user_role` (`id`, `name`, `content`, `parent_id`, `tenant_id`, `stat`, `create_sum`, `read_sum`, `update_sum`, `delete_sum`, `creator`, `create_date`, `update_date`) VALUES
	(1, '超级管理员', '拥有最高权限', 0, 0, NULL, 10, 20, NULL, NULL, NULL, '2022-04-18 21:36:24', '2022-04-18 21:40:03'),
	(2, '技术人员', NULL, 0, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:23:05', '2022-04-19 11:23:05'),
	(3, '开发者', NULL, 2, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:23:13', '2022-04-19 11:23:13'),
	(4, '测试者', NULL, 2, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:23:23', '2022-04-19 11:23:23'),
	(5, '运维人员', NULL, 2, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:23:39', '2022-04-19 11:23:39'),
	(6, '用户', '外部用户', 0, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:24:11', '2022-04-19 11:24:11'),
	(7, '一般用户', NULL, 6, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:24:22', '2022-04-19 11:24:22'),
	(8, 'VIP 用户', NULL, 6, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:24:30', '2022-04-19 11:24:30'),
	(9, '后台管理用户', NULL, 0, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:24:55', '2022-04-19 11:24:55'),
	(10, '运营商', NULL, 9, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:25:02', '2022-04-19 11:25:02'),
	(11, '厂家', NULL, 9, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-19 11:25:11', '2022-04-19 11:25:11'),
	(12, 'jlkj', 'lkjk88659808', 11, 0, NULL, 0, 0, 0, 0, NULL, '2022-04-25 16:53:39', '2022-04-25 16:53:39');


-- Dumping data for table ajaxjs.auth_client_details: ~3 rows (approximately)
INSERT INTO `auth_client_details` (`id`, `clientId`, `name`, `content`, `client_secret`, `redirec_uri`, `stat`, `uid`, `creator_id`, `create_date`, `update_date`, `extend`, `tenant_d`) VALUES
	(1, 'C2Oj5hKcwMmgxiKygwquLCSN', '测试客户端', NULL, '1PtTvjmAvy2zSUZISdjeKBFJTgZM43BZ', NULL, NULL, NULL, NULL, '2022-04-21 10:10:21', '2022-04-21 10:10:21', NULL, NULL),
	(2, 'fyTB6kxkz6IsXVhovYnrzjVb', '测试客户端', NULL, 'LK4XwXML1mOPYAp6aFDoxSO4SqgwhUGx', NULL, NULL, NULL, NULL, '2022-04-21 10:20:07', '2022-04-21 10:20:07', NULL, NULL),
	(3, 'CsVT2c5qgA2RsP5frMjWlZHJ', '测试客户端', NULL, '2kycQyHSeW48UYTDlbqta7oyeiY4LPQC', NULL, NULL, NULL, NULL, '2022-04-21 10:20:43', '2022-04-21 10:20:43', NULL, NULL);


-- Dumping data for table ajaxjs.auth_refresh_token: ~2 rows (approximately)
INSERT INTO `auth_refresh_token` (`id`, `refresh_token`, `token_id`, `expires_in`, `creator`, `create_date`, `update_date`) VALUES
	(1, '2.84940a8c0594a5a0658b6b9776213fde8cc446df.31536000.1682588732', 1, 1682588732, NULL, '2022-04-21 14:07:07', '2022-04-27 17:45:42'),
	(2, '2.70b7e4b6a71f38e90bc4eca08f5b6c158053838f.31536000.1682699008', 3, 1682699008, NULL, '2022-04-27 22:15:41', '2022-04-29 00:23:29');

-- Dumping data for table ajaxjs.user_auth: ~3 rows (approximately)
INSERT INTO `user_auth` (`id`, `userId`, `loginType`, `registerType`, `registerIp`, `identifier`, `credential`, `email_verified`, `phone_verified`, `stat`, `uid`, `createByUser`, `createDate`, `updateDate`, `updatePasswordDate`) VALUES
	(1, 303, 3, 0, NULL, 'oOXb75CZBkmY6Ke4tsHll1SngJwU', '746a2c5f0ee6c4b47d0311d1b5b853f08698e67c', NULL, NULL, NULL, NULL, NULL, '2022-04-06 16:22:44', '2022-04-21 18:16:47', NULL),
	(2, 304, 3, 0, NULL, 'oOXb75JJsiOZhAUjPfErHZjItHTw', NULL, NULL, NULL, NULL, NULL, NULL, '2022-04-06 22:59:37', '2022-04-06 22:59:37', NULL),
	(3, 1, 1, 0, NULL, NULL, 'dc072d175b22de7548501b31785cf97e76fe58d5', NULL, NULL, NULL, NULL, NULL, '2022-04-10 00:48:49', '2022-04-27 15:06:13', NULL);



