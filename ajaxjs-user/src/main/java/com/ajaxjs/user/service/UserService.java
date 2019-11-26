package com.ajaxjs.user.service;

import java.util.Date;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.app.attachment.Attachment_pictureService;
import com.ajaxjs.cms.app.attachment.Attachment_pictureServiceImpl;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.orm.JdbcConnection;
import com.ajaxjs.orm.JdbcReader;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.model.UserCommonAuth;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetricCipher;
import com.ajaxjs.util.io.ImageHelper;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;

@Bean("UserService")
public class UserService extends BaseService<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(UserService.class);

	public static UserDao dao = new Repository().bind(UserDao.class);

	{
		setUiName("用户");
		setShortName("user");
		setDao(dao);
	}

	@Resource("User_common_authService")
	private UserCommonAuthService passwordService;

	public UserCommonAuthService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(UserCommonAuthService passwordService) {
		this.passwordService = passwordService;
	}

	/**
	 * 普通口令注册
	 * 
	 * @param user			用户对象
	 * @param password		密码对象
	 * @return	新用户之 id
	 * @throws ServiceException
	 */
	public Long register(User user, UserCommonAuth password) throws ServiceException {
		LOGGER.info("用户注册");

		if (user.getPhone() != null) {
			user.setPhone(user.getPhone().trim()); // 消除空格
			checkIfRepeated("phone", user.getPhone(), "手机号码");
		}
		
		if (user.getName() != null) {
			user.setName(user.getName().trim());
			checkIfRepeated("name", user.getName(), "用户名");
		}
		
		if (user.getEmail() != null) {
			user.setEmail(user.getEmail().trim());
			checkIfRepeated("email", user.getEmail(), "邮箱");
		}

		Long userId = create(user);
		user.setId(userId);

		password.setUserId(userId);
		passwordService.create(password);

		return userId;
	}

	/**
	 * 
	 * @param user
	 * @param password
	 * @throws ServiceException
	 */
	public void register(User user, String password) throws ServiceException {
		LOGGER.info("执行用户注册");

		if (password == null)
			throw new IllegalArgumentException("注册密码不能为空");

		UserCommonAuth passwordModel = new UserCommonAuth();
		passwordModel.setPhone_verified(1); // 已验证
		passwordModel.setPassword(password);

		long id = register(user, passwordModel);
		LOGGER.info("创建用户成功，id：" + id);
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field		数据库里面的字段名称
	 * @param value		欲检查的值
	 * @param type		提示的类型
	 * @return true=值重复
	 * @throws ServiceException
	 */
	private static boolean checkIfRepeated(String field, String value, String type) throws ServiceException {
		if (value != null && checkIfRepeated(field, value))
			throw new ServiceException(type + " " + value + " 已注册");

		return false;
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field		数据库里面的字段名称
	 * @param value		欲检查的值
	 * @return true=值重复
	 */
	public static boolean checkIfRepeated(String field, String value) {
		value = value.trim();
		
		return value != null && JdbcReader.queryOne(JdbcConnection.getConnection(),
				"SELECT * FROM user WHERE " + field + " = ? LIMIT 1", Object.class, value) != null;
	}

	/**
	 * 
	 */
	private static final String encryptKey = "ErZwd#@$#@D32";

	/**
	 * 通过邮件重置密码
	 * 
	 * @param user 必须包含 email 字段
	 * @return 返回修改密码的 Token
	 * @throws ServiceException
	 */
	public String resetPasswordByEmail(User user) throws ServiceException {
		LOGGER.info("重置密码");

		String email = user.getEmail();

		if (email == null)
			throw new ServiceException("请提交邮件地址");

		user = dao.findByEmail(email);

		if (user == null)
			throw new ServiceException("该 email：" + email + " 的用户不存在！");

		String expireHex = Long.toHexString(System.currentTimeMillis());
		String emailToken = Encode.md5(encryptKey + email),
				timeToken = SymmetricCipher.AES_Encrypt(expireHex, encryptKey);

		return emailToken + timeToken;
	}

	/**
	 * 验证重置密码的 token 是否有效
	 * 
	 * @param token
	 * @param email 用户提交用于对比的 email
	 * @return
	 * @throws ServiceException
	 */
	public boolean validResetPasswordByEmail(String token, String email) throws ServiceException {
		String emailToken = token.substring(0, 32), timeToken = token.substring(32, token.length());

		if (!Encode.md5(encryptKey + email).equals(emailToken)) {
			throw new ServiceException("非法 email 账号！ " + email);
		}

		String expireHex = SymmetricCipher.AES_Decrypt(timeToken, encryptKey);
		long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
		double result = cha * 1.0 / (1000 * 60 * 60);

		if (result <= 12) {
			// 合法
		} else {
			throw new ServiceException("该请求已经过期，请重新发起！ ");
		}

		return true;
	}

	public int doUpdate(User user) throws ServiceException {
		LOGGER.info("修改用户信息");

		// if (user.getName() == null) { // 如果没有用户名
		// if (user.getPhone() != null) { // 则使用 user_{phone} 作为用户名
		// user.setName("user_" + user.getPhone());
		// }
		// }
		// try {
		if (user.getPhone() != null && dao.findByPhone(user.getPhone()) != null)
			throw new ServiceException(user.getPhone() + " 手机号码已注册");

		if (user.getName() != null && dao.findByUserName(user.getName()) != null)
			throw new ServiceException(user.getName() + " 用户名已注册");

		if (user.getEmail() != null && dao.findByEmail(user.getEmail()) != null)
			throw new ServiceException(user.getEmail() + " 邮件已注册");
		// } catch (Exception e) {
		// throw new NullPointerException(e.getMessage());
		// }

		return dao.update(user);
	}

	@Override
	public boolean delete(User bean) {
		UserCommonAuthService.dao.deleteByUserId(bean.getId());
		return dao.delete(bean);
	}

	public Attachment_picture updateOrCreateAvatar(long userUId, UploadFileInfo info) throws Exception {
		if (!info.isOk)
			throw new ServiceException("图片上传失败");

		Attachment_pictureService avatarService = new Attachment_pictureServiceImpl();

		Attachment_picture avatar = dao.findAvaterByUserId(userUId);
		boolean isCreate = avatar == null;

		if (isCreate)
			avatar = new Attachment_picture();

		// 获取图片信息
		ImageHelper imgHelper = new ImageHelper(info.fullPath);

		avatar.setOwner(userUId);
		avatar.setName(info.saveFileName);
		avatar.setPath(info.path);
		avatar.setPicWidth(imgHelper.width);
		avatar.setPicHeight(imgHelper.height);
		avatar.setFileSize((int) (imgHelper.file.length() / 1024));
		avatar.setCatalog(Attachment_pictureService.AVATAR);

		if (isCreate) {
			if (avatarService.create(avatar) != null) {
				return avatar;
			} else {
				throw new ServiceException("创建图片记录失败");
			}
		} else {
			if (avatarService.update(avatar) != 0) {
				return avatar;
			} else {
				throw new ServiceException("修改图片记录失败");
			}
		}
	}

}