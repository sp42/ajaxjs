package com.ajaxjs.shop.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.IBaseDao;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.QueryParams;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
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
		public static final String sql = "SELECT c.*, entry.id AS goodsId, entry.name AS goodsName," 
				+ " f.name AS goodsFormat, f.price, " + selectCover
				+ " AS cover FROM ${tableName} c INNER JOIN shop_goods_format f ON c.goodsFormatId = f.id INNER JOIN shop_goods entry ON f.goodsId = entry.id WHERE 1 = 1 ORDER BY c.id DESC";

		@Select(sql)
		public PageResult<Cart> find(int start, int limit, Function<String, String> sqlHandler);

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

	public PageResult<Cart> findCartList(int start, int limit, QueryParams qs) {
		return dao.find(start, limit, QueryParams.initSqlHandler(qs));
	}

	public List<Cart> findCartList(QueryParams qs) {
		return dao.findList(QueryParams.initSqlHandler(qs));
	}

	public List<Cart> findCartListIn(String[] cartIds) {
		return dao.findList(QueryParams.makeQuery(" c.id IN (" + String.join(",", cartIds) + ")"));
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
	
	private GroupService groupService = new GroupService();

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