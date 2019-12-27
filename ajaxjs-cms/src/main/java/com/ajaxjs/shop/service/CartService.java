package com.ajaxjs.shop.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.orm.annotation.Select;
import com.ajaxjs.orm.annotation.TableName;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.model.Group;
import com.ajaxjs.user.model.UserAddress;
import com.ajaxjs.user.service.UserAddressService;
import com.ajaxjs.util.CommonUtil;

/**
 * 
 * @author Frank Cheung
 *
 */
@Bean
public class CartService extends BaseService<Cart> {

	@TableName(value = "shop_cart", beanClass = Cart.class)
	public static interface CartDao extends IBaseDao<Cart> {
		public static final String sql = 
				  "SELECT e.*, g.id AS goodsId, g.name AS goodsName, g.cover, f.name AS goodsFormat, f.price FROM ${tableName} e "
				+ "INNER JOIN shop_goods_format f ON e.goodsFormatId = f.id "
				+ "INNER JOIN shop_goods g ON f.goodsId = g.id" + WHERE_REMARK_ORDER;

		@Select(sql)
		@Override
		public PageResult<Cart> findPagedList(int start, int limit, Function<String, String> sqlHandler);

		@Select(sql)
		@Override
		public List<Cart> findList(Function<String, String> sqlHandler);

		/**
		 * 获取购物车总数
		 * 
		 * @param userId
		 * @return
		 */
		@Select("SELECT COUNT(*) AS count FROM ${tableName} WHERE userId = ?")
		public int getCartListCountByUserId(long userId);
	}

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

	public List<Cart> findCartListIn(String[] cartIds) {
		return dao.findList(setWhere(" e.id IN (" + String.join(",", cartIds) + ")"));
	}

	/**
	 * 根据用户 id 查询其购物车内容
	 * 
	 * @param userId 用户 id
	 * @return
	 */
	public List<Cart> findListByUserId(long userId) {
		return dao.findList(setWhere(" e.userId = " + userId));
	}
	
//	@Resource("UserAddressService")
	private static UserAddressService userAddressService = new UserAddressService();

	/**
	 * SQL 查询是否有数据返回，没有返回 true
	 *
	 */
	@FunctionalInterface
	public static interface Cart2Order {
		public void apply(List<Cart> goodsList, UserAddress address, BigDecimal actualPrice);
	}

	/**
	 * 购物车转为订单
	 * 
	 * @param addressId
	 * @param cartIds
	 * @param fn
	 */
	public void cart2order(long addressId, String[] cartIds, Cart2Order fn) {
		List<Cart> carts = findCartListIn(cartIds);

		BigDecimal actualPrice = BigDecimal.valueOf(0); // 计算总价
		for (Cart cart : carts) {
			BigDecimal goodsAmount = cart.getPrice().multiply(BigDecimal.valueOf(cart.getGoodsNumber()));
			actualPrice = actualPrice.add(goodsAmount);
		}

		UserAddress address = userAddressService.findById(addressId);
		fn.apply(carts, address, actualPrice);
	}
	
	@Resource("GroupService")
	private GroupService groupService;

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
	public Map<String, Object> checkout(long addressId, String[] cartIds) {
		Map<String, Object> map = new HashMap<>();

		cart2order(addressId, cartIds, (goodsList, address, actualPrice) -> {
			List<Group> g = groupService.checkCanGroup(goodsList);
			
			if(CommonUtil.isNull(g)) {
				map.put("actualPrice", actualPrice);
				map.put("checkedAddress", address);
				map.put("checkedGoodsList", goodsList);
			} else {
				map.put("noPassGroup", g);
			}
		});


		return map;
	}
}