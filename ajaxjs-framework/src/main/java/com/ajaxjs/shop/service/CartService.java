package com.ajaxjs.shop.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.controller.MvcRequest;
import com.ajaxjs.shop.dao.CartDao;
import com.ajaxjs.shop.group.GroupService;
import com.ajaxjs.shop.model.Cart;

/**
 * 
 * @author sp42 frank@ajaxjs.com
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
		Function<String, String> b = betweenCreateDate("e.createDate", MvcRequest.getHttpServletRequest());
		return findPagedList(start, limit, userId != 0L ? byUserId(userId).andThen(b) : b);
	}

	/**
	 * 查询 cartIds 购物车
	 * 
	 * @param cartIds 多个购物车 id
	 * @return 多个购物车实体
	 */
	public List<Cart> findCartListIn(String[] cartIds) {
		return dao.findList(in("e.id", cartIds));
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
	
	@Resource("OrderService")
	OrderService orderService;

	/**
	 * 
	 * @param addressId
	 * @param cartIds
	 * @return
	 */
	public Map<String, Object> checkout(long userId, long addressId, String[] cartIds) {
		Map<String, Object> map = new HashMap<>();
		
		orderService.processOrder(userId, addressId, cartIds, 0);

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

		return map;
	}
}