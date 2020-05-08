package com.ajaxjs.shop.group;

import java.util.HashMap;
import java.util.Map;

import com.ajaxjs.framework.BaseService;
import com.ajaxjs.framework.Repository;
import com.ajaxjs.ioc.Bean;
import com.ajaxjs.ioc.Resource;
import com.ajaxjs.shop.dao.OrderInfoDao;
import com.ajaxjs.shop.model.OrderInfo;
import com.ajaxjs.shop.payment.wechat.MiniAppPay;
import com.ajaxjs.shop.payment.wechat.model.PerpayInfo;
import com.ajaxjs.shop.payment.wechat.model.PerpayReturn;
import com.ajaxjs.shop.service.CartService;
import com.ajaxjs.shop.service.OrderService;
import com.ajaxjs.util.logger.LogHelper;
import com.ajaxjs.weixin.payment.PayConstant;

/**
 * 订单核心业务
 * 
 * @author sp42 frank@ajaxjs.com
 *
 */
@Bean
public class WxPayService extends BaseService<OrderInfo> implements PayConstant {
	public static OrderInfoDao dao = new Repository().bind(OrderInfoDao.class);

	public static final LogHelper LOGGER = LogHelper.getLog(WxPayService.class);

	{
		setUiName("订单");
		setShortName("order");
		setDao(dao);
	}

	@Resource("OrderService")
	private OrderService orderService;

	/**
	 * 生成订单并支付
	 * 
	 * @param userId    用户 id
	 * @param addressId 送货地址id
	 * @param cartIds   订单明细由购物车的数据生成
	 * @return
	 */
	public Map<String, ?> createOrderAndPay(long userId, long addressId, String[] cartIds, String ip) {
		OrderInfo orderInfo = orderService.processOrder(userId, addressId, cartIds, 0);
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
		String openId = dao.findUserOpenId(userId); // 小程序内调用登录接口，获取到用户的 openid

		// 预付单
		PerpayInfo perpayInfo = new PerpayInfo();
		perpayInfo.setOpenid(openId);
		perpayInfo.setBody("集结号-网超");
		perpayInfo.setIp(ip);

//		Map<String, String> map = WxPay.unifiedOrder(orderInfo, perpayInfo);
//		PerpayReturn result = WxPay.sendUnifiedOrder(map); 

		PerpayReturn result = MiniAppPay.sendUnifiedOrder(orderInfo, perpayInfo);// 商户 server 调用支付统一下单

		if (result.isSuccess()) {
			LOGGER.info("获取 perpayid 成功！{0}", result.getPrepay_id());
//			Map<String, String> r = PaySignatures.getPayParam(result.getPrepay_id(), map.get("nonce_str")); // 商户 server 调用再次签名
			Map<String, String> r = MiniAppPay.getPayParam(result.getPrepay_id(), result.getNonce_str()); // 商户
																												// server
																												// 调用再次签名
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

	@Resource("CartService")
	private CartService cartService;

}