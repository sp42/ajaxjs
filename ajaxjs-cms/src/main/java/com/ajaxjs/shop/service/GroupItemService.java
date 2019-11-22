package com.ajaxjs.shop.service;

import java.util.List;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.GroupItem;

@Bean
public class GroupItemService extends BaseService<GroupItem> {

	@TableName(value = "shop_group_item", beanClass = GroupItem.class)
	public static interface GroupItemDao extends IBaseDao<GroupItem> {
		@Select("SELECT i.*, entry.name, f.name AS formatName, f.price AS formatPrice, f.uid AS formatUid, (" + selectCover 
				+ ") AS cover" + " FROM ${tableName} i INNER JOIN shop_goods entry ON entry.id = i.goodsId "
				+ "INNER JOIN shop_goods_format f ON f.id = i.goodsFormatId WHERE i.groupId = ?")
		public List<Map<String, Object>> findDetailByGroupId(Long groupId);
	}

	public GroupItemDao dao = new Repository().bind(GroupItemDao.class);

	{
		setUiName("团购明细");
		setShortName("groupItem");
		setDao(dao);
	}

	public List<GroupItem> findByGroupId(long groupId) {
		return dao.findList(sql -> " WHERE groupId = " + groupId);
	}
}