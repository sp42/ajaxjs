package com.ajaxjs.cms.app.user.service;

import java.util.Date;
import java.util.List;

import com.ajaxjs.cms.app.user.dao.UserDao;
import com.ajaxjs.cms.app.user.model.User;
import com.ajaxjs.cms.app.user.model.UserCommonAuth;
import com.ajaxjs.cms.model.Attachment_picture;
import com.ajaxjs.cms.service.Attachment_pictureService;
import com.ajaxjs.cms.service.Attachment_pictureServiceImpl;
import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.framework.service.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetricCipher;
import com.ajaxjs.util.io.image.ImageUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;

@Bean(value = "UserService", aop = { CommonService.class, GlobalLogAop.class })
public class UserServiceImpl implements UserService {
	private static final LogHelper LOGGER = LogHelper.getLog(UserServiceImpl.class);

	public static UserDao dao = new DaoHandler().bind(UserDao.class);

//	@Resource("User_common_authService") // 指定 service id
	private UserCommonAuthService passwordService = new UserCommonAuthServiceImpl();

	@Override
	public Long create(User user, UserCommonAuth password) throws ServiceException {
		LOGGER.info("用户注册");

		if (checkIfUserPhoneRepeat(user))
			throw new ServiceException(user.getPhone() + "手机号码已注册");

		if (checkIfUserNameRepeat(user))
			throw new ServiceException(user.getName() + "用户名已注册");

		Long userId = dao.create(user);

		user.setId(userId);

		password.setUserId(Integer.parseInt(userId.toString()));

		CommonService.onCreate(password);
		passwordService.create(password);

		return userId;
	}

	@Override
	public boolean checkIfUserNameRepeat(User user) {
		return user.getName() != null && dao.findByUserName(user.getName()) != null;
	}

	@Override
	public boolean checkIfUserNameRepeat(String userName) {
		User user = new User();
		user.setName(userName);
		return checkIfUserNameRepeat(user);
	}

	@Override
	public boolean checkIfUserPhoneRepeat(User user) {
		return user.getPhone() != null && dao.findByPhone(user.getPhone()) != null;
	}

	@Override
	public boolean checkIfUserPhoneRepeat(String phone) {
		User user = new User();
		user.setPhone(phone);
		return checkIfUserPhoneRepeat(user);
	}

	@Override
	public boolean loginByPassword(User user, UserCommonAuth userLoginInfo) throws ServiceException {
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
			throw new ServiceException("用户不存在");

		user.setId(foundUser.getId()); // let outside valued
		user.setName(foundUser.getName());

		UserCommonAuth auth = UserCommonAuthServiceImpl.dao.findByUserId(foundUser.getId());

		if (!auth.getPassword().equalsIgnoreCase(UserCommonAuthServiceImpl.encode(userLoginInfo.getPassword()))) {
			LOGGER.info("密码不正确，数据库密码：{0}, 提交密码 {1}", auth.getPassword(), UserCommonAuthServiceImpl.encode(userLoginInfo.getPassword()));
			return false;
		}

		if (foundUser == null || foundUser.getId() == 0)
			throw new ServiceException("非法用户！");

		long effectedRows = dao.updateLoginInfo(foundUser.getId(), userLoginInfo.getLoginType(), new Date(), MvcRequest.getMvcRequest().getIp());

		if (effectedRows <= 0)
			throw new ServiceException("更新会员登录日志出错");

		LOGGER.info(foundUser.getName() + " 登录成功！");
		return true;
	}

	private static final String encryptKey = "ErZwd#@$#@D32";

	@Override
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

	@Override
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

	@Override
	public User findById(Long id) {
		return dao.findById(id);
	}

	/**
	 * 不能调用该方法
	 */
	@Override
	public Long create(User user) {
		throw new Error("Should not execute this method!");
	}

	@Override
	public int update(User user) throws ServiceException {
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
//		} catch (ServiceException e) {
//			throw new NullPointerException(e.getMessage());
//		}
		
		return dao.update(user);
	}

	@Override
	public boolean delete(User bean) {
		UserCommonAuthServiceImpl.dao.deleteByUserId(bean.getId());
		return dao.delete(bean);
	}
 
	@Override
	public PageResult<User> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "用户";
	}

	@Override
	public String getTableName() {
		return "user";
	}

	@Override
	public Attachment_picture findAvaterByUserId(long userId) {
		return dao.findAvaterByUserId(userId);
	}

	@Override
	public Attachment_picture updateOrCreateAvatar(long userUId, UploadFileInfo info) throws ServiceException {
		if (!info.isOk) {
			throw new ServiceException("图片上传失败");
		}

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

	@Override
	public List<User> findList() {
		// TODO Auto-generated method stub
		return null;
	}

}