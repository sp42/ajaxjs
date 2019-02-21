package com.ajaxjs.cms.user.role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean("UserRoleRoleService")
public class RoleService extends BaseService<Map<String, Object>> {
	RoleDao dao = new Repository().bind(RoleDao.class);

	{
		setUiName("用户角色");
		setShortName("user_role");
		setDao(dao);
	}
	
	@TableName(value = "user_role", beanClass = Map.class)
	public static interface RoleDao extends IBaseDao<Map<String, Object>> {
		@Select(value = "SELECT accessKey FROM ${tableName}")
		public Integer[] getExistingPrime();
	}

	@Override
	public Long create(Map<String, Object> bean) {
		Integer[] ep = getExistingPrime();
		int prime;

		if (ep == null || ep.length == 0) {
			prime = 2;
		} else
			prime = getNextPrime(ep);

		bean.put("accessKey", prime);
		return dao.create(bean);
	}

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
		Integer[] p = getPrimeNumber(200);

		for (int i : p) {
			if (i > max)
				return i;
		}

		return 0;
	}

	/**
	 * 求 1 到n 所有质数
	 * 
	 * @param n
	 * @return
	 */
	private static int[] _getPrimeNumber(int n) {
		int[] priArr = new int[n];

		// 质数为大于1的自然数, 故i从2开始
		for (int i = 2; i < n; i++) {
			// isPrime作为当前这个数是否为质数的标记位
			boolean isPrime = true;

			for (int j = 2; j < i; j++) {
				if (i % j == 0) {
					isPrime = false;
					break;
				}
			}

			if (isPrime)
				priArr[i] = i;
		}

		return priArr;
	}

	public static Integer[] getPrimeNumber(int n) {
		List<Integer> list = new ArrayList<>();

		int[] retArr = _getPrimeNumber(n);

		for (int i = 0; i < retArr.length; i++) {
			if (retArr[i] != 0) {
				list.add(retArr[i]);
			}
		}

		Integer[] arr = list.toArray(new Integer[list.size()]);

		return arr;
	}
}