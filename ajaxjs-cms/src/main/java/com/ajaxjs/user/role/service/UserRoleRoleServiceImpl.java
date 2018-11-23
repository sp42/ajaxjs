package com.ajaxjs.user.role.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.ajaxjs.cms.service.aop.CommonService;
import com.ajaxjs.cms.service.aop.GlobalLogAop;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.dao.DaoHandler;
import com.ajaxjs.orm.dao.PageResult;
import com.ajaxjs.user.role.dao.UserRoleRoleDao;

@Bean(value = "UserRoleRoleService", aop = { CommonService.class, GlobalLogAop.class })
public class UserRoleRoleServiceImpl implements UserRoleRoleService {
	UserRoleRoleDao dao = new DaoHandler().bind(UserRoleRoleDao.class);

	@Override
	public Map<String, Object> findById(Integer id) {
		return dao.findById(id);
	}

	/**
	 * 获取当前最大的质数
	 * 
	 * @return 当前最大的质数
	 */
	public Integer[] getExistingPrime() {
		return dao.getExistingPrime();
	}

	@Override
	public Integer create(Map<String, Object> bean) {
		Integer[] ep = getExistingPrime();
		int prime;

		if (ep == null || ep.length == 0) {
			prime = 2;
		} else
			prime = getNextPrime(ep);

		bean.put("accessKey", prime);
		return dao.create(bean);
	}

	@Override
	public int update(Map<String, Object> bean) {
		return dao.update(bean);
	}

	@Override
	public boolean delete(Map<String, Object> bean) {
		return dao.delete(bean);
	}

	@Override
	public PageResult<Map<String, Object>> findPagedList(int start, int limit) {
		return dao.findPagedList(start, limit);
	}

	@Override
	public String getName() {
		return "用户角色";
	}

	@Override
	public String getTableName() {
		return "user_admin_role";
	}

	/**
	 * 生成下一个质数
	 * 
	 * @param existingPrime
	 *            当前最大的质数
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

	@Override
	public List<Map<String, Object>> findList() {
		// TODO Auto-generated method stub
		return null;
	}
}