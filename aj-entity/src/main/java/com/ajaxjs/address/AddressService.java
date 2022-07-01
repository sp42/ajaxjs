package com.ajaxjs.address;

import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.sql.annotation.Select;
import com.ajaxjs.sql.annotation.TableName;
import com.ajaxjs.sql.orm.IBaseDao;
import com.ajaxjs.sql.orm.Repository;

@Component
public class AddressService extends BaseService<Address> {

	@TableName(value = "entity_address", beanClass = Address.class)
	public static interface UserAddressDao extends IBaseDao<Address> {
		@Select("SELECT a.*, u.name AS userIdName, u.username FROM ${tableName} a LEFT JOIN user u ON u.id = a.userId WHERE 1 = 1 ORDER BY id DESC")
		public PageResult<Address> findPagedList(int start, int limit, Function<String, String> doSql);

		@Select("SELECT id FROM ${tableName} WHERE isDefault = 1")
		public Long getDefaultAddressId();
	}

	public static UserAddressDao DAO = new Repository().bind(UserAddressDao.class);

	{
		setDao(DAO);
	}

	/**
	 * 默认地址是唯一的，如果已经有默认地址，取消其默认值
	 * 
	 * @param bean
	 */
	private static void checkIfExist(Address bean) {
		if (bean.getIsDefault() != null && bean.getIsDefault()) {
			Long defaultId = DAO.getDefaultAddressId();

			if (defaultId != null && defaultId != bean.getId()) { // 已经有默认地址
				Address oldDefault = new Address();
				oldDefault.setId(defaultId);
				oldDefault.setIsDefault(false);
				DAO.update(oldDefault);
			}
		}
	}

	public List<Address> findListByUserId(long userId) {
		return findList(by("userId", userId));
	}

	@Override
	public Long create(Address bean) {
		checkIfExist(bean);
		return super.create(bean);
	}

	@Override
	public int update(Address bean) {
		checkIfExist(bean);
		return super.update(bean);
	}
}