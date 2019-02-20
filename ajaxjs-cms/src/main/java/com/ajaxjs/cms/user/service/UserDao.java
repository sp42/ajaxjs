package com.ajaxjs.cms.user.service;

import java.util.Date;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.user.User;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.orm.annotation.Delete;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.Update;

public interface UserDao extends IBaseDao<User> {
	final static String tableName = "user";

	@Select("SELECT * FROM " + tableName + " WHERE id = ?")
	@Override
	public User findById(Long id);

	@Select(value = "SELECT * FROM " + tableName)
	@Override
	public PageResult<User> findPagedList(int start, int limit);

	@Insert(tableName = tableName)
	@Override
	public Long create(User entry);

	@Update(tableName = tableName)
	@Override
	public int update(User entry);

	@Delete(tableName = tableName)
	@Override
	public boolean delete(User entry);

	@Insert("INSERT INTO user_login_log (userId, loginType, createDate, ip) VALUES (?, ?, ?, ?)")
	Long updateLoginInfo(long userId, long loginType, Date lastLoginDate, String ip);

	/**
	 * 根据用户名码查找用户
	 * 
	 * @param userName
	 *            用户名
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE name = ?")
	User findByUserName(String userName);

	/**
	 * 根据手机号码查找用户
	 * 
	 * @param phone
	 *            手机号码
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE phone = ? LIMIT 1")
	User findByPhone(String phone);

	/**
	 * 根据 email 查找用户
	 * 
	 * @param email
	 *            邮箱
	 * @return 返回用户，若 null 表示找不到用户
	 */
	@Select("SELECT * FROM user WHERE email = ? LIMIT 1")
	User findByEmail(String email);
	
	/**
	 * 查找用户头像
	 * @param userId
	 * @return
	 */
	
	@Select("SELECT * FROM attachment_picture WHERE catelog = 2 AND owner = ? ORDER BY id DESC LIMIT 1")
	Attachment_picture findAvaterByUserId(long userId); 
}