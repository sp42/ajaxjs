package com.ajaxjs.cms.user.service;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.user.User;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@TableName(value = "user", beanClass = User.class)
public interface UserDao extends IBaseDao<User> {
	/**
	 * 根据用户名码查找用户
	 * 
	 * @param userName 用户名
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE name = ?")
	User findByUserName(String userName);

	/**
	 * 根据手机号码查找用户
	 * 
	 * @param phone 手机号码
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE phone = ? LIMIT 1")
	User findByPhone(String phone);

	/**
	 * 根据 email 查找用户
	 * 
	 * @param email 邮箱
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE email = ? LIMIT 1")
	User findByEmail(String email);

	/**
	 * 查找用户头像
	 * 
	 * @param userId
	 * @return
	 */

	@Select("SELECT * FROM attachment_picture WHERE catelog = 2 AND owner = ? ORDER BY id DESC LIMIT 1")
	Attachment_picture findAvaterByUserId(long userId);
	
	@Select("SELECT accessKey FROM user_role WHERE id = ?")
	Long getPrivilegeByUserGroupId(long userGroupId);
}