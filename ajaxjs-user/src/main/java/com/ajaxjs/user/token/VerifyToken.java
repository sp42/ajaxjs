package com.ajaxjs.user.token;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.config.ConfigService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.orm.annotation.Insert;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.user.UserUtil;
import com.ajaxjs.user.model.User;
import com.ajaxjs.user.service.UserService;
import com.ajaxjs.util.Encode;

/**
 * 可验证的 Token，用于忘记密码、验证邮箱
 * 
 * @author Administrator
 *
 */
public class VerifyToken extends BaseService<Map<String, Object>> {

	/**
	 * 验证邮件
	 */
	public static final int EMAIL = 1;

	/**
	 * 忘记密码
	 */
	public static final int FORGET_PASSWORD = 2;

	@TableName("token")
	public static interface VerifyEmailDao extends IBaseDao<Map<String, Object>> {
		@Select("SELECT * FROM ${tableName} WHERE token = ?")
		public Map<String, Object> findByToken(String token);

		@Select("SELECT id FROM ${tableName} WHERE entityUid = ? AND type = ?")
		public Long isExistToken(long entityUid, int type);

		@Insert("INSERT INTO ${tableName} (token, randomStr, createDate, entityUid, type) VALUES (?, ?, ?, ?, ?)")
		public long saveToken(String token, String randromStr, Date now, long entityUid, int type);

	}

	public static VerifyEmailDao dao = new Repository().bind(VerifyEmailDao.class);

	/**
	 * 根据邮件和用户 uid 生成 Token
	 * 
	 * @param email
	 * @param userUid
	 * @return
	 */
	public static String saveTokenWithEmailUser(String email, long userUid, int type) {
		String randromStr = UserUtil.getRandomString(6);
		Date now = new Date();

		String str = userUid + email + randromStr + now.getTime(), token = Encode.md5(str);

		dao.saveToken(token, randromStr, now, userUid, type);

		return token;
	}

	public static void findDel(User user, int type) {
		Long existId = VerifyToken.dao.isExistToken(user.getUid(), type);

		if (existId != null) {
			Map<String, Object> map = new HashMap<>();
			map.put("id", existId);
			VerifyToken.dao.delete(map);
		}
	}

	/**
	 * 默认的 Token 验证所允许的超时时间
	 */
	public static final int TIMEOUT = 1;

	/**
	 * 返回 Token 验证所允许的超时时间
	 * 
	 * @return
	 */
	public static int getTimeout() {
		int c = ConfigService.getValueAsInt("user.tokenTimeout");
		return c == 0 ? TIMEOUT : c;
	}

	public static User verifyToken(String token, int timeout, boolean isVerifyMark) {
		Map<String, Object> data = VerifyToken.dao.findByToken(token);
		Objects.requireNonNull(data, "没有找到该 token: " + token + "。 非法请求！");

		Object date = data.get("createDate");
		long createDate;

		if (date instanceof Date) {
			createDate = ((Date) date).getTime();
		} else
			createDate = (long) date;

		long diff = new Date().getTime() - createDate;

		Long existId = (long) data.get("id");
		if (diff > (timeout * 3600000)) {
			// 删除已超时的 token
			Map<String, Object> map = new HashMap<>();
			map.put("id", existId);
			VerifyToken.dao.delete(map);

			throw new IllegalArgumentException("该 Token 已超时");
		}

		long userUid = (long) data.get("entityUid");
		User user = UserService.dao.findByUid(userUid);
		Objects.requireNonNull(data, "程序异常，没有找到对应的用户，用户 uid 为 " + userUid);

		if (isVerifyMark) {
			int v = user.getVerify();
			if ((VerifyToken.EMAIL & v) == VerifyToken.EMAIL)
				throw new IllegalArgumentException("用户之前已经验证邮件，这次校验无效。");
		}

		// 正式判断
		String email = user.getEmail();
		String str = userUid + email + data.get("randomStr") + createDate, _token = Encode.md5(str);

		if (token.equalsIgnoreCase(_token))
			return user;

		return null;
	}
}
