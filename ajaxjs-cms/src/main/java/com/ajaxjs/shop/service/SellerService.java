package com.ajaxjs.shop.service;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.Seller;

@Bean
public class SellerService extends BaseService<Seller> {

	@TableName(value = "shop_seller", beanClass = Seller.class)
	public static interface SellerDao extends IBaseDao<Seller> {
		@Select("SELECT * FROM ${tableName} WHERE userId = ?")
		public List<Seller> findListByUserId(long userId);

		@Select("SELECT id FROM ${tableName} WHERE isDefault = 1")
		public Long getDefaultAddressId();
	}

	public static SellerDao dao = new Repository().bind(SellerDao.class);

	{
		setUiName("商家");
		setShortName("seller");
		setDao(dao);
	}
}