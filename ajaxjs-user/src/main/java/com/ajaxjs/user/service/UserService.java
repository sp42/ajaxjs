package com.ajaxjs.user.service;

import java.util.Date;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.app.attachment.Attachment_pictureService;
import com.ajaxjs.cms.app.attachment.Attachment_pictureServiceImpl;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserCommonAuth;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetricCipher;
import com.ajaxjs.util.io.image.ImageUtil;
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
	 * @param user
	 * @param password
	 * @return
	 * @throws ServiceException
	 */
	public Long register(User user, UserCommonAuth password) throws ServiceException {
		LOGGER.info("用户注册");

		if (user.getPhone() != null && checkIfUserPhoneRepeat(user))
			throw new ServiceException(user.getPhone() + "手机号码已注册");

		if (user.getName() != null && checkIfUserNameRepeat(user))
			throw new ServiceException(user.getName() + "用户名已注册");

		Long userId = create(user);
		user.setId(userId);

		password.setUserId(userId);
		passwordService.create(password);

		return userId;
	}

	/**
	 * 检查用户名是否重复
	 * 
	 * @return 用户名是否重复
	 */
	public boolean checkIfUserNameRepeat(User user) {
		return user.getName() != null && dao.findByUserName(user.getName()) != null;
	}

	/**
	 * 检查用户名是否重复
	 * 
	 * @return 用户名是否重复
	 */
	public boolean checkIfUserNameRepeat(String userName) {
		User user = new User();
		user.setName(userName);
		return checkIfUserNameRepeat(user);
	}

	/**
	 * 检查用户手机是否重复
	 * 
	 * @return 手机是否重复 true=重複
	 */
	public boolean checkIfUserPhoneRepeat(User user) {
		return user.getPhone() != null && dao.findByPhone(user.getPhone()) != null;
	}

	/**
	 * 检查用户手机是否重复
	 * 
	 * @return 手机是否重复
	 */
	public boolean checkIfUserPhoneRepeat(String phone) {
		User user = new User();
		user.setPhone(phone);
		return checkIfUserPhoneRepeat(user);
	}

	public boolean loginByPassword(User user, UserCommonAuth userLoginInfo) throws ServiceException {
		User foundUser = null;

		if (user.getName() != null) {
			foundUser = dao.findByUserName(user.getName());
		} else if ((user.getPhone() != null)) {
			foundUser = dao.findByPhone(user.getPhone());
		} else if (user.getEmail() != null) {
			foundUser = dao.findByEmail(user.getEmail());
		}

		if (foundUser == null || foundUser.getId() == null || foundUser.getId() == 0)
			throw new ServiceException("用户不存在");

		user.setId(foundUser.getId()); // let outside valued
		user.setName(foundUser.getName());

		UserCommonAuth auth = UserCommonAuthService.dao.findByUserId(foundUser.getId());

		if (auth == null) {
			ServiceException e = new ServiceException("系統異常，用戶 " + foundUser.getId() + " 沒有對應的密碼記錄");
			LOGGER.warning(e);
			return false;
		}

		if (!auth.getPassword().equalsIgnoreCase(UserCommonAuthService.encode(userLoginInfo.getPassword()))) {
			LOGGER.info("密码不正确，数据库密码：{0}, 提交密码 {1}", auth.getPassword(), UserCommonAuthService.encode(userLoginInfo.getPassword()));
			return false;
		}

		if (foundUser == null || foundUser.getId() == 0)
			throw new ServiceException("非法用户！");

		LOGGER.info(foundUser.getName() + " 登录成功！");
		return true;
	}

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
		String emailToken = Encode.md5(encryptKey + email), timeToken = SymmetricCipher.AES_Encrypt(expireHex, encryptKey);

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

	public User findById(Long id) {
		return dao.findById(id);
	}

	public int doUpdate(User user) throws ServiceException {
		LOGGER.info("修改用户信息");

//		if (user.getName() == null) { // 如果没有用户名
//			if (user.getPhone() != null) { // 则使用 user_{phone} 作为用户名
//				user.setName("user_" + user.getPhone());
//			}
//		}
//		try {
		if (user.getPhone() != null && dao.findByPhone(user.getPhone()) != null)
			throw new ServiceException(user.getPhone() + " 手机号码已注册");

		if (user.getName() != null && dao.findByUserName(user.getName()) != null)
			throw new ServiceException(user.getName() + " 用户名已注册");

		if (user.getEmail() != null && dao.findByEmail(user.getEmail()) != null)
			throw new ServiceException(user.getEmail() + " 邮件已注册");
//		} catch (Exception e) {
//			throw new NullPointerException(e.getMessage());
//		}

		return dao.update(user);
	}

	@Override
	public boolean delete(User bean) {
		UserCommonAuthService.dao.deleteByUserId(bean.getId());
		return dao.delete(bean);
	}

	@Override
	public PageResult<User> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit, null);
	}

	public Attachment_picture findAvaterByUserId(long userId) {
		return dao.findAvaterByUserId(userId);
	}

	public Attachment_picture updateOrCreateAvatar(long userUId, UploadFileInfo info) throws Exception {
		if (!info.isOk)
			throw new ServiceException("图片上传失败");

		Attachment_pictureService avatarService = new Attachment_pictureServiceImpl();

		Attachment_picture avatar = findAvaterByUserId(userUId);
		boolean isCreate = avatar == null;

		if (isCreate) {
			avatar = new Attachment_picture();
		}

		// 获取图片信息
		ImageUtil img = new ImageUtil().setFilePath(info.fullPath).getSize();

		avatar.setOwner(userUId);
		avatar.setName(info.saveFileName);
		avatar.setPath(info.path);
		avatar.setPicWidth(img.getWidth());
		avatar.setPicHeight(img.getHeight());
		avatar.setFileSize((int) (img.getFile().length() / 1024));
		avatar.setCatelog(Attachment_pictureService.AVATAR);

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