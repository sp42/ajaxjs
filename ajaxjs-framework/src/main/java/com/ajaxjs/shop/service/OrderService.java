package com.ajaxjs.shop.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.PageResult;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.mvc.ModelAndView;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.dao.OrderInfoDao;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.user.controller.BaseUserController;
import com.ajaxjs.user.model.UserAddress;
import com.ajaxjs.user.service.UserAddressService;
import com.ajaxjs.util.CommonUtil;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.util.map.JsonHelper;
import com.ajaxjs.weixin.payment.PayConstant;

@Bean
public class OrderService extends BaseService<OrderInfo> implements PayConstant {
	private static final LogHelper LOGGER = LogHelper.getLog(OrderService.class);

	public static OrderInfoDao dao = new Repository().bind(OrderInfoDao.class);

	{
		setUiName("订单");
		setShortName("order");
		setDao(dao);
	}

	@Resource("UserAddressService")
	private UserAddressService addService = new UserAddressService();

	@Resource("CartService")
	private CartService cartService = new CartService();

	@Resource("OrderItemService")
	private OrderItemService orderItemService = new OrderItemService();

	/**
	 * 分页查找
	 * 
	 * @param start
	 * @param limit
	 * @param tradeStatus
	 * @param payStatus
	 * @param orderNo
	 * @return
	 */
	public PageResult<OrderInfo> findPagedList(int start, int limit, int tradeStatus, int payStatus, String orderNo,
			long userId) {
		Function<String, String> sqlHander = BaseService::betweenCreateDate;

		if (tradeStatus != 0)
			sqlHander = sqlHander.andThen(by("tradeStatus", tradeStatus));

		if (payStatus != 0)
			sqlHander = sqlHander.andThen(by("tradeStatus", payStatus));

		if (!CommonUtil.isEmptyString(orderNo))
			sqlHander = sqlHander.andThen(by("orderNo", orderNo));

		if (userId != 0)
			sqlHander = sqlHander.andThen(by("buyerId", userId));

		return findPagedList(start, limit, sqlHander);
	}

	/**
	 * 查找用户交易过的所有订单
	 * 
	 * @param userId 用户 id
	 * @return 订单列表
	 */
	public List<OrderInfo> findByUserId(long userId) {
		return dao.findList(by("buyerId", userId));
	}

	/**
	 * 根据订单号查询单个订单
	 * 
	 * @param orderNo 订单号
	 * @return 单个订单
	 */
	public OrderInfo findByOrderNo(String orderNo) {
		return dao.find(by("orderNo", orderNo));
	}

	/**
	 * 获取订单实体，包含详细的信息
	 * 
	 * @param orderId 订单 id
	 * @return 订单详细信息
	 */
	public Map<String, Object> orderDetail(Long orderId) {
		LOGGER.info("获取订单实体，包含详细的信息");

		OrderInfo info = dao.findById(orderId);
		List<OrderItem> list = dao.findOrderItemListByOrderId(orderId);
		info.setOrderItems(list.toArray(new OrderItem[list.size()])); // 转换为数组

		Map<String, Object> map = new HashMap<>();
		map.put("info", info);
		map.put("TradeStatusDict", ShopConstant.TradeStatus); // UI 也要显示数据字典
		map.put("PayTypeDict", ShopConstant.PAY_TYPE);
		map.put("PayStatusDict", ShopConstant.PayStatus);

		return map;
	}

	/**
	 * 删除订单关联的明细表项
	 * 
	 * @param orderId 订单 id
	 */
	public void deleteOrderItems(long orderId) {
		List<OrderItem> orderItems = dao.findOrderItemListByOrderId_Simple(orderId);

		for (OrderItem orderItem : orderItems)
			orderItemService.delete(orderItem);
	}

	/**
	 * 下单页面
	 * 
	 * @param mv ModelAndView 对象
	 */
	public void showCheckout(ModelAndView mv) {
		// 查询地址列表 放在这里，可节省一次数据库连接
		mv.put("addressList", JsonHelper.toJson(addService.findListByUserId(BaseUserController.getUserId())));
	}

	/**
	 * 生成订单，统一下单
	 * 
	 * @param userId    用户 id
	 * @param addressId 送货地址id
	 * @param cartIds   订单明细由购物车的数据生成
	 * @return 订单
	 */
	public OrderInfo processOrder(long userId, long addressId, String[] cartIds, int payType) {
		LOGGER.info("生成订单信息");

		// 购物车转为订单
		List<Cart> carts = cartService.findCartListIn(cartIds);
		BigDecimal actualPrice = getActualPrice(carts);

		OrderInfo order = new OrderInfo();
		order.setBuyerId(userId);
		order.setTotalPrice(actualPrice);
		order.setOrderPrice(actualPrice); // 当前没有优惠券
		order.setPayType(payType);

		getAddress(order, addressId);

		order.setOrderNo(ShopHelper.getOutterOrderNo()); // 生成外显的订单号

		create(order); // 保存订单

		OrderItem[] orderItems = new OrderItem[carts.size()];
		int i = 0; // TODO: why no i++?
		
		// 生成明细
		for (Cart cart : carts) {
			OrderItem orderItem = new OrderItem();
			orderItem.setGoodsId(cart.getGoodsId());
			orderItem.setGoodsFormatId(cart.getGoodsFormatId());
			orderItem.setGoodsPrice(cart.getPrice());
			orderItem.setGoodsNumber(cart.getGoodsNumber());
			orderItem.setGoodsAmount(actualPrice);
			orderItem.setOrderId(order.getId());
			orderItem.setBuyerId(userId);
			orderItem.setSellerId(cart.getSellerId());
			if (cart.getGroupId() != null)
				orderItem.setGroupId(cart.getGroupId());

			orderItems[i] = orderItem;

			// 创建订单，将购物车的item 变为 order item
			if (orderItemService.create(orderItem) != null) {
				Cart _cart = new Cart();
				_cart.setId(cart.getId());
				cartService.delete(_cart);
			} else {
				LOGGER.warning("不能创建订单 ");
			}
		}

		order.setOrderItems(orderItems);

		return order;
	}

	/**
	 * 当处理完毕订单后执行
	 * 
	 * @param order
	 */
	public void onProcessOrderDone(OrderInfo order) {
	}

	/**
	 * 复制地址
	 * 
	 * @param order
	 * @param addressId
	 */
	private void getAddress(OrderInfo order, long addressId) {
		System.out.println(UserAddressService.AREA_DATA.get("China_AREA", "86", "210000"));
		System.out.println(UserAddressService.AREA_DATA.get("China_AREA", "210000", "210100"));

		UserAddress address = addService.findById(addressId);
		order.setShippingTarget(address.getName());
		order.setShippingPhone(address.getPhone());
		order.setShippingCode(address.getZipCode());

		String p = address.getLocationProvince() + "", c = address.getLocationCity() + "",
				d = address.getLocationDistrict() + "";

		order.setShippingAddress(UserAddressService.AREA_DATA.get("China_AREA", "86", p).toString()
				+ UserAddressService.AREA_DATA.get("China_AREA", p, c)
				+ UserAddressService.AREA_DATA.get("China_AREA", c, d) + address.getAddress());

	}

	/**
	 * 计算总价
	 * 
	 * @param carts 要购买的购物车内容
	 * @return 总价
	 */
	private static BigDecimal getActualPrice(List<Cart> carts) {
		BigDecimal actualPrice = BigDecimal.valueOf(0);

		for (Cart cart : carts) {
			BigDecimal goodsAmount = cart.getPrice().multiply(BigDecimal.valueOf(cart.getGoodsNumber()));
			actualPrice = actualPrice.add(goodsAmount);
		}

		return actualPrice;
	}
}
