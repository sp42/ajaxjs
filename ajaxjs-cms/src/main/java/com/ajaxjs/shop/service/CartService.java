package com.ajaxjs.shop.service;

import java.util.List;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.shop.dao.CartDao;
import com.ajaxjs.shop.dep.GroupService;
import com.ajaxjs.shop.model.Cart;

/**
 * 
 * @author Frank Cheung
 *
 */
@Bean
public class CartService extends BaseService<Cart> {
	@Resource("GroupService")
	private GroupService groupService;
	
	public static CartDao dao = new Repository().bind(CartDao.class);

	{
		setDao(dao);
		setShortName("cart");
		setUiName("购物车");
	}

	public PageResult<Cart> findPagedList(int start, int limit, long userId) {
		return dao.findPagedList(start, limit,
				userId != 0L ? setWhere(" e.userId = " + userId).andThen(BaseService::betweenCreateDate) : BaseService::betweenCreateDate);
	}

	@Override
	public List<Cart> findList() {
		return dao.findList(BaseService::betweenCreateDate);
	}

	/**
	 * 查询 cartIds 购物车
	 * 
	 * @param cartIds 多个购物车 id
	 * @return 多个购物车实体
	 */
	public List<Cart> findCartListIn(String[] cartIds) {
		return dao.findList(setWhere(" e.id IN (" + String.join(",", cartIds) + ")"));
	}

	/**
	 * 根据用户 id 查询其购物车内容
	 * 
	 * @param userId 用户 id
	 * @return 用户 id 其购物车内容
	 */
	public List<Cart> findListByUserId(long userId) {
		return dao.findList(byUserId(userId));
	}

	@Override
	public Long create(Cart bean) {
		Long groupId = bean.getGroupId();
		if (groupId != null) {// 增加当前参团人数
			groupService.addCurrentPerson(groupId);
		}

		return super.create(bean);
	}

	@Override
	public boolean delete(Cart bean) {
		bean = findById(bean.getId());
		Long groupId = bean.getGroupId();

		if (groupId != null) {// 增加当前参团人数
			groupService.downCurrentPerson(groupId);
		}

		return super.delete(bean);
	}

	/**
	 * 
	 * @param addressId
	 * @param cartIds
	 * @return
	 */
//	public Map<String, Object> checkout(long addressId, String[] cartIds) {
//		Map<String, Object> map = new HashMap<>();
//
//		cart2order(addressId, cartIds, (goodsList, address, actualPrice) -> {
//			List<Group> g = groupService.checkCanGroup(goodsList);
//
//			if (CommonUtil.isNull(g)) {
//				map.put("actualPrice", actualPrice);
//				map.put("checkedAddress", address);
//				map.put("checkedGoodsList", goodsList);
//			} else {
//				map.put("noPassGroup", g);
//			}
//		});
//
//		return map;
//	}
}