package com.ajaxjs.user.role;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean("UserRoleService")
public class RoleService extends BaseService<Map<String, Object>> {
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

//	@Override
//	public Long create(Map<String, Object> bean) {
//		Integer[] ep = getExistingPrime();
//		int prime;
//
//		if (ep == null || ep.length == 0) {
//			prime = 2;
//		} else
//			prime = getNextPrime(ep);
//
//		bean.put("accessKey", prime);
//		return super.create(bean);
//	}

	/**
	 * 获取当前最大的质数
	 * 
	 * @return 当前最大的质数
	 */
	public Integer[] getExistingPrime() {
		return dao.getExistingPrime();
	}

	/**
	 * 生成下一个质数
	 * 
	 * @param existingPrime 当前最大的质数
	 * @return
	 */
	public static int getNextPrime(Integer[] existingPrime) {
		int max = Collections.max(Arrays.asList(existingPrime));
		Integer[] p = RoleUtil.getPrimeNumber(200);

		for (int i : p) {
			if (i > max)
				return i;
		}

		return 0;
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
			num = obj instanceof Integer ? ((Integer) userGroup.get("accessKey")).longValue()
					: (long) userGroup.get("accessKey");
		}

		long newlyResRight = set(num, resId, isEnable);

		Map<String, Object> newlyUserGroup = new HashMap<>();
		newlyUserGroup.put("id", userGroup.get("id"));
		newlyUserGroup.put("accessKey", newlyResRight);
		update(newlyUserGroup);

		return newlyResRight;
	}

	/**
	 * 查询num的第pos位权限值
	 * 
	 * @param num 总权限值
	 * @param pos 从右数起第pos位,从0开始
	 * @return 第pos位为1时，返回true，否则返回false
	 */
	public static boolean check(long num, int pos) {
		num >>>= pos;// 右移X位
		return (num & 1) == 1;
	}

	public static boolean simple8421(int num, int pos) {
		return (num & pos) == pos;
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
		Objects.requireNonNull(s);
		Object _privilegeTotal = s.getAttribute("privilegeTotal");
		if (_privilegeTotal == null)
			return false;
		
		long privilegeTotal = (long) _privilegeTotal;

		return check(privilegeTotal, pos);
	}

	/**
	 * 将num的第pos位设置为v
	 * 
	 * @param num 权限值
	 * @param pos 从右数起第pos位,从0开始
	 * @param v   值
	 */
	public static long set(long num, int pos, boolean v) {
		boolean old = check(num, pos);// 原权限

		if (v) {// 期望改为无权限
			if (!old) {// 原来有权限
				num = num + (1L << pos);// 将第pos位设置为1
			}
		} else {// 期望改为有权限
			if (old) {// 原来无权限
				num = num - (1L << pos);// 将第pos位设置为0
			}
		}

		return num;
	}

	/**
	 * 调试方法，将long转为二进制，并输出
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
		for (int i = 0; i < buf.length; i++) {
			System.out.print(buf[i]);
		}
		System.out.print("-->" + num + "\r\n");
	}
}