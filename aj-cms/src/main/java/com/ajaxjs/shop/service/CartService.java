package com.ajaxjs.shop.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.shop.dao.CartDao;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.sql.orm.PageResult;
import com.ajaxjs.sql.orm.Repository;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.ioc.Component;
import com.ajaxjs.util.ioc.Resource;
import com.ajaxjs.web.mvc.controller.MvcRequest;

/**
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Component
public class CartService extends BaseService<Cart> {
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
//		Long groupId = bean.getGroupId();
//		if (groupId != null) {// 增加当前参团人数
//			groupService.addCurrentPerson(groupId);
//		}

		return super.create(bean);
	}

	@Override
	public boolean delete(Cart bean) {
		bean = findById(bean.getId());
//		Long groupId = bean.getGroupId();
//
//		if (groupId != null) {// 增加当前参团人数
//			groupService.downCurrentPerson(groupId);
//		}

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
		List<Cart> carts = findCartListIn(cartIds);
		BigDecimal actualPrice = getActualPrice(carts);
		Map<String, Object> map = new HashMap<>();
		map.put("actualPrice", actualPrice);
		map.put("checkedAddress", "sssssssssssss"); // TODO
		map.put("checkedGoodsList", null);

		return map;
	}

	/**
	 * 计算总价
	 * 
	 * @param carts 要购买的购物车内容
	 * @return 总价
	 */
	public static BigDecimal getActualPrice(List<Cart> carts) {
		BigDecimal actualPrice = BigDecimal.valueOf(0);

		if (CommonUtil.isNull(carts))
			throw new IllegalArgumentException("没有任何购物车或者已支付");

		for (Cart cart : carts) {
			BigDecimal goodsAmount = cart.getPrice().multiply(BigDecimal.valueOf(cart.getGoodsNumber()));
			actualPrice = actualPrice.add(goodsAmount);
		}

		return actualPrice;
	}
}