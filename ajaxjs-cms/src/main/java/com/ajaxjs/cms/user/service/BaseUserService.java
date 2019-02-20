package com.ajaxjs.cms.user.service;

import java.util.Date;
import java.util.List;

import com.ajaxjs.cms.app.attachment.Attachment_picture;
import com.ajaxjs.cms.app.attachment.Attachment_pictureService;
import com.ajaxjs.cms.app.attachment.Attachment_pictureServiceImpl;
import com.ajaxjs.cms.user.User;
import com.ajaxjs.cms.user.UserCommonAuth;
import com.ajaxjs.cms.user.UserLoginLogService.UserLoginLogDao;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;

import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetricCipher;
import com.ajaxjs.util.io.image.ImageUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;

@Bean("UserService")
public class BaseUserService extends BaseService<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(BaseUserService.class);
	
	public UserDao dao = new Repository().bind(UserDao.class);

	public Long create(User user, UserCommonAuth password) {
		LOGGER.info("用户注册");

		if (checkIfUserPhoneRepeat(user))
			throw new Exception(user.getPhone() + "手机号码已注册");

		if (checkIfUserNameRepeat(user))
			throw new Exception(user.getName() + "用户名已注册");

		Long userId = dao.create(user);

		user.setId(userId);

		password.setUserId(Integer.parseInt(userId.toString()));

		CommonService.onCreate(password);
		passwordService.create(password);

		return userId;
	}

	public boolean checkIfUserNameRepeat(User user) {
		return user.getName() != null && dao.findByUserName(user.getName()) != null;
	}

	public boolean checkIfUserNameRepeat(String userName) {
		User user = new User();
		user.setName(userName);
		return checkIfUserNameRepeat(user);
	}

	public boolean checkIfUserPhoneRepeat(User user) {
		return user.getPhone() != null && dao.findByPhone(user.getPhone()) != null;
	}

	public boolean checkIfUserPhoneRepeat(String phone) {
		User user = new User();
		user.setPhone(phone);
		return checkIfUserPhoneRepeat(user);
	}

	public boolean loginByPassword(User user, UserCommonAuth userLoginInfo) {
		User foundUser = null;
		switch (userLoginInfo.getLoginType()) {
		case UserConstant.loginByUserName:
			foundUser = dao.findByUserName(user.getName());
			break;
		case UserConstant.loginByPhoneNumber:
			foundUser = dao.findByPhone(user.getPhone());
			break;
		case UserConstant.loginByEmail:
			foundUser = dao.findByEmail(user.getEmail());
			break;
		default:
			foundUser = null;
		}

		if (foundUser == null || foundUser.getId() == null || foundUser.getId() == 0)
			throw new Exception("用户不存在");

		user.setId(foundUser.getId()); // let outside valued
		user.setName(foundUser.getName());

		UserCommonAuth auth = UserCommonAuthService.dao.findByUserId(foundUser.getId());

		if (!auth.getPassword().equalsIgnoreCase(UserCommonAuthService.encode(userLoginInfo.getPassword()))) {
			LOGGER.info("密码不正确，数据库密码：{0}, 提交密码 {1}", auth.getPassword(), UserCommonAuthService.encode(userLoginInfo.getPassword()));
			return false;
		}

		if (foundUser == null || foundUser.getId() == 0)
			throw new Exception("非法用户！");

		long effectedRows = dao.updateLoginInfo(foundUser.getId(), userLoginInfo.getLoginType(), new Date(), MvcRequest.getMvcRequest().getIp());

		if (effectedRows <= 0)
			throw new Exception("更新会员登录日志出错");

		LOGGER.info(foundUser.getName() + " 登录成功！");
		return true;
	}

	private static final String encryptKey = "ErZwd#@$#@D32";

	public String resetPasswordByEmail(User user) {
		LOGGER.info("重置密码");

		String email = user.getEmail();
		if (email == null)
			throw new Exception("请提交邮件地址");
		user = dao.findByEmail(email);

		if (user == null)
			throw new Exception("该 email：" + email + " 的用户不存在！");

		String expireHex = Long.toHexString(System.currentTimeMillis());
		String emailToken = Encode.md5(encryptKey + email), timeToken = SymmetricCipher.AES_Encrypt(expireHex, encryptKey);

		return emailToken + timeToken;
	}

	public boolean validResetPasswordByEmail(String token, String email) {
		String emailToken = token.substring(0, 32), timeToken = token.substring(32, token.length());

		if (!Encode.md5(encryptKey + email).equals(emailToken)) {
			throw new Exception("非法 email 账号！ " + email);
		}

		String expireHex = SymmetricCipher.AES_Decrypt(timeToken, encryptKey);
		long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
		double result = cha * 1.0 / (1000 * 60 * 60);

		if (result <= 12) {
			// 合法
		} else {
			throw new Exception("该请求已经过期，请重新发起！ ");
		}

		return true;
	}

	public User findById(Long id) {
		return dao.findById(id);
	}

	/**
	 * 不能调用该方法
	 */
	public Long create(User user) {
		throw new Error("Should not execute this method!");
	}

	public int doUpdate(User user) {
		LOGGER.info("修改用户信息");

//		if (user.getName() == null) { // 如果没有用户名
//			if (user.getPhone() != null) { // 则使用 user_{phone} 作为用户名
//				user.setName("user_" + user.getPhone());
//			}
//		}
//		try {
		if (user.getPhone() != null && dao.findByPhone(user.getPhone()) != null)
			throw new Exception(user.getPhone() + " 手机号码已注册");

		if (user.getName() != null && dao.findByUserName(user.getName()) != null)
			throw new Exception(user.getName() + " 用户名已注册");

		if (user.getEmail() != null && dao.findByEmail(user.getEmail()) != null)
			throw new Exception(user.getEmail() + " 邮件已注册");
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
		return dao.findPagedList(start, limit);
	}

	public Attachment_picture findAvaterByUserId(long userId) {
		return dao.findAvaterByUserId(userId);
	}

	public Attachment_picture updateOrCreateAvatar(long userUId, UploadFileInfo info) throws Exception {
		if (!info.isOk) 
			throw new Exception("图片上传失败");
		

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
				throw new Exception("创建图片记录失败");
			}
		} else {
			if (avatarService.update(avatar) != 0) {
				return avatar;
			} else {
				throw new Exception("修改图片记录失败");
			}
		}
	}

}