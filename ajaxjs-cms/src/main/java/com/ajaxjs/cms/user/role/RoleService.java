package com.ajaxjs.cms.user.role;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.framework.ServiceException;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;

@Bean("UserRoleService")
public class RoleService extends BaseService<Map<String, Object>> {
	RoleDao dao = new Repository().bind(RoleDao.class);

	{
		setUiName("用户角色");
		setShortName("user_role");
		setDao(dao);
	}

	@TableName(value = "user_role", beanClass = Map.class)
	public static interface RoleDao extends IBaseDao<Map<String, Object>> {
		
		@Select("SELECT * FROM ${tableName} ORDER BY pid ")
		@Override
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
		if(userGroup == null || resId == 0) 
			throw new ServiceException("参数异常");
		
		// sqlite 没有 long，郁闷
		long num;
		Object obj = userGroup.get("accessKey");
		
		if(obj== null) {
			num = 0L;
		} else {
			num = obj instanceof Integer ? ((Integer) userGroup.get("accessKey")).longValue() : (long) userGroup.get("accessKey");
		}

		long newlyResRight = RightConstant.set(num, resId, isEnable);

		Map<String, Object> newlyUserGroup = new HashMap<>();
		newlyUserGroup.put("id", userGroup.get("id"));
		newlyUserGroup.put("accessKey", newlyResRight);
		update(newlyUserGroup);
		
		return newlyResRight;
	}

}