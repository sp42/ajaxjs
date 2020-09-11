package com.ajaxjs.user.profile;

import java.util.function.Function;

import com.ajaxjs.app.attachment.Attachment_picture;
import com.ajaxjs.app.attachment.Attachment_pictureService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.JdbcConnection;
import com.ajaxjs.sql.JdbcReader;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.user.BaseUserService;
import com.ajaxjs.user.User;
import com.ajaxjs.user.password.UserCommonAuthService;
import com.ajaxjs.util.io.ImageHelper;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.web.UploadFileInfo;
import com.ajaxjs.web.mvc.ModelAndView;

@Component
public class ProfileService extends BaseUserService {
	private static final LogHelper LOGGER = LogHelper.getLog(ProfileService.class);

	@Resource
	private UserCommonAuthService passwordService;

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

		return value != null
				&& JdbcReader.queryOne(JdbcConnection.getConnection(), "SELECT * FROM user WHERE " + field + " = ? LIMIT 1", Object.class, value) != null;
	}

	public final static String CATALOG_FIND = "e.catalogId IN ( SELECT id FROM user_role WHERE `path` LIKE ( CONCAT (( SELECT `path` FROM user_role WHERE id = %d ) , '%%')) )";

	public static Function<String, String> setRoleId() {
		Object v = getValue("roleId", int.class);
		/* user_role 表没有 path 字段，不能那样子玩了 */
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
		if (user.getPhone() != null && DAO.findByPhone(user.getPhone()) != null)
			throw new ServiceException(user.getPhone() + " 手机号码已注册");

		if (user.getName() != null && DAO.findByUserName(user.getName()) != null)
			throw new ServiceException(user.getName() + " 用户名已注册");

		if (user.getEmail() != null && DAO.findByEmail(user.getEmail()) != null)
			throw new ServiceException(user.getEmail() + " 邮件已注册");
		// } catch (Exception e) {
		// throw new NullPointerException(e.getMessage());
		// }

		return DAO.update(user);
	}

	@Override
	public boolean delete(User bean) {
		UserCommonAuthService.dao.deleteByUserId(bean.getId());
		return DAO.delete(bean);
	}

	public Attachment_picture updateOrCreateAvatar(long userUId, UploadFileInfo info) throws Exception {
		if (!info.isOk)
			throw new ServiceException("图片上传失败");

		Attachment_pictureService avatarService = new Attachment_pictureService();

		Attachment_picture avatar = DAO.findAvaterByUserId(userUId);
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
	 * 当访问用户会员中心（前台）的时候
	 * 
	 * @param mv
	 */
	public void onUserCenterHome(ModelAndView mv) {
	}

}