package com.ajaxjs.user.profile;

import java.util.Date;
import java.util.function.Function;

import com.ajaxjs.app.attachment.Attachment_picture;
import com.ajaxjs.app.attachment.Attachment_pictureService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcReader;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.user.User;
import com.ajaxjs.user.UserDao;
import com.ajaxjs.user.password.UserCommonAuth;
import com.ajaxjs.user.password.UserCommonAuthService;
import com.ajaxjs.util.Encode;
import com.ajaxjs.util.cryptography.SymmetriCipher;
import com.ajaxjs.util.io.ImageHelper;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;

@Component("UserService")
public class ProfileService extends BaseService<User> {
	private static final LogHelper LOGGER = LogHelper.getLog(ProfileService.class);

	public static UserDao dao = new Repository().bind(UserDao.class);

	{
		setUiName("用户");
		setShortName("user");
		setDao(dao);
	}

	@Resource
	private UserCommonAuthService passwordService;

	public UserCommonAuthService getPasswordService() {
		return passwordService;
	}

	public void setPasswordService(UserCommonAuthService passwordService) {
		this.passwordService = passwordService;
	}


	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field 数据库里面的字段名称
	 * @param value 欲检查的值
	 * @param type  提示的类型
	 * @return true=值重复
	 * @throws ServiceException
	 */
	public static boolean checkIfRepeated(String field, String value, String type) throws ServiceException {
		if (value != null && checkIfRepeated(field, value))
			throw new ServiceException(type + " " + value + " 已注册");

		return false;
	}

	/**
	 * 检查某个值是否已经存在一样的值
	 * 
	 * @param field 数据库里面的字段名称
	 * @param value 欲检查的值
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
				timeToken = SymmetriCipher.AES_Encrypt(expireHex, encryptKey);

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

		String expireHex = SymmetriCipher.AES_Decrypt(timeToken, encryptKey);
		long cha = new Date().getTime() - Long.parseLong(expireHex, 16);
		double result = cha * 1.0 / (1000 * 60 * 60);

		if (result <= 12) {
			// 合法
		} else {
			throw new ServiceException("该请求已经过期，请重新发起！ ");
		}

		return true;
	}
	
	public final static String CATALOG_FIND = "e.catalogId IN ( SELECT id FROM user_role WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM user_role WHERE id = %d ) , '%%')) )";
	
	public static Function<String, String> setRoleId() {
		Object v = getValue("roleId", int.class);
		/* user_role 表没有 path 字段，不能那样子玩了  */
//		return v == null ? setWhere(null) : setWhere(String.format(CATALOG_FIND, (int) v));
		return v == null ? setWhere(null) : by("roleId", v);
	}
	
	@Override
	public PageResult<User> findPagedList(int start, int limit) {
		return super.findPagedList(start, limit, setRoleId().andThen(BaseService::betweenCreateDate));
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

		Attachment_pictureService avatarService = new Attachment_pictureService();

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
		avatar.setCatalogId(Attachment_pictureService.AVATAR);

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
	
	/**
	 * 按实体 userId 查找的高阶函数
	 * 
	 * @param uid userId
	 * @return SQL 处理器
	 */
	public static Function<String, String> byUserId(long userId) {
		return by("userId", userId);
	}

	public static Function<String, String> byUserId = by("userId", long.class, "userId");
}