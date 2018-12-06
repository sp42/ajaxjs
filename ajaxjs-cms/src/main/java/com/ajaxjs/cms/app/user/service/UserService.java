package com.ajaxjs.cms.app.user.service;

import com.ajaxjs.cms.app.user.model.User;
import com.ajaxjs.cms.app.user.model.UserCommonAuth;
import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.framework.service.IService;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.web.UploadFileInfo;

public interface UserService extends IService<User, Long> {
	/**
	 * 普通口令注册
	 * 
	 * @param user
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public Long create(User user, UserCommonAuth password) throws ServiceException;

	/**
	 * 普通口令登录
	 * 
	 * @param user 可能包含 name、phone、email 字段
	 * @param userLoginInfo 用户提交的登录信息
	 * @return
	 * @throws ServiceException
	 */
	public boolean loginByPassword(User user, UserCommonAuth userLoginInfo) throws ServiceException;

	/**
	 * 通过邮件重置密码
	 * 
	 * @param user 必须包含 email 字段
	 * @return 返回修改密码的 Token
	 * @throws ServiceException
	 */
	public String resetPasswordByEmail(User user) throws ServiceException;

	/**
	 * 验证重置密码的 token 是否有效
	 * 
	 * @param token
	 * @param email 用户提交用于对比的 email
	 * @return
	 * @throws ServiceException
	 */
	public boolean validResetPasswordByEmail(String token, String email) throws ServiceException;

	/**
	 * 检查用户名是否重复
	 * 
	 * @return 用户名是否重复
	 */
	public boolean checkIfUserNameRepeat(User user);

	/**
	 * 检查用户名是否重复
	 * 
	 * @return 用户名是否重复
	 */
	public boolean checkIfUserNameRepeat(String userName);

	/**
	 * 检查用户手机是否重复
	 * 
	 * @return 手机是否重复
	 */
	public boolean checkIfUserPhoneRepeat(User user);

	/**
	 * 检查用户手机是否重复
	 * 
	 * @return 手机是否重复
	 */
	public boolean checkIfUserPhoneRepeat(String phone);

	public Attachment_picture findAvaterByUserId(long userId);

	/**
	 * 查找是否有用戶頭像，如果有則更新，沒有則創建之
	 * 
	 * @param userId 用户 id
	 * @return 用戶頭像
	 * @
	 */
	public Attachment_picture updateOrCreateAvatar(long userId, UploadFileInfo info) throws ServiceException;
}