package com.ajaxjs.user.role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.web.mvc.MvcRequest;

@Component("UserRoleService")
public class RoleService extends BaseService<Map<String, Object>> implements RightConstant {
	public static RoleDao dao = new Repository().bind(RoleDao.class);

	{
		setUiName("用户角色");
		setShortName("user_role");
		setDao(dao);
	}

	@TableName(value = "user_role", beanClass = Map.class)
	public static interface RoleDao extends IBaseDao<Map<String, Object>> {

		@Select("SELECT * FROM ${tableName} ORDER BY pid ")
		public List<Map<String, Object>> findList();

		@Select("SELECT accessKey FROM ${tableName}")
		public Integer[] getExistingPrime();
	}

	/**
	 * 
	 * @param userGroupId
	 * @param resId
	 * @param isEnable
	 * @return
	 * @throws ServiceException
	 */
	public long updateResourceRightValue(long userGroupId, int resId, boolean isEnable) throws ServiceException {
		Map<String, Object> userGroup = findById(userGroupId);
		if (userGroup == null || resId == 0)
			throw new ServiceException("参数异常");

		// sqlite 没有 long，郁闷
		long num;
		Object obj = userGroup.get("accessKey");

		if (obj == null) {
			num = 0L;
		} else {
			num = obj instanceof Integer ? ((Integer) userGroup.get("accessKey")).longValue() : (long) userGroup.get("accessKey");
		}

		long newlyResRight = set(num, resId, isEnable);

		Map<String, Object> newlyUserGroup = new HashMap<>();
		newlyUserGroup.put("id", userGroup.get("id"));
		newlyUserGroup.put("accessKey", newlyResRight);
		update(newlyUserGroup);

		return newlyResRight;
	}

	/**
	 * 查询 num 的第 pos 位权限值
	 * 
	 * @param num 总权限值
	 * @param pos 从右数起第 pos 位,从 0开 始
	 * @return 第 pos 位为 1 时，返回 true，否则返回 false
	 */
	public static boolean check(long num, int pos) {
		num >>>= pos;// 右移 X位
		return (num & 1) == 1;
	}

	/**
	 * 将 num 的第 pos 位设置为 v
	 * 
	 * @param num 权限值
	 * @param pos 从右数起第 pos 位,从 0 开始
	 * @param v   值
	 */
	public static long set(long num, int pos, boolean v) {
		boolean old = check(num, pos);// 原权限

		if (v) {// 期望改为无权限
			if (!old) // 原来有权限
				num = num + (1L << pos);// 将第 pos 位设置为 1

		} else {// 期望改为有权限
			if (old) // 原来无权限
				num = num - (1L << pos);// 将第 pos 位设置为 0
		}

		return num;
	}

	/**
	 * 调试方法，将 long 转为二进制，并输出
	 * 
	 * @param num
	 */
	public static void printBinary(long num) {
		long reLong = num;
		// 得到64位值
		byte[] buf = new byte[64];
		int pos = 64;

		do {
			buf[--pos] = (byte) (reLong & 1);
			reLong >>>= 1;// 右移一位，相当除以2
		} while (reLong != 0);

		// print
		for (int i = 0; i < buf.length; i++)
			System.out.print(buf[i]);

//		LOGGER.info("-->" + num);
	}

	/**
	 * 
	 * @param pos
	 * @return
	 */
	public static boolean check(int pos) {
		HttpServletRequest request = MvcRequest.getHttpServletRequest();
		Objects.requireNonNull(request);

		return check(request.getSession(), pos);
	}

	public static boolean check(HttpSession s, int pos) {
		Object privilegeTotal = s.getAttribute("privilegeTotal");

		if (privilegeTotal == null)
			return false;

		return check((long) privilegeTotal, pos);
	}

	public static boolean simple8421(int total, int pos) {
		if (total == 0)
			return false;

		return (total & pos) == pos;
	}
}