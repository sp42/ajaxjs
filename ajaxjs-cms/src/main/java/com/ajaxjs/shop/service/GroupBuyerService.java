package com.ajaxjs.shop.service;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.GroupBuyer;

@Bean
public class GroupBuyerService extends BaseService<GroupBuyer> {
	@TableName(value = "shop_group_buyer", beanClass = GroupBuyer.class)
	public static interface GroupBuyerDao extends IBaseDao<GroupBuyer> {
	}

	GroupBuyerDao dao = new Repository().bind(GroupBuyerDao.class);

	{
		setUiName("团购买家记录");
		setShortName("groupBuyer");
		setDao(dao);
	}
}