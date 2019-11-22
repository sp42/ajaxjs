package com.ajaxjs.shop.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.shop.ShopConstant;
import com.ajaxjs.shop.ShopHelper;
import com.ajaxjs.shop.model.Cart;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.model.OrderItem;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.payment.PayConstant;
import com.ajaxjs.weixin.payment.PaySignatures;
import com.ajaxjs.weixin.payment.PaymentNotification;
import com.ajaxjs.weixin.payment.PerpayInfo;
import com.ajaxjs.weixin.payment.PerpayReturn;
import com.ajaxjs.weixin.payment.WxPay;

/**
 * 订单核心业务
 * 
 * @author Frank Cheung
 *
 */
@Bean
public class OrderInfoService extends BaseService<OrderInfo> implements PayConstant {
	public static OrderInfoDao dao = new Repository().bind(OrderInfoDao.class);

	public static final LogHelper LOGGER = LogHelper.getLog(OrderInfoService.class);

	{
		setUiName("订单");
		setShortName("order");
		setDao(dao);
	}

	/**
	 * 查找用户交易过的所有订单
	 * 
	 * @param userId 用户 id
	 * @return 订单列表
	 */
	public List<OrderInfo> findOrderListByUserId(long userId) {
		return dao.findList(addWhere("buyerId = " + userId));
	}

	/**
	 * 根据订单号查询单个订单
	 * 
	 * @param orderNo 订单号
	 * @return 单个订单
	 */
	public OrderInfo findOrderByOrderNo(String orderNo) {
		return dao.find(addWhere("orderNo ='" + orderNo + "'"));
	}

	/**
	 * 生成订单并支付
	 * 
	 * @param userId 用户 id
	 * @param addressId 送货地址id
	 * @param cartIds 订单明细由购物车的数据生成
	 * @return
	 */
	public Map<String, ?> createOrderAndPay(long userId, long addressId, String[] cartIds, String ip) {
		OrderInfo orderInfo = submitOrder(userId, addressId, cartIds);
		return wxPay(userId, orderInfo, ip);
	}

	public Map<String, ?> pay(long userId, long orderId, String ip) {
		return wxPay(userId, dao.findById(orderId), ip);
	}

	/**
	 * 核心支付方法
	 * 
	 * @param userId
	 * @param orderInfo
	 * @param ip
	 * @return
	 */
	static Map<String, ?> wxPay(long userId, OrderInfo orderInfo, String ip) {
		String openId = dao.findUserOpenId(userId);	 // 小程序内调用登录接口，获取到用户的 openid
		
		// 预付单
		PerpayInfo perpayInfo = new PerpayInfo();
		perpayInfo.setOpenid(openId);
		perpayInfo.setBody("集结号-网超");
		perpayInfo.setIp(ip);

//		Map<String, String> map = WxPay.unifiedOrder(orderInfo, perpayInfo);
//		PerpayReturn result = WxPay.sendUnifiedOrder(map); 

		PerpayReturn result = WxPay.sendUnifiedOrder(orderInfo, perpayInfo);// 商户 server 调用支付统一下单
		
		if (result.isSuccess()) {
			LOGGER.info("获取 perpayid 成功！{0}", result.getPrepay_id());
//			Map<String, String> r = PaySignatures.getPayParam(result.getPrepay_id(), map.get("nonce_str")); // 商户 server 调用再次签名
			Map<String, String> r = PaySignatures.getPayParam(result.getPrepay_id(), result.getNonce_str()); // 商户 server 调用再次签名
			r.put("orderInfoId", orderInfo.getId() + ""); // 新订单 id
			
			return r; // 将此 map 返回给小程序客户端，让它来调起支付界面
		} else {
			LOGGER.warning("获取 perpayid 失败！错误代码：{0}，错误信息：{1}。", result.getError_code(), result.getReturn_msg());
			return new HashMap<String, Object>() {
				private static final long serialVersionUID = 1L;
				{
					put("isOk", false);
					put("msg", result.getReturn_msg());
				}
			};
		}
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
		map.put("PayTypeDict", ShopConstant.PayType);
		map.put("PayStatusDict", ShopConstant.PayStatus);

		return map;
	}

//	@Resource("OrderItemService")
	private OrderItemService orderItemService = new OrderItemService();

//	@Resource("CartService")
	private CartService cartService = new CartService();


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
	 * 生成订单，统一下单
	 * 
	 * @param userId 用户 id
	 * @param addressId 送货地址id
	 * @param cartIds 订单明细由购物车的数据生成
	 * @return
	 */
	public OrderInfo submitOrder(long userId, long addressId, String[] cartIds) {
		LOGGER.info("生成订单信息");

		OrderInfo order = new OrderInfo();
		order.setBuyerId(userId);

		cartService.cart2order(addressId, cartIds, (cartList, address, actualPrice) -> {
			order.setTotalPrice(actualPrice);
			order.setOrderPrice(actualPrice); // 当前没有优惠券

			// 复制地址
			order.setShippingTarget(address.getName());
			order.setShippingPhone(address.getPhone());
			order.setShippingCode(address.getZopCode());
			order.setShippingAddress(address.getProvince() + address.getCity() + address.getDistrict() + address.getAddress());

			order.setOrderNo(ShopHelper.getOutterOrderNo()); // 生成外显的订单号

			create(order); // 保存订单

			OrderItem[] orderItems = new OrderItem[cartList.size()];
			int i = 0; // TODO: why no i++?

			// 生成明细
			for (Cart cart : cartList) {
				OrderItem orderItem = new OrderItem();
				orderItem.setGoodsId(cart.getGoodsId());
				orderItem.setGoodsFormatId(cart.getGoodsFormatId());
				orderItem.setGoodsPrice(cart.getPrice());
				orderItem.setGoodsNumber(cart.getGoodsNumber());
				orderItem.setGoodsAmount(actualPrice);
				orderItem.setOrderId(order.getId());
				orderItem.setBuyerId(userId);
				orderItem.setSellerId(cart.getSellerId());
				if(cart.getGroupId() != null)
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
		});

		return order;
	}

	/**
	 * 处理支付通知的异步回调，控制器必须提供一个接口调用该服务方法
	 * 
	 * @param perpayReturn
	 * @param responseResult
	 * @return
	 */
	public Map<String, String> payNotification(PaymentNotification perpayReturn, Map<String, String> responseResult) {
		LOGGER.info("处理支付通知的异步回调");
		boolean isOk = false;
		String msg = "UNKONW";

		if (perpayReturn.isSuccess()) {
			// 验证签名是否正确
			String toCheck = PaySignatures.generateSignature(perpayReturn.getData(), PaySignatures.getMchSecretId());

			if (perpayReturn.getSign().equals(toCheck)) {
				String totalFee = perpayReturn.getTotal_fee(), orderNo = perpayReturn.getOut_trade_no();

				OrderInfo order = findOrderByOrderNo(orderNo);
				Objects.requireNonNull(order, "订单 " + orderNo + " 不存在");

				if (totalFee.endsWith(ShopHelper.toCent(order.getTotalPrice()))) {// 收到的金额和订单的金额是否吻合？
					OrderInfo updateBill = new OrderInfo();
					updateBill.setId(order.getId());
				 	updateBill.setTransactionId(perpayReturn.getTransaction_id());
					updateBill.setPayStatus(ShopConstant.PAYED);
					updateBill.setPayDate(new Date());
					update(updateBill);
					isOk = true;
					msg = "OK";
				} else {
					msg = "非法响应！交易金额不对，支付方返回 " + totalFee + "分，而订单记录是 " + (ShopHelper.toCent(order.getTotalPrice())) + "分";
				}
			} else {
				msg = "非法响应！";
			}
		} else {
			msg = "交易失败";
		}

		responseResult.put("return_code", isOk ? "SUCCESS" : "FAIL");
		responseResult.put("return_msg", msg);

		return responseResult;
	}
}