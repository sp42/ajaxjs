/*
Navicat MySQL Data Transfer

Source Server         : aa
Source Server Version : 50067
Source Host           : localhost:3306
Source Database       : snakerdb

Target Server Type    : MYSQL
Target Server Version : 50067
File Encoding         : 65001

Date: 2015-03-07 15:31:02
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `conf_dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `conf_dictionary`;
CREATE TABLE `conf_dictionary` (
  `id` int(11) NOT NULL auto_increment,
  `cnName` varchar(200) NOT NULL,
  `description` varchar(500) default NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of conf_dictionary
-- ----------------------------
INSERT INTO `conf_dictionary` VALUES ('1', '是否', '', 'yesNo');
INSERT INTO `conf_dictionary` VALUES ('2', '性别', '', 'sex');
INSERT INTO `conf_dictionary` VALUES ('3', '表单类型', '', 'formType');
INSERT INTO `conf_dictionary` VALUES ('4', '字段类型', '', 'fieldType');
INSERT INTO `conf_dictionary` VALUES ('5', '默认字段值', '', 'fieldDefault');

-- ----------------------------
-- Table structure for `conf_dictitem`
-- ----------------------------
DROP TABLE IF EXISTS `conf_dictitem`;
CREATE TABLE `conf_dictitem` (
  `id` int(11) NOT NULL auto_increment,
  `code` varchar(50) default NULL,
  `description` varchar(500) default NULL,
  `name` varchar(200) NOT NULL,
  `orderby` int(11) default NULL,
  `dictionary` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK_DICTITEM_DICTIONARY` (`dictionary`),
  CONSTRAINT `FK_DICTITEM_DICTIONARY` FOREIGN KEY (`dictionary`) REFERENCES `conf_dictionary` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of conf_dictitem
-- ----------------------------
INSERT INTO `conf_dictitem` VALUES ('11', '1', '', '是', '1', '1');
INSERT INTO `conf_dictitem` VALUES ('12', '2', '', '否', '2', '1');
INSERT INTO `conf_dictitem` VALUES ('21', '1', '', '男', '1', '2');
INSERT INTO `conf_dictitem` VALUES ('22', '2', '', '女', '2', '2');
INSERT INTO `conf_dictitem` VALUES ('31', '1', '', '公文管理', '1', '3');
INSERT INTO `conf_dictitem` VALUES ('32', '2', '', '案例管理', '2', '3');
INSERT INTO `conf_dictitem` VALUES ('41', '1', '', '字符', '1', '4');
INSERT INTO `conf_dictitem` VALUES ('42', '2', '', '整数', '2', '4');
INSERT INTO `conf_dictitem` VALUES ('43', '3', '', '小数', '3', '4');
INSERT INTO `conf_dictitem` VALUES ('44', '4', '', '日期', '4', '4');
INSERT INTO `conf_dictitem` VALUES ('45', '5', '', '文本', '5', '4');
INSERT INTO `conf_dictitem` VALUES ('51', '1', '', '当前用户Id', '1', '5');
INSERT INTO `conf_dictitem` VALUES ('52', '2', '', '当前用户名称', '2', '5');
INSERT INTO `conf_dictitem` VALUES ('53', '3', '', '当前用户部门Id', '3', '5');
INSERT INTO `conf_dictitem` VALUES ('54', '4', '', '当前用户部门名称', '4', '5');
INSERT INTO `conf_dictitem` VALUES ('55', '5', '', '当前日期yyyy-MM-dd', '5', '5');
INSERT INTO `conf_dictitem` VALUES ('56', '6', '', '当前时间yyyy-MM-dd HH:mm:ss', '6', '5');
INSERT INTO `conf_dictitem` VALUES ('57', '7', '', '当前年月yyyy-MM', '7', '5');

-- ----------------------------
-- Table structure for `df_field`
-- ----------------------------
DROP TABLE IF EXISTS `df_field`;
CREATE TABLE `df_field` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(200) NOT NULL,
  `plugins` varchar(200) default NULL,
  `title` varchar(50) default NULL,
  `type` varchar(50) default NULL,
  `flow` varchar(10) default NULL,
  `tableName` varchar(50) default NULL,
  `formId` int(11) NOT NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of df_field
-- ----------------------------
INSERT INTO `df_field` VALUES ('1', 'xm', 'text', '姓名', 'text', '0', 'TBL_myform1', '1');
INSERT INTO `df_field` VALUES ('2', 'age', 'text', '年龄', 'text', '0', 'TBL_myform1', '1');

-- ----------------------------
-- Table structure for `df_form`
-- ----------------------------
DROP TABLE IF EXISTS `df_form`;
CREATE TABLE `df_form` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(200) NOT NULL,
  `displayName` varchar(200) default NULL,
  `type` varchar(50) default NULL,
  `creator` varchar(50) default NULL,
  `createTime` varchar(50) default NULL,
  `originalHtml` text,
  `parseHtml` text,
  `fieldNum` int(11) default '0',
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of df_form
-- ----------------------------
INSERT INTO `df_form` VALUES ('1', 'myform1', '案例表单1', '2', 'admin', '2014-12-28 17:50:14', '<p><input orgtype=\"text\" orgwidth=\"150\" orgalign=\"left\" style=\"text-align: left; width: 150px;\" fieldflow=\"0\" orghide=\"0\" plugins=\"text\" fieldname=\"xm\" value=\"\" title=\"姓名\" name=\"xm\" type=\"text\"/><input orgtype=\"text\" orgwidth=\"150\" orgalign=\"left\" style=\"text-align: left; width: 150px;\" fieldflow=\"0\" orghide=\"0\" plugins=\"text\" fieldname=\"age\" value=\"\" title=\"年龄\" name=\"age\" type=\"text\"/></p>', '<p><input orgtype=\"text\" orgwidth=\"150\" orgalign=\"left\" style=\"text-align: left; width: 150px;\" fieldflow=\"0\" orghide=\"0\" plugins=\"text\" fieldname=\"xm\" value=\"\" title=\"姓名\" name=\"xm\" type=\"text\"/><input orgtype=\"text\" orgwidth=\"150\" orgalign=\"left\" style=\"text-align: left; width: 150px;\" fieldflow=\"0\" orghide=\"0\" plugins=\"text\" fieldname=\"age\" value=\"\" title=\"年龄\" name=\"age\" type=\"text\"/></p>', '2');

-- ----------------------------
-- Table structure for `flow_approval`
-- ----------------------------
DROP TABLE IF EXISTS `flow_approval`;
CREATE TABLE `flow_approval` (
  `id` int(11) NOT NULL auto_increment,
  `operator` varchar(50) NOT NULL,
  `operateTime` varchar(50) default NULL,
  `result` varchar(50) default NULL,
  `description` varchar(500) default NULL,
  `orderId` varchar(50) default NULL,
  `taskId` varchar(50) default NULL,
  `taskName` varchar(100) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flow_approval
-- ----------------------------

-- ----------------------------
-- Table structure for `flow_borrow`
-- ----------------------------
DROP TABLE IF EXISTS `flow_borrow`;
CREATE TABLE `flow_borrow` (
  `id` int(11) NOT NULL auto_increment,
  `operator` varchar(50) NOT NULL,
  `description` varchar(500) default NULL,
  `amount` double default NULL,
  `operateTime` varchar(50) default NULL,
  `repaymentDate` varchar(50) default NULL,
  `orderId` varchar(50) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of flow_borrow
-- ----------------------------

-- ----------------------------
-- Table structure for `sec_authority`
-- ----------------------------
DROP TABLE IF EXISTS `sec_authority`;
CREATE TABLE `sec_authority` (
  `id` int(11) NOT NULL auto_increment,
  `description` varchar(500) default NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_authority
-- ----------------------------
INSERT INTO `sec_authority` VALUES ('101', '待办任务', 'ACTIVETASK');
INSERT INTO `sec_authority` VALUES ('102', '流程实例', 'ORDER');
INSERT INTO `sec_authority` VALUES ('103', '历史任务', 'HISTORYTASK');
INSERT INTO `sec_authority` VALUES ('104', '流程定义', 'PROCESSLIST');
INSERT INTO `sec_authority` VALUES ('105', '流程部署', 'PROCESSDEPLOY');
INSERT INTO `sec_authority` VALUES ('106', '委托授权', 'SURROGATE');
INSERT INTO `sec_authority` VALUES ('301', '字典查询', 'DICTLIST');
INSERT INTO `sec_authority` VALUES ('302', '字典查看', 'DICTVIEW');
INSERT INTO `sec_authority` VALUES ('303', '字典编辑', 'DICTEDIT');
INSERT INTO `sec_authority` VALUES ('304', '字典删除', 'DICTDELETE');
INSERT INTO `sec_authority` VALUES ('311', '表单查询', 'FORMTLIST');
INSERT INTO `sec_authority` VALUES ('312', '表单查看', 'FORMVIEW');
INSERT INTO `sec_authority` VALUES ('313', '表单编辑', 'FORMEDIT');
INSERT INTO `sec_authority` VALUES ('314', '表单删除', 'FORMDELETE');
INSERT INTO `sec_authority` VALUES ('401', '用户查询', 'USERLIST');
INSERT INTO `sec_authority` VALUES ('402', '部门查询', 'ORGLIST');
INSERT INTO `sec_authority` VALUES ('403', '角色查询', 'ROLELIST');
INSERT INTO `sec_authority` VALUES ('404', '权限查询', 'AUTHORITYLIST');
INSERT INTO `sec_authority` VALUES ('405', '资源查询', 'RESOURCELIST');
INSERT INTO `sec_authority` VALUES ('406', '菜单查询', 'MENULIST');
INSERT INTO `sec_authority` VALUES ('411', '用户查看', 'USERVIEW');
INSERT INTO `sec_authority` VALUES ('412', '部门查看', 'ORGVIEW');
INSERT INTO `sec_authority` VALUES ('413', '角色查看', 'ROLEVIEW');
INSERT INTO `sec_authority` VALUES ('414', '权限查看', 'AUTHORITYVIEW');
INSERT INTO `sec_authority` VALUES ('415', '资源查看', 'RESOURCEVIEW');
INSERT INTO `sec_authority` VALUES ('416', '菜单查看', 'MENUVIEW');
INSERT INTO `sec_authority` VALUES ('421', '用户删除', 'USERDELETE');
INSERT INTO `sec_authority` VALUES ('422', '部门删除', 'ORGDELETE');
INSERT INTO `sec_authority` VALUES ('423', '角色删除', 'ROLEDELETE');
INSERT INTO `sec_authority` VALUES ('424', '权限删除', 'AUTHORITYDELETE');
INSERT INTO `sec_authority` VALUES ('425', '资源删除', 'RESOURCEDELETE');
INSERT INTO `sec_authority` VALUES ('426', '菜单删除', 'MENUDELETE');
INSERT INTO `sec_authority` VALUES ('431', '用户编辑', 'USEREDIT');
INSERT INTO `sec_authority` VALUES ('432', '部门编辑', 'ORGEDIT');
INSERT INTO `sec_authority` VALUES ('433', '角色编辑', 'ROLEEDIT');
INSERT INTO `sec_authority` VALUES ('434', '权限编辑', 'AUTHORITYEDIT');
INSERT INTO `sec_authority` VALUES ('435', '资源编辑', 'RESOURCEEDIT');
INSERT INTO `sec_authority` VALUES ('436', '菜单编辑', 'MENUEDIT');

-- ----------------------------
-- Table structure for `sec_authority_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sec_authority_resource`;
CREATE TABLE `sec_authority_resource` (
  `authority_id` int(11) NOT NULL,
  `resource_id` int(11) NOT NULL,
  KEY `FK_AUTHORITY_RESOURCE1` (`authority_id`),
  KEY `FK_AUTHORITY_RESOURCE2` (`resource_id`),
  CONSTRAINT `FK_AUTHORITY_RESOURCE1` FOREIGN KEY (`authority_id`) REFERENCES `sec_authority` (`id`),
  CONSTRAINT `FK_AUTHORITY_RESOURCE2` FOREIGN KEY (`resource_id`) REFERENCES `sec_resource` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_authority_resource
-- ----------------------------
INSERT INTO `sec_authority_resource` VALUES ('101', '101');
INSERT INTO `sec_authority_resource` VALUES ('102', '102');
INSERT INTO `sec_authority_resource` VALUES ('103', '103');
INSERT INTO `sec_authority_resource` VALUES ('104', '104');
INSERT INTO `sec_authority_resource` VALUES ('105', '105');
INSERT INTO `sec_authority_resource` VALUES ('106', '106');
INSERT INTO `sec_authority_resource` VALUES ('301', '301');
INSERT INTO `sec_authority_resource` VALUES ('302', '302');
INSERT INTO `sec_authority_resource` VALUES ('303', '303');
INSERT INTO `sec_authority_resource` VALUES ('304', '304');
INSERT INTO `sec_authority_resource` VALUES ('311', '311');
INSERT INTO `sec_authority_resource` VALUES ('312', '312');
INSERT INTO `sec_authority_resource` VALUES ('313', '313');
INSERT INTO `sec_authority_resource` VALUES ('314', '314');
INSERT INTO `sec_authority_resource` VALUES ('401', '401');
INSERT INTO `sec_authority_resource` VALUES ('402', '402');
INSERT INTO `sec_authority_resource` VALUES ('403', '403');
INSERT INTO `sec_authority_resource` VALUES ('404', '404');
INSERT INTO `sec_authority_resource` VALUES ('405', '405');
INSERT INTO `sec_authority_resource` VALUES ('406', '406');
INSERT INTO `sec_authority_resource` VALUES ('411', '411');
INSERT INTO `sec_authority_resource` VALUES ('412', '412');
INSERT INTO `sec_authority_resource` VALUES ('413', '413');
INSERT INTO `sec_authority_resource` VALUES ('414', '414');
INSERT INTO `sec_authority_resource` VALUES ('415', '415');
INSERT INTO `sec_authority_resource` VALUES ('416', '416');
INSERT INTO `sec_authority_resource` VALUES ('421', '421');
INSERT INTO `sec_authority_resource` VALUES ('422', '422');
INSERT INTO `sec_authority_resource` VALUES ('423', '423');
INSERT INTO `sec_authority_resource` VALUES ('424', '424');
INSERT INTO `sec_authority_resource` VALUES ('425', '425');
INSERT INTO `sec_authority_resource` VALUES ('426', '426');
INSERT INTO `sec_authority_resource` VALUES ('431', '431');
INSERT INTO `sec_authority_resource` VALUES ('432', '432');
INSERT INTO `sec_authority_resource` VALUES ('433', '433');
INSERT INTO `sec_authority_resource` VALUES ('434', '434');
INSERT INTO `sec_authority_resource` VALUES ('435', '435');
INSERT INTO `sec_authority_resource` VALUES ('436', '436');

-- ----------------------------
-- Table structure for `sec_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sec_menu`;
CREATE TABLE `sec_menu` (
  `id` int(11) NOT NULL auto_increment,
  `description` varchar(500) default NULL,
  `name` varchar(200) NOT NULL,
  `parent_menu` int(11) default NULL,
  `orderby` int(11) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_menu
-- ----------------------------
INSERT INTO `sec_menu` VALUES ('1', '', '流程管理', '0', '1');
INSERT INTO `sec_menu` VALUES ('2', '', '功能模块', '0', '2');
INSERT INTO `sec_menu` VALUES ('3', '', '配置管理', '0', '3');
INSERT INTO `sec_menu` VALUES ('4', '', '系统管理', '0', '4');
INSERT INTO `sec_menu` VALUES ('11', '', '待办任务', '1', '1');
INSERT INTO `sec_menu` VALUES ('12', '', '流程实例', '1', '2');
INSERT INTO `sec_menu` VALUES ('13', '', '历史任务', '1', '3');
INSERT INTO `sec_menu` VALUES ('14', '', '流程定义', '1', '4');
INSERT INTO `sec_menu` VALUES ('15', '', '委托授权', '1', '5');
INSERT INTO `sec_menu` VALUES ('31', '', '数据字典', '3', '1');
INSERT INTO `sec_menu` VALUES ('32', '', '表单管理', '3', '2');
INSERT INTO `sec_menu` VALUES ('41', '', '用户管理', '4', '1');
INSERT INTO `sec_menu` VALUES ('42', '', '部门管理', '4', '2');
INSERT INTO `sec_menu` VALUES ('43', '', '角色管理', '4', '3');
INSERT INTO `sec_menu` VALUES ('44', '', '权限管理', '4', '4');
INSERT INTO `sec_menu` VALUES ('45', '', '资源管理', '4', '5');
INSERT INTO `sec_menu` VALUES ('46', '', '菜单管理', '4', '6');

-- ----------------------------
-- Table structure for `sec_org`
-- ----------------------------
DROP TABLE IF EXISTS `sec_org`;
CREATE TABLE `sec_org` (
  `id` int(11) NOT NULL auto_increment,
  `active` varchar(255) default NULL,
  `description` varchar(500) default NULL,
  `fullname` varchar(200) default NULL,
  `name` varchar(200) NOT NULL,
  `type` varchar(200) default NULL,
  `parent_org` int(11) default NULL,
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200205 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_org
-- ----------------------------
INSERT INTO `sec_org` VALUES ('200', null, 'a', null, 'XXX有限公司', null, '0');
INSERT INTO `sec_org` VALUES ('2000', null, 'a', null, '行政部', null, '200');
INSERT INTO `sec_org` VALUES ('2001', null, 'a', null, '市场部', null, '200');
INSERT INTO `sec_org` VALUES ('2002', null, 'a', null, '研发部', null, '200');
INSERT INTO `sec_org` VALUES ('2003', null, 'a', null, '客服部', null, '200');
INSERT INTO `sec_org` VALUES ('200200', null, 'a', null, '测试组', null, '2002');
INSERT INTO `sec_org` VALUES ('200201', null, 'a', null, '质量组', null, '2002');
INSERT INTO `sec_org` VALUES ('200202', null, 'a', null, '研发组', null, '2002');
INSERT INTO `sec_org` VALUES ('200203', null, 'a', null, '需求组', null, '2002');
INSERT INTO `sec_org` VALUES ('200204', null, 'a啊啊啊', null, 'UI组', null, '2002');

-- ----------------------------
-- Table structure for `sec_resource`
-- ----------------------------
DROP TABLE IF EXISTS `sec_resource`;
CREATE TABLE `sec_resource` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(200) NOT NULL,
  `source` varchar(200) default NULL,
  `menu` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FK_RESOURCE_MENU` (`menu`),
  CONSTRAINT `FK_RESOURCE_MENU` FOREIGN KEY (`menu`) REFERENCES `sec_menu` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=437 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_resource
-- ----------------------------
INSERT INTO `sec_resource` VALUES ('101', '待办任务', '/snaker/task/active', '11');
INSERT INTO `sec_resource` VALUES ('102', '流程实例', '/snaker/flow/order', '12');
INSERT INTO `sec_resource` VALUES ('103', '历史任务', '/snaker/task/history', '13');
INSERT INTO `sec_resource` VALUES ('104', '流程定义', '/snaker/process/list', '14');
INSERT INTO `sec_resource` VALUES ('105', '流程部署', '/snaker/process/deploy/**;/snaker/process/add/**', null);
INSERT INTO `sec_resource` VALUES ('106', '委托授权', '/snaker/surrogate/list', '15');
INSERT INTO `sec_resource` VALUES ('301', '字典查询', '/config/dictionary', '31');
INSERT INTO `sec_resource` VALUES ('302', '字典查看', '/config/dictionary/view/**', null);
INSERT INTO `sec_resource` VALUES ('303', '字典编辑', '/config/dictionary/update/**;/config/dictionary/save/**;/config/dictionary/add/**;/config/dictionary/edit/**', null);
INSERT INTO `sec_resource` VALUES ('304', '字典删除', '/config/dictionary/delete/**', null);
INSERT INTO `sec_resource` VALUES ('311', '表单查询', '/config/form', '32');
INSERT INTO `sec_resource` VALUES ('312', '表单查看', '/config/form/view/**', null);
INSERT INTO `sec_resource` VALUES ('313', '表单编辑', '/config/form/update/**;/config/form/save/**;/config/form/add/**;/config/form/edit/**', null);
INSERT INTO `sec_resource` VALUES ('314', '表单删除', '/config/form/delete/**', null);
INSERT INTO `sec_resource` VALUES ('401', '用户查询', '/security/user', '41');
INSERT INTO `sec_resource` VALUES ('402', '部门查询', '/security/org', '42');
INSERT INTO `sec_resource` VALUES ('403', '角色查询', '/security/role', '43');
INSERT INTO `sec_resource` VALUES ('404', '权限查询', '/security/authority', '44');
INSERT INTO `sec_resource` VALUES ('405', '资源查询', '/security/resource', '45');
INSERT INTO `sec_resource` VALUES ('406', '菜单查询', '/security/menu', '46');
INSERT INTO `sec_resource` VALUES ('411', '用户查看', '/security/user/view/**', null);
INSERT INTO `sec_resource` VALUES ('412', '部门查看', '/security/org/view/**', null);
INSERT INTO `sec_resource` VALUES ('413', '角色查看', '/security/role/view/**', null);
INSERT INTO `sec_resource` VALUES ('414', '权限查看', '/security/authority/view/**', null);
INSERT INTO `sec_resource` VALUES ('415', '资源查看', '/security/resource/view/**', null);
INSERT INTO `sec_resource` VALUES ('416', '菜单查看', '/security/menu/view/**', null);
INSERT INTO `sec_resource` VALUES ('421', '用户删除', '/security/user/delete/**', null);
INSERT INTO `sec_resource` VALUES ('422', '部门删除', '/security/org/delete/**', null);
INSERT INTO `sec_resource` VALUES ('423', '角色删除', '/security/role/delete/**', null);
INSERT INTO `sec_resource` VALUES ('424', '权限删除', '/security/authority/delete/**', null);
INSERT INTO `sec_resource` VALUES ('425', '资源删除', '/security/resource/delete/**', null);
INSERT INTO `sec_resource` VALUES ('426', '菜单删除', '/security/menu/delete/**', null);
INSERT INTO `sec_resource` VALUES ('431', '用户编辑', '/security/user/update/**;/security/user/save/**;/security/user/add/**;/security/user/edit/**', null);
INSERT INTO `sec_resource` VALUES ('432', '部门编辑', '/security/org/update/**;/security/org/save/**;/security/org/add/**;/security/org/edit/**', null);
INSERT INTO `sec_resource` VALUES ('433', '角色编辑', '/security/role/update/**;/security/role/save/**;/security/role/add/**;/security/role/edit/**', null);
INSERT INTO `sec_resource` VALUES ('434', '权限编辑', '/security/authority/update/**;/security/authority/save/**;/security/authority/add/**;/security/authority/edit/**', null);
INSERT INTO `sec_resource` VALUES ('435', '资源编辑', '/security/resource/update/**;/security/resource/save/**;/security/resource/add/**;/security/resource/edit/**', null);
INSERT INTO `sec_resource` VALUES ('436', '菜单编辑', '/security/menu/update/**;/security/menu/save/**;/security/menu/add/**;/security/menu/edit/**', null);

-- ----------------------------
-- Table structure for `sec_role`
-- ----------------------------
DROP TABLE IF EXISTS `sec_role`;
CREATE TABLE `sec_role` (
  `id` int(11) NOT NULL auto_increment,
  `description` varchar(500) default NULL,
  `name` varchar(200) NOT NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_role
-- ----------------------------
INSERT INTO `sec_role` VALUES ('1', '系统管理员', 'Admin');
INSERT INTO `sec_role` VALUES ('2', '普通用户', 'General');

-- ----------------------------
-- Table structure for `sec_role_authority`
-- ----------------------------
DROP TABLE IF EXISTS `sec_role_authority`;
CREATE TABLE `sec_role_authority` (
  `role_id` int(11) NOT NULL,
  `authority_id` int(11) NOT NULL,
  KEY `FK_ROLE_AUTHORITY1` (`authority_id`),
  KEY `FK_ROLE_AUTHORITY2` (`role_id`),
  CONSTRAINT `FK_ROLE_AUTHORITY1` FOREIGN KEY (`authority_id`) REFERENCES `sec_authority` (`id`),
  CONSTRAINT `FK_ROLE_AUTHORITY2` FOREIGN KEY (`role_id`) REFERENCES `sec_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_role_authority
-- ----------------------------
INSERT INTO `sec_role_authority` VALUES ('1', '101');
INSERT INTO `sec_role_authority` VALUES ('1', '102');
INSERT INTO `sec_role_authority` VALUES ('1', '103');
INSERT INTO `sec_role_authority` VALUES ('1', '104');
INSERT INTO `sec_role_authority` VALUES ('1', '105');
INSERT INTO `sec_role_authority` VALUES ('1', '106');
INSERT INTO `sec_role_authority` VALUES ('1', '301');
INSERT INTO `sec_role_authority` VALUES ('1', '302');
INSERT INTO `sec_role_authority` VALUES ('1', '303');
INSERT INTO `sec_role_authority` VALUES ('1', '304');
INSERT INTO `sec_role_authority` VALUES ('1', '311');
INSERT INTO `sec_role_authority` VALUES ('1', '312');
INSERT INTO `sec_role_authority` VALUES ('1', '313');
INSERT INTO `sec_role_authority` VALUES ('1', '314');
INSERT INTO `sec_role_authority` VALUES ('1', '401');
INSERT INTO `sec_role_authority` VALUES ('1', '402');
INSERT INTO `sec_role_authority` VALUES ('1', '403');
INSERT INTO `sec_role_authority` VALUES ('1', '404');
INSERT INTO `sec_role_authority` VALUES ('1', '405');
INSERT INTO `sec_role_authority` VALUES ('1', '406');
INSERT INTO `sec_role_authority` VALUES ('1', '411');
INSERT INTO `sec_role_authority` VALUES ('1', '412');
INSERT INTO `sec_role_authority` VALUES ('1', '413');
INSERT INTO `sec_role_authority` VALUES ('1', '414');
INSERT INTO `sec_role_authority` VALUES ('1', '415');
INSERT INTO `sec_role_authority` VALUES ('1', '416');
INSERT INTO `sec_role_authority` VALUES ('1', '421');
INSERT INTO `sec_role_authority` VALUES ('1', '422');
INSERT INTO `sec_role_authority` VALUES ('1', '423');
INSERT INTO `sec_role_authority` VALUES ('1', '424');
INSERT INTO `sec_role_authority` VALUES ('1', '425');
INSERT INTO `sec_role_authority` VALUES ('1', '426');
INSERT INTO `sec_role_authority` VALUES ('1', '431');
INSERT INTO `sec_role_authority` VALUES ('1', '432');
INSERT INTO `sec_role_authority` VALUES ('1', '433');
INSERT INTO `sec_role_authority` VALUES ('1', '434');
INSERT INTO `sec_role_authority` VALUES ('1', '435');
INSERT INTO `sec_role_authority` VALUES ('1', '436');
INSERT INTO `sec_role_authority` VALUES ('2', '101');
INSERT INTO `sec_role_authority` VALUES ('2', '102');
INSERT INTO `sec_role_authority` VALUES ('2', '103');
INSERT INTO `sec_role_authority` VALUES ('2', '104');
INSERT INTO `sec_role_authority` VALUES ('2', '106');
INSERT INTO `sec_role_authority` VALUES ('2', '301');
INSERT INTO `sec_role_authority` VALUES ('2', '302');
INSERT INTO `sec_role_authority` VALUES ('2', '401');
INSERT INTO `sec_role_authority` VALUES ('2', '402');
INSERT INTO `sec_role_authority` VALUES ('2', '403');
INSERT INTO `sec_role_authority` VALUES ('2', '404');
INSERT INTO `sec_role_authority` VALUES ('2', '405');
INSERT INTO `sec_role_authority` VALUES ('2', '406');
INSERT INTO `sec_role_authority` VALUES ('2', '411');
INSERT INTO `sec_role_authority` VALUES ('2', '412');
INSERT INTO `sec_role_authority` VALUES ('2', '413');
INSERT INTO `sec_role_authority` VALUES ('2', '414');
INSERT INTO `sec_role_authority` VALUES ('2', '415');
INSERT INTO `sec_role_authority` VALUES ('2', '416');

-- ----------------------------
-- Table structure for `sec_role_user`
-- ----------------------------
DROP TABLE IF EXISTS `sec_role_user`;
CREATE TABLE `sec_role_user` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL,
  KEY `FK_ROLE_USER1` (`user_id`),
  KEY `FK_ROLE_USER2` (`role_id`),
  CONSTRAINT `FK_ROLE_USER1` FOREIGN KEY (`user_id`) REFERENCES `sec_user` (`id`),
  CONSTRAINT `FK_ROLE_USER2` FOREIGN KEY (`role_id`) REFERENCES `sec_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_role_user
-- ----------------------------
INSERT INTO `sec_role_user` VALUES ('1', '1');
INSERT INTO `sec_role_user` VALUES ('2', '2');
INSERT INTO `sec_role_user` VALUES ('3', '2');
INSERT INTO `sec_role_user` VALUES ('6', '1');
INSERT INTO `sec_role_user` VALUES ('6', '2');
INSERT INTO `sec_role_user` VALUES ('5', '1');
INSERT INTO `sec_role_user` VALUES ('5', '2');
INSERT INTO `sec_role_user` VALUES ('4', '1');
INSERT INTO `sec_role_user` VALUES ('4', '2');

-- ----------------------------
-- Table structure for `sec_user`
-- ----------------------------
DROP TABLE IF EXISTS `sec_user`;
CREATE TABLE `sec_user` (
  `id` int(11) NOT NULL auto_increment,
  `address` varchar(200) default NULL,
  `age` int(11) default NULL,
  `email` varchar(100) default NULL,
  `enabled` varchar(255) default NULL,
  `fullname` varchar(100) default NULL,
  `password` varchar(50) default NULL,
  `plainPassword` varchar(50) default NULL,
  `salt` varchar(255) default NULL,
  `sex` varchar(255) default NULL,
  `type` int(11) default NULL,
  `username` varchar(50) NOT NULL,
  `org` int(11) default NULL,
  PRIMARY KEY  (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `FK_USER_ORG` (`org`),
  CONSTRAINT `FK_USER_ORG` FOREIGN KEY (`org`) REFERENCES `sec_org` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_user
-- ----------------------------
INSERT INTO `sec_user` VALUES ('1', '', null, '', '1', '系统管理员', 'f9e1a0299c2570eb5942fbbda0b2a8ceb2ef9769', null, 'e97e0cea2389225f', '1', null, 'admin', '200');
INSERT INTO `sec_user` VALUES ('2', '', null, '', '1', 'test', 'f9e1a0299c2570eb5942fbbda0b2a8ceb2ef9769', null, 'e97e0cea2389225f', '1', null, 'test', '200200');
INSERT INTO `sec_user` VALUES ('3', '', null, '', '1', 'snaker', 'f9e1a0299c2570eb5942fbbda0b2a8ceb2ef9769', null, 'e97e0cea2389225f', '1', null, 'snaker', '200202');
INSERT INTO `sec_user` VALUES ('4', null, null, 'test2@163.com', '1', 'test2', '282407ec8f9c106cf1fad607a29e13e2cffa9349', '123', 'f12cff8ca367c300', '1', null, 'test2', '200200');
INSERT INTO `sec_user` VALUES ('5', null, null, 'test3@163.com', '1', 'test3', 'fbe23333742c048dc6f4236544c2af69394e1cd8', '123456', '68bb658fac2d14fa', '2', null, 'test3', '200202');
INSERT INTO `sec_user` VALUES ('6', null, null, 'test4@163.com', '1', 'test4', '164330e65002d7e4592d6f7c08a74fc7073aedd0', '123456', 'fdcebaca55bbbcb4', '1', null, 'test4', '200201');

-- ----------------------------
-- Table structure for `sec_user_authority`
-- ----------------------------
DROP TABLE IF EXISTS `sec_user_authority`;
CREATE TABLE `sec_user_authority` (
  `user_id` int(11) NOT NULL,
  `authority_id` int(11) NOT NULL,
  KEY `FK_USER_AUTHORITY1` (`authority_id`),
  KEY `FK_USER_AUTHORITY2` (`user_id`),
  CONSTRAINT `FK_USER_AUTHORITY1` FOREIGN KEY (`authority_id`) REFERENCES `sec_authority` (`id`),
  CONSTRAINT `FK_USER_AUTHORITY2` FOREIGN KEY (`user_id`) REFERENCES `sec_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sec_user_authority
-- ----------------------------

-- ----------------------------
-- Table structure for `tbl_myform1`
-- ----------------------------
DROP TABLE IF EXISTS `tbl_myform1`;
CREATE TABLE `tbl_myform1` (
  `ID` int(11) NOT NULL auto_increment,
  `xm` varchar(255) NOT NULL default '',
  `age` varchar(255) NOT NULL default '',
  `FORMID` int(11) NOT NULL,
  `UPDATETIME` varchar(20) default NULL,
  `ORDERID` varchar(50) default NULL,
  `TASKID` varchar(50) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tbl_myform1
-- ----------------------------
INSERT INTO `tbl_myform1` VALUES ('1', '啊张三', '10', '1', '2014-12-28 17:56:22', '', '');
INSERT INTO `tbl_myform1` VALUES ('2', '李四', '20', '1', '2014-12-28 17:56:39', '', '');

-- ----------------------------
-- Table structure for `wf_cc_order`
-- ----------------------------
DROP TABLE IF EXISTS `wf_cc_order`;
CREATE TABLE `wf_cc_order` (
  `order_Id` varchar(100) default NULL COMMENT '流程实例ID',
  `actor_Id` varchar(100) default NULL COMMENT '参与者ID',
  `status` tinyint(1) default NULL COMMENT '状态',
  `create_Time` varchar(50) default NULL COMMENT '创建时间',
  `finish_Time` varchar(50) default NULL COMMENT '完成时间',
  KEY `IDX_CCORDER_ORDER` (`order_Id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='抄送实例表';

-- ----------------------------
-- Records of wf_cc_order
-- ----------------------------
INSERT INTO `wf_cc_order` VALUES ('531e4b88ff274df9b10b421928c64037', '2000', '1', '2015-02-05 16:00:39', null);
INSERT INTO `wf_cc_order` VALUES ('158ddf7cf8d447c6a1d797a3f7beeb46', '2000', '1', '2015-02-05 16:30:19', null);

-- ----------------------------
-- Table structure for `wf_hist_order`
-- ----------------------------
DROP TABLE IF EXISTS `wf_hist_order`;
CREATE TABLE `wf_hist_order` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `process_Id` varchar(100) NOT NULL COMMENT '流程定义ID',
  `order_State` tinyint(1) NOT NULL COMMENT '状态',
  `creator` varchar(100) default NULL COMMENT '发起人',
  `create_Time` varchar(50) NOT NULL COMMENT '发起时间',
  `end_Time` varchar(50) default NULL COMMENT '完成时间',
  `expire_Time` varchar(50) default NULL COMMENT '期望完成时间',
  `priority` tinyint(1) default NULL COMMENT '优先级',
  `parent_Id` varchar(100) default NULL COMMENT '父流程ID',
  `order_No` varchar(100) default NULL COMMENT '流程实例编号',
  `variable` varchar(2000) default NULL COMMENT '附属变量json存储',
  PRIMARY KEY  (`id`),
  KEY `IDX_HIST_ORDER_PROCESSID` (`process_Id`),
  KEY `IDX_HIST_ORDER_NO` (`order_No`),
  KEY `FK_HIST_ORDER_PARENTID` (`parent_Id`),
  KEY `FK_HIST_ORDER_PROCESSID` (`process_Id`),
  CONSTRAINT `FK_HIST_ORDER_PARENTID` FOREIGN KEY (`parent_Id`) REFERENCES `wf_hist_order` (`id`),
  CONSTRAINT `FK_HIST_ORDER_PROCESSID` FOREIGN KEY (`process_Id`) REFERENCES `wf_process` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='历史流程实例表';

-- ----------------------------
-- Records of wf_hist_order
-- ----------------------------
INSERT INTO `wf_hist_order` VALUES ('158ddf7cf8d447c6a1d797a3f7beeb46', 'c447f68ba2854f74a2b8810e315ba28d', '0', 'admin', '2015-02-05 16:29:00', '2015-02-05 16:33:50', null, null, null, '20150205-16:29:00-589-206', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"测试3天的处理情况\",\"day\":3,\"orderId\":\"\"}');
INSERT INTO `wf_hist_order` VALUES ('531e4b88ff274df9b10b421928c64037', 'c447f68ba2854f74a2b8810e315ba28d', '0', 'admin', '2015-01-22 11:26:42', '2015-02-05 16:00:23', null, null, null, '20150122-11:26:42-194-591', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"2\",\"day\":2,\"orderId\":\"\"}');
INSERT INTO `wf_hist_order` VALUES ('931d13e3dbf04aea9959e94a75dbfca8', 'c447f68ba2854f74a2b8810e315ba28d', '1', 'admin', '2015-01-17 18:26:22', null, null, null, null, '20150117-18:26:23-063-372', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"snaker\",\"approveDept.operator\":\"test\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"dfdf\",\"day\":1,\"orderId\":\"\"}');
INSERT INTO `wf_hist_order` VALUES ('f0a73ac16cae44679301a21b758fb2e7', 'c447f68ba2854f74a2b8810e315ba28d', '1', 'admin', '2015-02-05 16:37:51', null, null, null, null, '20150205-16:37:51-348-995', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"5天，测试target\\r\\n\",\"day\":5,\"orderId\":\"\"}');

-- ----------------------------
-- Table structure for `wf_hist_task`
-- ----------------------------
DROP TABLE IF EXISTS `wf_hist_task`;
CREATE TABLE `wf_hist_task` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `order_Id` varchar(100) NOT NULL COMMENT '流程实例ID',
  `task_Name` varchar(100) NOT NULL COMMENT '任务名称',
  `display_Name` varchar(200) NOT NULL COMMENT '任务显示名称',
  `task_Type` tinyint(1) NOT NULL COMMENT '任务类型',
  `perform_Type` tinyint(1) default NULL COMMENT '参与类型',
  `task_State` tinyint(1) NOT NULL COMMENT '任务状态',
  `operator` varchar(100) default NULL COMMENT '任务处理人',
  `create_Time` varchar(50) NOT NULL COMMENT '任务创建时间',
  `finish_Time` varchar(50) default NULL COMMENT '任务完成时间',
  `expire_Time` varchar(50) default NULL COMMENT '任务期望完成时间',
  `action_Url` varchar(200) default NULL COMMENT '任务处理url',
  `parent_Task_Id` varchar(100) default NULL COMMENT '父任务ID',
  `variable` varchar(2000) default NULL COMMENT '附属变量json存储',
  PRIMARY KEY  (`id`),
  KEY `IDX_HIST_TASK_ORDER` (`order_Id`),
  KEY `IDX_HIST_TASK_TASKNAME` (`task_Name`),
  KEY `IDX_HIST_TASK_PARENTTASK` (`parent_Task_Id`),
  KEY `FK_HIST_TASK_ORDERID` (`order_Id`),
  CONSTRAINT `FK_HIST_TASK_ORDERID` FOREIGN KEY (`order_Id`) REFERENCES `wf_hist_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='历史任务表';

-- ----------------------------
-- Records of wf_hist_task
-- ----------------------------
INSERT INTO `wf_hist_task` VALUES ('0e8045c7e8e84f31b64740727ac969cf', '158ddf7cf8d447c6a1d797a3f7beeb46', 'approveDept', '部门经理审批', '0', '0', '0', 'admin', '2015-02-05 16:29:00', '2015-02-05 16:30:19', null, '/flow/leave/approveDept', '17ba39a405134fb6a7c5ce2a9c1c4302', '{\"taskId\":\"0e8045c7e8e84f31b64740727ac969cf\",\"nextOperator\":\"\",\"ccOperatorName\":\"行政部\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"ccOperator\":\"2000\",\"nextOperatorName\":\"\",\"method\":\"0\",\"approveDept.suggest\":\"同意\",\"orderId\":\"158ddf7cf8d447c6a1d797a3f7beeb46\"}');
INSERT INTO `wf_hist_task` VALUES ('17ba39a405134fb6a7c5ce2a9c1c4302', '158ddf7cf8d447c6a1d797a3f7beeb46', 'apply', '请假申请', '0', '0', '0', 'admin', '2015-02-05 16:29:00', '2015-02-05 16:29:00', null, '/flow/leave/apply', 'start', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"测试3天的处理情况\",\"day\":3,\"orderId\":\"\"}');
INSERT INTO `wf_hist_task` VALUES ('3af1ed73cd6e4d51af035e2bbe6acffa', '931d13e3dbf04aea9959e94a75dbfca8', 'apply', '请假申请', '0', '0', '0', 'admin', '2015-01-17 18:26:23', '2015-01-17 18:26:23', null, '/flow/leave/apply', 'start', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"snaker\",\"approveDept.operator\":\"test\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"dfdf\",\"day\":1,\"orderId\":\"\"}');
INSERT INTO `wf_hist_task` VALUES ('70dff51d62ff46fe97b4811fa6363f11', '531e4b88ff274df9b10b421928c64037', 'apply', '请假申请', '0', '0', '0', 'admin', '2015-01-22 11:26:42', '2015-01-22 11:26:42', null, '/flow/leave/apply', 'start', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"2\",\"day\":2,\"orderId\":\"\"}');
INSERT INTO `wf_hist_task` VALUES ('9552244bf8d44956bd45a83fee2d2030', '531e4b88ff274df9b10b421928c64037', 'approveDept', '部门经理审批', '0', '0', '0', 'admin', '2015-01-22 11:26:43', '2015-02-05 16:00:22', null, '/flow/leave/approveDept', '70dff51d62ff46fe97b4811fa6363f11', '{\"taskId\":\"9552244bf8d44956bd45a83fee2d2030\",\"nextOperator\":\"\",\"ccOperatorName\":\"行政部\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"ccOperator\":\"2000\",\"nextOperatorName\":\"\",\"method\":\"0\",\"approveDept.suggest\":\"同意！\",\"orderId\":\"531e4b88ff274df9b10b421928c64037\"}');
INSERT INTO `wf_hist_task` VALUES ('a848b78ed8004309b0d2f44b73255ee7', '158ddf7cf8d447c6a1d797a3f7beeb46', 'approveBoss', '总经理审批', '0', '0', '0', 'admin', '2015-02-05 16:30:19', '2015-02-05 16:33:50', null, '/flow/leave/approveBoss', '0e8045c7e8e84f31b64740727ac969cf', '{\"taskId\":\"a848b78ed8004309b0d2f44b73255ee7\",\"approveBoss.suggest\":\"同意！\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"method\":\"0\",\"orderId\":\"158ddf7cf8d447c6a1d797a3f7beeb46\"}');
INSERT INTO `wf_hist_task` VALUES ('b1ce25af049a457dae0634635a42bd3b', 'f0a73ac16cae44679301a21b758fb2e7', 'approveDept', '部门经理审批', '0', '0', '0', 'admin', '2015-02-05 16:37:51', '2015-03-03 14:14:06', null, '/flow/leave/approveDept', 'cf9d69ac7b58487ca5f7f3ec66e34595', '{\"taskId\":\"b1ce25af049a457dae0634635a42bd3b\",\"nextOperator\":\"\",\"ccOperatorName\":\"\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"ccOperator\":\"\",\"nextOperatorName\":\"\",\"method\":\"0\",\"approveDept.suggest\":\"同意\",\"orderId\":\"f0a73ac16cae44679301a21b758fb2e7\"}');
INSERT INTO `wf_hist_task` VALUES ('cf9d69ac7b58487ca5f7f3ec66e34595', 'f0a73ac16cae44679301a21b758fb2e7', 'apply', '请假申请', '0', '0', '0', 'admin', '2015-02-05 16:37:51', '2015-02-05 16:37:51', null, '/flow/leave/apply', 'start', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"5天，测试target\\r\\n\",\"day\":5,\"orderId\":\"\"}');

-- ----------------------------
-- Table structure for `wf_hist_task_actor`
-- ----------------------------
DROP TABLE IF EXISTS `wf_hist_task_actor`;
CREATE TABLE `wf_hist_task_actor` (
  `task_Id` varchar(100) NOT NULL COMMENT '任务ID',
  `actor_Id` varchar(100) NOT NULL COMMENT '参与者ID',
  KEY `IDX_HIST_TASKACTOR_TASK` (`task_Id`),
  KEY `FK_HIST_TASKACTOR` (`task_Id`),
  CONSTRAINT `FK_HIST_TASKACTOR` FOREIGN KEY (`task_Id`) REFERENCES `wf_hist_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='历史任务参与者表';

-- ----------------------------
-- Records of wf_hist_task_actor
-- ----------------------------
INSERT INTO `wf_hist_task_actor` VALUES ('3af1ed73cd6e4d51af035e2bbe6acffa', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('70dff51d62ff46fe97b4811fa6363f11', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('9552244bf8d44956bd45a83fee2d2030', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('17ba39a405134fb6a7c5ce2a9c1c4302', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('0e8045c7e8e84f31b64740727ac969cf', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('a848b78ed8004309b0d2f44b73255ee7', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('cf9d69ac7b58487ca5f7f3ec66e34595', 'admin');
INSERT INTO `wf_hist_task_actor` VALUES ('b1ce25af049a457dae0634635a42bd3b', 'admin');

-- ----------------------------
-- Table structure for `wf_order`
-- ----------------------------
DROP TABLE IF EXISTS `wf_order`;
CREATE TABLE `wf_order` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `parent_Id` varchar(100) default NULL COMMENT '父流程ID',
  `process_Id` varchar(100) NOT NULL COMMENT '流程定义ID',
  `creator` varchar(100) default NULL COMMENT '发起人',
  `create_Time` varchar(50) NOT NULL COMMENT '发起时间',
  `expire_Time` varchar(50) default NULL COMMENT '期望完成时间',
  `last_Update_Time` varchar(50) default NULL COMMENT '上次更新时间',
  `last_Updator` varchar(100) default NULL COMMENT '上次更新人',
  `priority` tinyint(1) default NULL COMMENT '优先级',
  `parent_Node_Name` varchar(100) default NULL COMMENT '父流程依赖的节点名称',
  `order_No` varchar(100) default NULL COMMENT '流程实例编号',
  `variable` varchar(2000) default NULL COMMENT '附属变量json存储',
  `version` int(3) default NULL COMMENT '版本',
  PRIMARY KEY  (`id`),
  KEY `IDX_ORDER_PROCESSID` (`process_Id`),
  KEY `IDX_ORDER_NO` (`order_No`),
  KEY `FK_ORDER_PARENTID` (`parent_Id`),
  KEY `FK_ORDER_PROCESSID` (`process_Id`),
  CONSTRAINT `FK_ORDER_PARENTID` FOREIGN KEY (`parent_Id`) REFERENCES `wf_order` (`id`),
  CONSTRAINT `FK_ORDER_PROCESSID` FOREIGN KEY (`process_Id`) REFERENCES `wf_process` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程实例表';

-- ----------------------------
-- Records of wf_order
-- ----------------------------
INSERT INTO `wf_order` VALUES ('931d13e3dbf04aea9959e94a75dbfca8', null, 'c447f68ba2854f74a2b8810e315ba28d', 'admin', '2015-01-17 18:26:22', null, '2015-01-17 18:26:22', 'admin', null, null, '20150117-18:26:23-063-372', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"snaker\",\"approveDept.operator\":\"test\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"dfdf\",\"day\":1,\"orderId\":\"\"}', null);
INSERT INTO `wf_order` VALUES ('f0a73ac16cae44679301a21b758fb2e7', null, 'c447f68ba2854f74a2b8810e315ba28d', 'admin', '2015-02-05 16:37:51', null, '2015-02-05 16:37:51', 'admin', null, null, '20150205-16:37:51-348-995', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"5天，测试target\\r\\n\",\"day\":5,\"orderId\":\"\"}', null);

-- ----------------------------
-- Table structure for `wf_process`
-- ----------------------------
DROP TABLE IF EXISTS `wf_process`;
CREATE TABLE `wf_process` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `name` varchar(100) default NULL COMMENT '流程名称',
  `display_Name` varchar(200) default NULL COMMENT '流程显示名称',
  `type` varchar(100) default NULL COMMENT '流程类型',
  `instance_Url` varchar(200) default NULL COMMENT '实例url',
  `state` tinyint(1) default NULL COMMENT '流程是否可用',
  `content` longblob COMMENT '流程模型定义',
  `version` int(2) default NULL COMMENT '版本',
  `create_Time` varchar(50) default NULL COMMENT '创建时间',
  `creator` varchar(50) default NULL COMMENT '创建人',
  PRIMARY KEY  (`id`),
  KEY `IDX_PROCESS_NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程定义表';

-- ----------------------------
-- Records of wf_process
-- ----------------------------
INSERT INTO `wf_process` VALUES ('59c402a162e44df098811a8cd0832fca', 'forkjoin', 'forkjoin流程测试', null, '', '1', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D226E6F223F3E0A3C70726F6365737320646973706C61794E616D653D22666F726B6A6F696EE6B581E7A88BE6B58BE8AF9522206E616D653D22666F726B6A6F696E223E0A3C737461727420646973706C61794E616D653D2273746172743122206C61796F75743D2234342C3137372C2D312C2D3122206E616D653D22737461727431223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3122206F66667365743D22302C302220746F3D227461736B31222F3E0A3C2F73746172743E0A3C656E6420646973706C61794E616D653D22656E643122206C61796F75743D223736342C3137372C2D312C2D3122206E616D653D22656E6431222F3E0A3C7461736B2061737369676E65653D227461736B312E6F70657261746F722220646973706C61794E616D653D22E4BBBBE58AA1312220666F726D3D22666F726B6A6F696E2F7461736B312E68746D6C22206C61796F75743D223135362C3137352C2D312C2D3122206E616D653D227461736B312220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3222206F66667365743D22302C302220746F3D22666F726B31222F3E0A3C2F7461736B3E0A3C666F726B20646973706C61794E616D653D22666F726B3122206C61796F75743D223238392C3137372C2D312C2D3122206E616D653D22666F726B31223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3322206F66667365743D22302C302220746F3D227461736B32222F3E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3522206F66667365743D22302C302220746F3D227461736B33222F3E0A3C2F666F726B3E0A3C6A6F696E20646973706C61794E616D653D226A6F696E3122206C61796F75743D223535372C3137372C2D312C2D3122206E616D653D226A6F696E31223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3722206F66667365743D22302C302220746F3D227461736B34222F3E0A3C2F6A6F696E3E0A3C7461736B2061737369676E65653D227461736B322E6F70657261746F722220646973706C61794E616D653D22E4BBBBE58AA1322220666F726D3D22666F726B6A6F696E2F7461736B322E68746D6C22206C61796F75743D223430342C3132362C2D312C2D3122206E616D653D227461736B322220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3422206F66667365743D22302C302220746F3D226A6F696E31222F3E0A3C2F7461736B3E0A3C7461736B2061737369676E65653D227461736B332E6F70657261746F722220646973706C61794E616D653D22E4BBBBE58AA1332220666F726D3D22666F726B6A6F696E2F7461736B332E68746D6C22206C61796F75743D223430342C3232362C2D312C2D3122206E616D653D227461736B332220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3622206F66667365743D22302C302220746F3D226A6F696E31222F3E0A3C2F7461736B3E0A3C7461736B2061737369676E65653D227461736B342E6F70657261746F722220646973706C61794E616D653D22E4BBBBE58AA1342220666F726D3D22666F726B6A6F696E2F7461736B342E68746D6C22206C61796F75743D223634352C3137352C2D312C2D3122206E616D653D227461736B342220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3822206F66667365743D22302C302220746F3D22656E6431222F3E0A3C2F7461736B3E0A3C2F70726F636573733E0A, '0', '2015-03-03 14:30:10', null);
INSERT INTO `wf_process` VALUES ('926a7f990ec247ffbd34135dcf3fea9c', 'borrow', '借款申请流程', null, '/snaker/flow/all', '1', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D226E6F223F3E0A3C70726F63657373206E616D653D22626F72726F772220646973706C61794E616D653D22E5809FE6ACBEE794B3E8AFB7E6B581E7A88B2220696E7374616E636555726C3D222F736E616B65722F666C6F772F616C6C22203E0A3C7374617274206C61796F75743D2232342C3132302C35302C353022206E616D653D2273746172743122203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D226170706C7922206E616D653D2270617468313322202F3E0A3C2F73746172743E0A3C7461736B206C61796F75743D223130382C3132322C3130332C343522206E616D653D226170706C792220646973706C61794E616D653D22E794B3E8AFB7E5809FE6ACBE222061737369676E65653D226170706C792E6F70657261746F7222206175746F457865637574653D225922203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D22617070726F76616C22206E616D653D2270617468323722202F3E0A3C2F7461736B3E0A3C7461736B206C61796F75743D223237312C3132302C39372C343622206E616D653D22617070726F76616C2220646973706C61794E616D653D22E5AEA1E689B922203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D2272656374313822206E616D653D2270617468323822202F3E0A3C2F7461736B3E0A3C656E64206C61796F75743D223530362C3131382C35302C353022206E616D653D22656E6422203E0A3C2F656E643E0A3C6465636973696F6E206C61796F75743D223431342C3131362C33382C353222206E616D653D2272656374313822203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D22656E6422206E616D653D227061746832392220646973706C61794E616D653D22E5908CE6848F22202F3E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D226170706C79222020673D223433352C33393B3136332C343322206E616D653D227061746833302220646973706C61794E616D653D22E4B88DE5908CE6848F22202F3E0A3C2F6465636973696F6E3E0A3C2F70726F636573733E, '0', '2015-01-14 16:16:17', null);
INSERT INTO `wf_process` VALUES ('a86bd91c5d3743fc821b04e7e2d9c9cb', 'test1', '请假申请', null, '', '1', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D226E6F223F3E0A3C70726F63657373206E616D653D2274657374312220646973706C61794E616D653D22E8AFB7E58187E794B3E8AFB722203E0A3C7374617274206C61796F75743D2236302C3130382C35302C353022206E616D653D22737461727422203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D22726563743222206E616D653D22706174683722202F3E0A3C2F73746172743E0A3C7461736B206C61796F75743D223135312C3130372C3130302C353022206E616D653D2272656374322220646973706C61794E616D653D22E8AFB7E58187E794B3E8AFB722203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D22726563743322206E616D653D22706174683822202F3E0A3C2F7461736B3E0A3C7461736B206C61796F75743D223238332C3130372C3130302C353022206E616D653D2272656374332220646973706C61794E616D653D22E5AEA1E6A0B822203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D22726563743422206E616D653D22706174683922202F3E0A3C2F7461736B3E0A3C6465636973696F6E206C61796F75743D223431362C3132352C33382C313722206E616D653D22726563743422203E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D22656E6422206E616D653D227061746831302220646973706C61794E616D653D22E9809AE8BF8722202F3E0A3C7472616E736974696F6E206F66667365743D22302C2D31302220746F3D227265637432222020673D223433392C37353B3139392C373022206E616D653D227061746831312220646973706C61794E616D653D22E4B88DE9809AE8BF8722202F3E0A3C2F6465636973696F6E3E0A3C656E64206C61796F75743D223530372C3131312C35302C353022206E616D653D22656E6422203E0A3C2F656E643E0A3C2F70726F636573733E, '0', '2015-01-16 11:46:43', null);
INSERT INTO `wf_process` VALUES ('a96247210f28439db3e09a2dafc32b95', 'free', '自由流', null, '/flow/free/all', '1', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D226E6F223F3E0A3C70726F6365737320646973706C61794E616D653D22E887AAE794B1E6B5812220696E7374616E636555726C3D222F666C6F772F667265652F616C6C22206E616D653D2266726565222F3E0A, '0', '2015-03-03 14:31:01', null);
INSERT INTO `wf_process` VALUES ('b81183afcb294ef0bdd018bf5e39e190', 'group', '参与者为组测试', null, '', '1', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D226E6F223F3E0A3C70726F6365737320646973706C61794E616D653D22E58F82E4B88EE88085E4B8BAE7BB84E6B58BE8AF9522206E616D653D2267726F7570223E0A3C737461727420646973706C61794E616D653D2273746172743122206C61796F75743D2233362C3131352C2D312C2D3122206E616D653D22737461727431223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3122206F66667365743D22302C302220746F3D227461736B31222F3E0A3C2F73746172743E0A3C656E6420646973706C61794E616D653D22656E643122206C61796F75743D223435352C3131352C2D312C2D3122206E616D653D22656E6431222F3E0A3C7461736B206175746F457865637574653D22592220646973706C61794E616D653D22E4BBBBE58AA1312220666F726D3D2267726F75702F7461736B312E68746D6C22206C61796F75743D223134392C3131332C2D312C2D3122206E616D653D227461736B312220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3222206F66667365743D22302C302220746F3D227461736B32222F3E0A3C2F7461736B3E0A3C7461736B2061737369676E65653D227461736B322E6F70657261746F7222206175746F457865637574653D22592220646973706C61794E616D653D22E4BBBBE58AA1322220666F726D3D2267726F75702F7461736B322E68746D6C22206C61796F75743D223330322C3131332C2D312C2D3122206E616D653D227461736B322220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3322206F66667365743D22302C302220746F3D22656E6431222F3E0A3C2F7461736B3E0A3C2F70726F636573733E0A, '0', '2015-03-03 14:32:09', null);
INSERT INTO `wf_process` VALUES ('c447f68ba2854f74a2b8810e315ba28d', 'leave', '请假流程测试', null, '/snaker/flow/all', '1', 0x3C3F786D6C2076657273696F6E3D22312E302220656E636F64696E673D225554462D3822207374616E64616C6F6E653D226E6F223F3E0A3C70726F6365737320646973706C61794E616D653D22E8AFB7E58187E6B581E7A88BE6B58BE8AF952220696E7374616E636555726C3D222F736E616B65722F666C6F772F616C6C22206E616D653D226C65617665223E0A3C737461727420646973706C61794E616D653D2273746172743122206C61796F75743D2232342C3132342C2D312C2D3122206E616D653D22737461727431223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3122206F66667365743D22302C302220746F3D226170706C79222F3E0A3C2F73746172743E0A3C656E6420646973706C61794E616D653D22656E643122206C61796F75743D223537302C3132342C2D312C2D3122206E616D653D22656E6431222F3E0A3C7461736B2061737369676E65653D226170706C792E6F70657261746F722220646973706C61794E616D653D22E8AFB7E58187E794B3E8AFB72220666F726D3D222F666C6F772F6C656176652F6170706C7922206C61796F75743D223131372C3132322C2D312C2D3122206E616D653D226170706C792220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3222206F66667365743D22302C302220746F3D22617070726F766544657074222F3E0A3C2F7461736B3E0A3C7461736B2061737369676E65653D22617070726F7665446570742E6F70657261746F722220646973706C61794E616D653D22E983A8E997A8E7BB8FE79086E5AEA1E689B92220666F726D3D222F666C6F772F6C656176652F617070726F76654465707422206C61796F75743D223237322C3132322C2D312C2D3122206E616D653D22617070726F7665446570742220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3322206F66667365743D22302C302220746F3D226465636973696F6E31222F3E0A3C2F7461736B3E0A3C6465636973696F6E20646973706C61794E616D653D226465636973696F6E312220657870723D22247B646179202667743B2032203F20277472616E736974696F6E3527203A20277472616E736974696F6E34277D22206C61796F75743D223432362C3132342C2D312C2D3122206E616D653D226465636973696F6E31223E0A3C7472616E736974696F6E20646973706C61794E616D653D22266C743B3D32E5A4A92220673D2222206E616D653D227472616E736974696F6E3422206F66667365743D22302C302220746F3D22656E6431222F3E0A3C7472616E736974696F6E20646973706C61794E616D653D222667743B32E5A4A92220673D2222206E616D653D227472616E736974696F6E3522206F66667365743D22302C302220746F3D22617070726F7665426F7373222F3E0A3C2F6465636973696F6E3E0A3C7461736B2061737369676E65653D22617070726F7665426F73732E6F70657261746F722220646973706C61794E616D653D22E680BBE7BB8FE79086E5AEA1E689B92220666F726D3D222F666C6F772F6C656176652F617070726F7665426F737322206C61796F75743D223430342C3233312C2D312C2D3122206E616D653D22617070726F7665426F73732220706572666F726D547970653D22414E59223E0A3C7472616E736974696F6E20673D2222206E616D653D227472616E736974696F6E3622206F66667365743D22302C302220746F3D22656E6431222F3E0A3C2F7461736B3E0A3C2F70726F636573733E0A, '0', '2015-01-16 14:45:12', null);

-- ----------------------------
-- Table structure for `wf_surrogate`
-- ----------------------------
DROP TABLE IF EXISTS `wf_surrogate`;
CREATE TABLE `wf_surrogate` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `process_Name` varchar(100) default NULL COMMENT '流程名称',
  `operator` varchar(100) default NULL COMMENT '授权人',
  `surrogate` varchar(100) default NULL COMMENT '代理人',
  `odate` varchar(64) default NULL COMMENT '操作时间',
  `sdate` varchar(64) default NULL COMMENT '开始时间',
  `edate` varchar(64) default NULL COMMENT '结束时间',
  `state` tinyint(1) default NULL COMMENT '状态',
  PRIMARY KEY  (`id`),
  KEY `IDX_SURROGATE_OPERATOR` (`operator`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='委托代理表';

-- ----------------------------
-- Records of wf_surrogate
-- ----------------------------
INSERT INTO `wf_surrogate` VALUES ('0f284d60544b43388c00f4a57ebd0fad', 'borrow', 'admin', 'snaker', null, '2015-02-28', '2015-03-03', '1');
INSERT INTO `wf_surrogate` VALUES ('1124369b95d549dca0ce6547569fb523', 'leave', 'admin', 'snaker', null, '2015-02-28', '2015-03-03', '1');
INSERT INTO `wf_surrogate` VALUES ('71fab07829754cf39d1b1b02dc2c6e1d', 'test1', 'admin', 'test', null, '2015-03-03', '2015-03-10', '1');
INSERT INTO `wf_surrogate` VALUES ('8a1ffc98ac824fd0a70a116a75658586', 'leave', 'admin', 'snaker', null, '2015-02-28', '2015-03-03', '1');
INSERT INTO `wf_surrogate` VALUES ('8d0f5379e2104184aac2c46b31a198e2', 'borrow', 'admin', 'snaker', null, '2015-02-28', '2015-03-03', '1');
INSERT INTO `wf_surrogate` VALUES ('920641c0bdd44cdd9a44f4a83c3bcc60', 'test1', 't1', 'r1', null, '2015-03-03', '2015-03-06', '1');

-- ----------------------------
-- Table structure for `wf_task`
-- ----------------------------
DROP TABLE IF EXISTS `wf_task`;
CREATE TABLE `wf_task` (
  `id` varchar(100) NOT NULL COMMENT '主键ID',
  `order_Id` varchar(100) NOT NULL COMMENT '流程实例ID',
  `task_Name` varchar(100) NOT NULL COMMENT '任务名称',
  `display_Name` varchar(200) NOT NULL COMMENT '任务显示名称',
  `task_Type` tinyint(1) NOT NULL COMMENT '任务类型',
  `perform_Type` tinyint(1) default NULL COMMENT '参与类型',
  `operator` varchar(100) default NULL COMMENT '任务处理人',
  `create_Time` varchar(50) default NULL COMMENT '任务创建时间',
  `finish_Time` varchar(50) default NULL COMMENT '任务完成时间',
  `expire_Time` varchar(50) default NULL COMMENT '任务期望完成时间',
  `action_Url` varchar(200) default NULL COMMENT '任务处理的url',
  `parent_Task_Id` varchar(100) default NULL COMMENT '父任务ID',
  `variable` varchar(2000) default NULL COMMENT '附属变量json存储',
  `version` tinyint(1) default NULL COMMENT '版本',
  PRIMARY KEY  (`id`),
  KEY `IDX_TASK_ORDER` (`order_Id`),
  KEY `IDX_TASK_TASKNAME` (`task_Name`),
  KEY `IDX_TASK_PARENTTASK` (`parent_Task_Id`),
  KEY `FK_TASK_ORDERID` (`order_Id`),
  CONSTRAINT `FK_TASK_ORDERID` FOREIGN KEY (`order_Id`) REFERENCES `wf_order` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务表';

-- ----------------------------
-- Records of wf_task
-- ----------------------------
INSERT INTO `wf_task` VALUES ('907a307e48e5489081b02346809dffe4', '931d13e3dbf04aea9959e94a75dbfca8', 'approveDept', '部门经理审批', '0', '0', null, '2015-01-17 18:26:23', null, null, '/flow/leave/approveDept', '3af1ed73cd6e4d51af035e2bbe6acffa', '{\"taskId\":\"\",\"apply.operator\":\"admin\",\"approveBoss.operator\":\"snaker\",\"approveDept.operator\":\"test\",\"submit\":\"提交\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"reason\":\"dfdf\",\"day\":1,\"orderId\":\"\"}', null);
INSERT INTO `wf_task` VALUES ('a7d794d5e34541a092a1ab3f5cd62187', 'f0a73ac16cae44679301a21b758fb2e7', 'approveBoss', '总经理审批', '0', '0', null, '2015-03-03 14:14:06', null, null, '/flow/leave/approveBoss', 'b1ce25af049a457dae0634635a42bd3b', '{\"taskId\":\"b1ce25af049a457dae0634635a42bd3b\",\"approveBoss.operator\":\"admin\",\"submit\":\"提交\",\"ccOperatorName\":\"\",\"reason\":\"5天，测试target\\r\\n\",\"processId\":\"c447f68ba2854f74a2b8810e315ba28d\",\"nextOperator\":\"\",\"apply.operator\":\"admin\",\"approveDept.operator\":\"admin\",\"ccOperator\":\"\",\"nextOperatorName\":\"\",\"method\":\"0\",\"day\":5,\"approveDept.suggest\":\"同意\",\"orderId\":\"f0a73ac16cae44679301a21b758fb2e7\"}', null);

-- ----------------------------
-- Table structure for `wf_task_actor`
-- ----------------------------
DROP TABLE IF EXISTS `wf_task_actor`;
CREATE TABLE `wf_task_actor` (
  `task_Id` varchar(100) NOT NULL COMMENT '任务ID',
  `actor_Id` varchar(100) NOT NULL COMMENT '参与者ID',
  KEY `IDX_TASKACTOR_TASK` (`task_Id`),
  KEY `FK_TASK_ACTOR_TASKID` (`task_Id`),
  CONSTRAINT `FK_TASK_ACTOR_TASKID` FOREIGN KEY (`task_Id`) REFERENCES `wf_task` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='任务参与者表';

-- ----------------------------
-- Records of wf_task_actor
-- ----------------------------
INSERT INTO `wf_task_actor` VALUES ('907a307e48e5489081b02346809dffe4', 'test');
INSERT INTO `wf_task_actor` VALUES ('a7d794d5e34541a092a1ab3f5cd62187', 'admin');
