package com.ajaxjs.shop.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.cms.app.attachment.Attachment_pictureService;
import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.model.Group;

@Bean
public class GroupService extends BaseService<Group> {

	@TableName(value = "shop_group", beanClass = Group.class)
	public static interface GroupDao extends IBaseDao<Group> {
		@Select(value = "SELECT entry.name, entry.subTitle, entry.coverPrice, entry.titlePrice, entry.sellerId, g.*, (" + selectCover
				+ ") AS cover FROM shop_goods entry INNER JOIN ${tableName} g ON g.goodsId = entry.id WHERE 1 = 1", countSql = "SELECT COUNT(*) AS count FROM shop_goods entry INNER JOIN ${tableName} g ON g.goodsId = entry.id WHERE 1 = 1")
		public PageResult<Group> findPagedList_Cover(int start, int limit, Function<String, String> sqlHandler);

		@Select("SELECT entry.*, g.*, entry.uid, entry.id AS goodsId, (" + selectCover + ") AS cover FROM shop.shop_goods entry INNER JOIN ${tableName} g ON g.goodsId = entry.id WHERE g.id = ?")
		@Override
		public Group findById(Long id);
		
		@Select("SELECT e.*, g.name, g.sellerId FROM ${tableName} e INNER JOIN shop_goods g ON e.goodsId = g.id")
		public List<Group> findList(Function<String, String> doSql);
	}

	public GroupDao dao = new Repository().bind(GroupDao.class);

	{
		setUiName("团购");
		setShortName("simple-group");
		setDao(dao);
	}

	public boolean addCurrentPerson(Long groupId) {
		Group group = findById(groupId), _group;
		int c = group.getCurrentPerson() == null ? 0 : group.getCurrentPerson();

		_group = new Group();
		_group.setId(group.getId());
		_group.setCurrentPerson(++c);
		return update(_group) > 0;
	}

	public boolean downCurrentPerson(Long groupId) {
		Group group = findById(groupId), _group;
		int c = group.getCurrentPerson() == null ? 0 : group.getCurrentPerson();

		_group = new Group();
		_group.setId(group.getId());
		_group.setCurrentPerson(--c);
		return update(_group) > 0;
	}

//	@Override
//	public Group findById(Long id) {
//		return dao.findById(id);
//	}

	@Resource("GoodsFormatService")
	private GoodsFormatService goodsFormatService;

	@Resource("Attachment_pictureService")
	private Attachment_pictureService pictureService;

	@Resource("ShopBookmarkService")
	private ShopBookmarkService bookmarkService;

	@Resource("CartService")
	private CartService shopCartService;

	public Map<String, Object> getGroupDetail(long groupId, long userId) {
		Group group = dao.findById(groupId);
		Map<String, Object> map = new HashMap<>();

		map.put("info", group);
		map.put("formats", goodsFormatService.findByGoodsId(group.getGoodsId()));
		map.put("pics", pictureService.findAttachmentPictureByOwner(group.getUid()));
		map.put("userHasCollect", userId == 0L ? false : ShopBookmarkService.userHasCollect(userId, groupId, ShopConstant.ENTRY_GROUP));
		map.put("cartGoodsCount", userId == 0L ? 0 : CartService.dao.getCartListCountByUserId(userId));

		return map;
	}

	/**
	 * 是否达到成团条件
	 * 
	 * @param goodsList
	 */
	public List<Group> checkCanGroup(List<Cart> goodsList) {
		List<String> groupsIds = new ArrayList<>();

		for (Cart cart : goodsList) {
			Long groupId = cart.getGroupId();
			if (groupId != null && groupId != 0L) {
				groupsIds.add(groupId + "");
			}
		}

		if(groupsIds.size() >0 ) {			
			String ids = String.join(",", groupsIds);
			
			List<Group> list = dao.findList(addWhere("e.id IN (" + ids + ")")), noPassGroup = new ArrayList<>();
			for(Group group : list) {
				if(group.getCurrentPerson() < group.getMinimumPerson()) {
					noPassGroup.add(group);
				}
			}
			
			return noPassGroup;
		}
		
		return null;
	}
}