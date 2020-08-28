package com.ajaxjs.user;

import com.ajaxjs.app.attachment.Attachment_picture;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;

@TableName(value = "user", beanClass = User.class)
public interface UserDao extends IBaseDao<User> {
	/**
	 * 根据用户名码查找用户
	 * 
	 * @param userName 用户名
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE name = ?")
	public User findByUserName(String userName);

	/**
	 * 根据手机号码查找用户
	 * 
	 * @param phone 手机号码
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE phone = ? LIMIT 1")
	public User findByPhone(String phone);

	/**
	 * 根据 email 查找用户
	 * 
	 * @param email 邮箱
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE email = ? LIMIT 1")
	public User findByEmail(String email);

	/**
	 * 查找用户头像
	 * 
	 * @param userId
	 * @return
	 */
	@Select("SELECT * FROM attachment_picture WHERE catelog = 2 AND owner = ? ORDER BY id DESC LIMIT 1")
	public Attachment_picture findAvaterByUserId(long userId);

	/**
	 * 
	 * @param userGroupId
	 * @return
	 */
	@Select("SELECT accessKey FROM user_role WHERE id = ?")
	public Long getPrivilegeByUserGroupId(long userGroupId);

	/**
	 * 获取用户的名称，读取两个值，用户 id 即 user.name 和 用户昵称 user.username user 表名缩写必须为 u
	 */
	public static final String GET_USERNAME = " u.name AS userName, u.username AS userNickName ";
	
	public static final String LEFT_JOIN_USER = " LEFT JOIN user ON user.id = e.userId ";
	
	public static final String LEFT_JOIN_USER_SELECT = "SELECT e.*, user.name AS userName FROM ${tableName} e " + UserDao.LEFT_JOIN_USER + IBaseDao.WHERE_REMARK_ORDER;
}